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
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.physics.liquid.LiquidDef;

public class PhysicsManager extends DataManager {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_type", "sys_body",
			"sys_position", "sys_rotation", "sys_liquid"));
	private Map<Entity, Body> m_bodies = new HashMap<Entity, Body>();
	private Map<Entity, Liquid> m_liquids = new HashMap<Entity, Liquid>();
	private World m_world = new World(new Vec2(0, -10));

	public PhysicsManager() {

	}

	/**
	 * Creates a Body for the Entity and adds it as a data field.
	 * 
	 * @param entity
	 * @param bodyDef
	 */
	public void createBody(Entity entity, BodyDef bodyDef) {
		if (m_bodies.containsKey(entity)) {
			m_world.destroyBody(m_bodies.get(entity));
		}

		Body body = m_world.createBody(bodyDef);
		m_bodies.put(entity, body);

		entity.directSet("sys_type", State.SOLID);
		entity.directSet("sys_body", body);
	}

	/**
	 * Creates a Liquid for the Entity, adds it, and returns it.
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

		entity.directSet("sys_type", State.LIQUID);
		entity.directSet("sys_liquid", liquid);
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

	private Liquid createLiquid(LiquidDef def) {
		List<Body> particles = new ArrayList<Body>();

		for (Vector2f p : def.getParticles()) {
			BodyDef bodyDef = PhysicsFactory.makeBodyDef(p, BodyType.DYNAMIC, 0, 0.9f);
			Body body = m_world.createBody(bodyDef);
			FixtureDef fix = PhysicsFactory.makeCircularFixtureDef(def.getParticleRadius(), 0.1f, 0f, 0f);
			body.createFixture(fix);
			particles.add(body);
		}

		return new Liquid(particles, def.getParticleRadius());
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
			liquid.applyForces(time);
		}

		m_world.step(time, 20, 20);
	}

	@Override
	public void updateData(Entity entity, String identifier) {
		State state = (State) entity.getData("sys_type");
		if (state == State.SOLID) {
			Body body = m_bodies.get(entity);

			if (identifier.equals("sys_position")) {
				entity.directSet("sys_position", new Vector2f(body.getPosition().x, body.getPosition().y));
				return;
			}

			if (identifier.equals("sys_rotation")) {
				entity.directSet("sys_rotation", body.getAngle());
				return;
			}
		} else if (state == State.LIQUID) {
		}
	}

	@Override
	public void setData(Entity entity, String identifier, Object data) {
		Body body = m_bodies.get(entity);
		if (identifier.equals("sys_position")) {
			body.m_xf.p.set(((Vector2f) data).x, ((Vector2f) data).y);
			updateData(entity, identifier);
		}
		if (identifier.equals("sys_rotation")) {
			body.m_xf.q.set((float) data);
			updateData(entity, identifier);
		}
	}
}
