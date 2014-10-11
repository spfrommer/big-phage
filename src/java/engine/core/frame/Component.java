package engine.core.frame;

import java.util.Set;

import engine.core.exec.GameState;

public abstract class Component {
	private Entity m_entity;
	private World m_world;

	protected Entity getEntity() {
		return m_entity;
	}

	public void setEntity(Entity entity) {
		m_entity = entity;
	}

	protected World getWorld() {
		return m_world;
	}

	public void setWorld(World world) {
		m_world = world;
	}

	protected Object getData(String identifier) {
		return m_entity.getData(identifier);
	}

	protected void setData(String identifier, Object data) {
		m_entity.setData(identifier, data);
	}

	public abstract void update(float time, GameState state);

	public abstract Set<String> getDataIdentifiers();
}
