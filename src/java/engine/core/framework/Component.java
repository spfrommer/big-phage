package engine.core.framework;

public abstract class Component {
	private Entity m_entity;

	/*public Entity getEntity() {
		return m_entity;
	}*/

	public void setEntity(Entity entity) {
		m_entity = entity;
	}

	protected Object getData(String identifier) {
		return m_entity.getData(identifier);
	}

	public abstract void update(float time);

	public abstract String[] getRequiredData();

	public abstract Object createObjectFor(String identifier);
}
