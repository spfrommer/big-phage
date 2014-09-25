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

import engine.core.framework.DataManager;
import engine.core.framework.Entity;

public class PhysicsManager extends DataManager {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_type", "sys_position",
			"sys_rotation", "sys_particles", "sys_particlesize"));
	private Map<Entity, Body> m_bodies = new HashMap<Entity, Body>();
	private Map<Entity, Liquid> m_liquids = new HashMap<Entity, Liquid>();
	private World m_world = new World(new Vec2(0, -10));

	/**
	 * Creates a Body for the Entity, adds it, and returns it.
	 * 
	 * @param entity
	 * @param bodyDef
	 * @return the created Body
	 */
	public Body createBody(Entity entity, BodyDef bodyDef) {
		if (m_bodies.containsKey(entity)) {
			m_world.destroyBody(m_bodies.get(entity));
		}

		Body body = m_world.createBody(bodyDef);
		m_bodies.put(entity, body);

		entity.directSet("sys_type", State.SOLID);
		return body;
	}

	/**
	 * Creates a Liquid for the Entity, adds it, and returns it.
	 * 
	 * @param entity
	 * @param liquidDef
	 * @return the created Liquid
	 */
	public Liquid createLiquid(Entity entity, LiquidDef liquidDef) {
		if (m_liquids.containsKey(entity)) {
			Liquid liquid = m_liquids.get(entity);
			for (Body b : liquid.getParticles()) {
				m_world.destroyBody(b);
			}
		}

		Liquid liquid = createLiquid(liquidDef);
		m_liquids.put(entity, liquid);

		entity.directSet("sys_type", State.LIQUID);
		return liquid;
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
	 * @param entity
	 * @return the Body associated with the Entity
	 */
	public Body getBody(Entity entity) {
		return m_bodies.get(entity);
	}

	@Override
	public void entityRegistered(Entity entity) {
		if (!m_bodies.containsKey(entity)) {

		}
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

		m_world.step(1f / 60f, 20, 20);
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
				entity.directSet("sys_rotation", (double) body.getAngle());
				return;
			}
		} else if (state == State.LIQUID) {
			Liquid liquid = m_liquids.get(entity);
			if (identifier.equals("sys_particles")) {
				hasdata: {
					@SuppressWarnings("unchecked")
					List<Vector2f> particles = (List<Vector2f>) entity.getData("sys_particles");
					List<Body> bParticles = liquid.getParticles();
					if (particles.size() != bParticles.size()) {
						break hasdata;
					}
					for (int i = 0; i < particles.size(); i++) {
						particles.get(i).set(bParticles.get(i).getPosition());
					}
					return;
				}

				List<Vector2f> particles = new ArrayList<Vector2f>();
				for (Body p : liquid.getParticles())
					particles.add(new Vector2f(p.getPosition()));
				entity.directSet("sys_particles", particles);
			}

			if (identifier.equals("sys_particlesize")) {
				entity.directSet("sys_particlesize", liquid.getParticleSize());
			}
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
