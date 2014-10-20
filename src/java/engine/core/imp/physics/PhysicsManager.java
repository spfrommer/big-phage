package engine.core.imp.physics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;

import engine.commons.utils.Vector2f;
import engine.core.frame.DataManager;
import engine.core.frame.Entity;
import engine.core.imp.physics.collision.ManagerFilter;
import engine.core.imp.physics.collision.ManagerHandler;
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.physics.liquid.LiquidDef;
import engine.core.imp.physics.liquid.PhysicsConstants;

public class PhysicsManager extends DataManager {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_type", "sys_body",
			"sys_position", "sys_rotation", "sys_liquid"));
	private List<Body> m_toBeRemoved = new ArrayList<Body>();
	private Map<Entity, Body> m_bodies = new HashMap<Entity, Body>();
	private Map<Entity, Liquid> m_liquids = new HashMap<Entity, Liquid>();
	private World m_world;
	private ManagerFilter m_collisionFilter = new ManagerFilter();
	private ManagerHandler m_collisionHandler = new ManagerHandler();

	public PhysicsManager() {
		m_world = new World(new Vec2(0, -10));
		m_world.setContactFilter(m_collisionFilter);
		m_world.setContactListener(m_collisionHandler);
	}

	public PhysicsManager(Vector2f gravity) {
		m_world = new World(new Vec2(gravity.x, gravity.y));
		m_world.setContactFilter(m_collisionFilter);
		m_world.setContactListener(m_collisionHandler);
	}

	public void setGravity(Vector2f gravity) {
		m_world.setGravity(new Vec2(gravity.x, gravity.y));
	}

	/**
	 * @return the collision filter
	 */
	public ManagerFilter getCollisionFilter() {
		return m_collisionFilter;
	}

	/**
	 * @return the collision handler
	 */
	public ManagerHandler getCollisionHandler() {
		return m_collisionHandler;
	}

	/**
	 * Creates a Body for the Entity and adds it as a data field.
	 * 
	 * @param entity
	 * @param bodyDef
	 */
	public void createSolid(Entity entity, BodyDef bodyDef) {
		if (m_bodies.containsKey(entity)) {
			m_world.destroyBody(m_bodies.get(entity));
		}

		Body body = m_world.createBody(bodyDef);
		body.setUserData(entity);
		m_bodies.put(entity, body);

		entity.directSetData("sys_type", State.SOLID);
		entity.directSetData("sys_body", body);
	}

	/**
	 * Will make a Body not associated with an Entity (it will not receive collision events). In order to do this,
	 * either use createSolid() or set the Body's user data to be the Entity that should be associated with the Event;
	 * also add the Event that should be triggered to the CollisionHandler.
	 * 
	 * @param bodyDef
	 * @return a Body
	 */
	public Body createBody(BodyDef bodyDef) {
		return m_world.createBody(bodyDef);
	}

	/**
	 * Destroys a Body.
	 * 
	 * @param body
	 */
	public void destroyBody(Body body) {
		m_toBeRemoved.add(body);
		// m_world.destroyBody(body);
	}

	/**
	 * Destroys a Body.
	 * 
	 * @param entity
	 * @param body
	 */
	public void destroyBody(Entity entity) {
		m_bodies.remove(entity);
		destroyBody((Body) entity.getData("sys_body"));
	}

	/**
	 * Creates a Liquid for the Entity and adds it as a data field.
	 * 
	 * @param entity
	 * @param liquidDef
	 */
	public void createLiquid(Entity entity, LiquidDef liquidDef) {
		if (m_liquids.containsKey(entity)) {
			Liquid liquid = m_liquids.get(entity);
			for (Body b : liquid.getParticles()) {
				m_world.destroyBody(b);
			}
		}

		Liquid liquid = createLiquid(liquidDef);
		m_liquids.put(entity, liquid);

		entity.directSetData("sys_type", State.LIQUID);
		entity.directSetData("sys_liquid", liquid);
	}

	/**
	 * @param def
	 * @return the created Liquid
	 */
	private Liquid createLiquid(LiquidDef def) {
		List<Body> particles = new ArrayList<Body>();

		for (Vector2f p : def.getParticles()) {
			BodyDef bodyDef = PhysicsFactory.makeBodyDef(p, BodyType.DYNAMIC, 0f, PhysicsConstants.LIQUID_DAMPENING);
			Body body = m_world.createBody(bodyDef);
			FixtureDef fix = PhysicsFactory.makeCircularFixtureDef(def.getParticleRadius(), def.getDensity(),
					PhysicsConstants.LIQUID_FRICTION, PhysicsConstants.LIQUID_RESTITUTION);
			body.createFixture(fix);
			particles.add(body);
		}

		return new Liquid(particles, def.getParticleRadius(), def.getDensity(), def.getOrigin(), def.getMaxDist());
	}

	/**
	 * Creates a Joint and returns it.
	 * 
	 * @param def
	 * @return the created Joint
	 */
	public Joint createJoint(JointDef def) {
		return m_world.createJoint(def);
	}

	/**
	 * Destroys a Joint.
	 * 
	 * @param joint
	 */
	public void destroyJoint(Joint joint) {
		m_world.destroyJoint(joint);
	}

	@Override
	public void entityRegistered(Entity entity) {

	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public void update(float time) {
		for (Liquid liquid : m_liquids.values()) {
			liquid.applyForces(time, this);
		}
		m_world.step(time, 20, 20);
		for (Body b : m_toBeRemoved) {
			m_world.destroyBody(b);
		}
		m_toBeRemoved.clear();
	}

	@Override
	public void updateData(Entity entity, String identifier) {
		State state = (State) entity.getData("sys_type");
		if (state == State.SOLID) {
			Body body = m_bodies.get(entity);

			if (identifier.equals("sys_position")) {
				entity.directSetData("sys_position", new Vector2f(body.getPosition().x, body.getPosition().y));
				return;
			}

			if (identifier.equals("sys_rotation")) {
				entity.directSetData("sys_rotation", body.getAngle());
				return;
			}
		} else if (state == State.LIQUID) {
		}
	}

	@Override
	public void setData(Entity entity, String identifier, Object data) {
		Body body = m_bodies.get(entity);
		if (identifier.equals("sys_position")) {
			// body.m_xf.p.set(((Vector2f) data).x, ((Vector2f) data).y);
			body.setTransform(new Vec2(((Vector2f) data).x, ((Vector2f) data).y), body.getAngle());
			updateData(entity, identifier);
		}
		if (identifier.equals("sys_rotation")) {
			body.setTransform(body.getTransform().p, (Float) data);
			updateData(entity, identifier);
		}
	}
}
