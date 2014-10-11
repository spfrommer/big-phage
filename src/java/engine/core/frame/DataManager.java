package engine.core.frame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DataManager {
	// just a list for calling entityRegistered() - not used in any other way
	private List<Entity> m_registeredEntities = new ArrayList<Entity>();

	/**
	 * Check if the DataManager manages the fields of the Entity, and if it does, adds the Entity and calls
	 * entityRegistered().
	 * 
	 * @param entity
	 */
	public final void checkRegister(Entity entity) {
		if (!m_registeredEntities.contains(entity)) {
			Set<String> intersection = new HashSet<String>(entity.getDataIdentifiers());
			intersection.retainAll(getDataIdentifiers());
			if (intersection.size() > 0) {
				m_registeredEntities.add(entity);
				entityRegistered(entity);
			}
		}
	}

	/**
	 * Called when an Entity is added with fields we can set.
	 * 
	 * @param entity
	 */
	public abstract void entityRegistered(Entity entity);

	/**
	 * @return the data this manager manages.
	 */
	public abstract Set<String> getDataIdentifiers();

	/**
	 * Allows the DataManager to handle requests to set data.
	 * 
	 * @param entity
	 * @param identifier
	 * @param data
	 */
	public abstract void setData(Entity entity, String identifier, Object data);

	protected abstract void update(float time);

	/**
	 * Called once every update loop by the world.
	 * 
	 * @param entity
	 * @param identifier
	 */
	public abstract void updateData(Entity entity, String identifier);

	public static final DataManager NONE = new DataManager() {
		@Override
		public void entityRegistered(Entity entity) {

		}

		@Override
		public Set<String> getDataIdentifiers() {
			return new HashSet<String>();
		}

		@Override
		public void update(float time) {
		}

		@Override
		public void updateData(Entity entity, String identifier) {

		}

		@Override
		public void setData(Entity entity, String identifier, Object data) {
			entity.directSetData(identifier, data);
		}
	};
}
