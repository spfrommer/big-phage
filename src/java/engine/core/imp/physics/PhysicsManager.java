package engine.core.imp.physics;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import engine.core.framework.DataManager;
import engine.core.framework.Entity;

public class PhysicsManager extends DataManager {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation"));
	private Map<Entity, Body> m_bodies = new HashMap<Entity, Body>();
	private World m_world = new World(new Vec2(0, -10));

	public Body setBody(Entity entity, BodyDef bodyDef) {
		if (m_bodies.containsKey(entity)) {
			m_world.destroyBody(m_bodies.get(entity));
		}

		Body body = m_world.createBody(bodyDef);
		m_bodies.put(entity, body);
		return body;

	}

	@Override
	public void entityRegistered(Entity entity) {
		if (!m_bodies.containsKey(entity)) {
			// add default body
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public void update(float time) {
		m_world.step(1f / 60f, 20, 20);
	}

	@Override
	public void updateData(Entity entity, String identifier) {
		Body body = m_bodies.get(entity);
		if (identifier.equals("sys_position")) {
			entity.directSet("sys_position", new Vector2f(body.getPosition().x, body.getPosition().y));
		}
		if (identifier.equals("sys_rotation"))
			entity.directSet("sys_rotation", (double) body.getAngle());
	}

	@Override
	public void setData(Entity entity, String identifier, Object data) {
		Body body = m_bodies.get(entity);
		if (identifier.equals("sys_position")) {
			body.m_xf.p.set(((Vector2f) data).x, ((Vector2f) data).y);
			updateData(entity, identifier);
		}
	}
}
