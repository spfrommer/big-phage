package engine.core.framework;

public interface DataManager {
	public String[] getDataIdentifiers();

	public void update(float time);

	public Object updateData(Entity entity, String identifier);
}
