package engine.core.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class World {
	private List<DataManager> m_managers = new ArrayList<DataManager>();
	private Map<String, DataManager> m_identifierRegister = new HashMap<String, DataManager>();

	private List<Entity> m_entities = new ArrayList<Entity>();

	public void addDataManager(DataManager dm) {
		m_managers.add(dm);
		for (String s : dm.getDataIdentifiers()) {
			if (!m_identifierRegister.containsKey(s)) {
				m_identifierRegister.put(s, dm);
			} else {
				throw new RuntimeException("Identifier " + s + " is registered by two DataManagers!");
			}
		}
	}

	public void removeDataManager(DataManager dm) {
		m_managers.remove(dm);
		for (String s : dm.getDataIdentifiers()) {
			m_identifierRegister.remove(s);
		}
		dm = DataManager.NONE;
	}

	public DataManager getRegisteredManager(String identifier) {
		DataManager manager = m_identifierRegister.get(identifier);
		if (manager == null)
			manager = DataManager.NONE;
		return manager;
	}

	public void addEntity(Entity e) {
		e.setWorld(this);
		m_entities.add(e);
	}

	public void removeEntity(Entity e) {
		e.setWorld(null);
		m_entities.remove(e);
	}

	public void update(float time) {
		updateData(time);
		updateComponents(time);
	}

	public void updateData(float time) {
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
	}

	public void updateComponents(float time) {
		for (Entity e : m_entities) {
			e.updateComponents(time);
		}
	}
}
