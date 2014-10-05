package engine.core.frame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
	private List<DataManager> m_managers = new ArrayList<DataManager>();
	private Map<String, DataManager> m_managerRegister = new HashMap<String, DataManager>();
	private Map<String, FieldInitializer> m_initializerRegister = new HashMap<String, FieldInitializer>();

	private List<Entity> m_entities = new ArrayList<Entity>();

	private static final Comparator<Entity> s_updateComparator = new UpdateComparator();
	private int m_lastUpdate = -1;

	public void addDataManager(DataManager dm) {
		m_managers.add(dm);
		for (String s : dm.getDataIdentifiers()) {
			if (!m_managerRegister.containsKey(s)) {
				m_managerRegister.put(s, dm);
			} else {
				throw new RuntimeException("Identifier " + s + " is registered by two DataManagers!");
			}
		}
	}

	public void removeDataManager(DataManager dm) {
		m_managers.remove(dm);
		for (String s : dm.getDataIdentifiers()) {
			m_managerRegister.remove(s);
		}
	}

	public DataManager getRegisteredManager(String identifier) {
		DataManager manager = m_managerRegister.get(identifier);
		if (manager == null)
			manager = DataManager.NONE;
		return manager;
	}

	public void addFieldInitializer(FieldInitializer fi) {
		for (String s : fi.getDataIdentifiers()) {
			if (!m_initializerRegister.containsKey(s)) {
				m_initializerRegister.put(s, fi);
			} else {
				throw new RuntimeException("Identifier " + s + " is registered by two FieldInitializers!");
			}
		}
	}

	public void removeFieldInitializer(FieldInitializer fi) {
		for (String s : fi.getDataIdentifiers()) {
			m_initializerRegister.remove(s);
		}
	}

	public FieldInitializer getRegisteredInitializer(String identifier) {
		FieldInitializer initializer = m_initializerRegister.get(identifier);
		if (initializer == null)
			initializer = FieldInitializer.NONE;
		return initializer;
	}

	public void addEntity(Entity e) {
		e.setWorld(this);
		m_entities.add(e);
	}

	public void removeEntity(Entity e) {
		e.setWorld(null);
		m_entities.remove(e);
	}

	public void setUpdateOrder(Entity e, UpdateOrder order) {
		if (order == UpdateOrder.FIRST) {
			setUpdateOrder(e, 0);
		} else {
			setUpdateOrder(e, ++m_lastUpdate);
		}
	}

	public void setUpdateOrder(Entity e, int order) {
		e.directSetOrder(order);
		m_lastUpdate = Math.max(m_lastUpdate, order);
		Collections.sort(m_entities, s_updateComparator);
	}

	public void update(float time) {
		// updateData(time);
		for (DataManager dm : m_managers)
			dm.update(time);
		updateEntities(time);
	}

	/*private void updateData(float time) {
		for (DataManager dm : m_managers) {
			dm.update(time);

			Set<String> identifiers = dm.getDataIdentifiers();

			for (Entity e : m_entities) {
				for (String identifier : identifiers) {
					if (e.hasDataFor(identifier)) {
						dm.updateData(e, identifier);
					}
				}
			}
		}
	}*/

	private void updateEntities(float time) {
		for (Entity e : m_entities) {

			e.updateData(time);
			e.updateComponents(time);
		}
	}

	private static class UpdateComparator implements Comparator<Entity> {
		@Override
		public int compare(Entity entity1, Entity entity2) {
			int update1 = entity1.getUpdateOrder();
			int update2 = entity2.getUpdateOrder();
			if (update1 < update2)
				return -1;
			if (update1 > update2)
				return 1;
			return 0;
		}
	}
}
