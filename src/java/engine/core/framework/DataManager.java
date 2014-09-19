package engine.core.framework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DataManager {
	private List<Entity> m_registeredEntities = new ArrayList<Entity>();

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

	public abstract void entityRegistered(Entity entity);

	public abstract Set<String> getDataIdentifiers();

	public abstract void setData(Entity entity, String identifier, Object data);

	protected abstract void update(float time);

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
			entity.directSet(identifier, data);
		}
	};
}
