package engine.core.frame;

import java.util.Set;

import engine.core.exec.GameState;

public abstract class Component {
	private Entity m_entity;

	protected Entity getEntity() {
		return m_entity;
	}

	public void setEntity(Entity entity) {
		m_entity = entity;
	}

	protected Object getData(String identifier) {
		return m_entity.getData(identifier);
	}

	protected void setData(String identifier, Object data) {
		m_entity.setData(identifier, data);
	}

	public abstract void update(float time, GameState state);

	public abstract Set<String> getDataIdentifiers();

	// public abstract Object createObjectFor(String identifier);
}
