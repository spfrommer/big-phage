package engine.core.framework;

import java.util.ArrayList;
import java.util.List;

public class World {
	private List<DataManager> m_managers = new ArrayList<DataManager>();

	private List<Entity> m_entities = new ArrayList<Entity>();

	public void addDataManager(DataManager dm) {
		m_managers.add(dm);
	}

	public void removeDataManager(DataManager dm) {
		m_managers.remove(dm);
	}

	public void addEntity(Entity e) {
		m_entities.add(e);
	}

	public void removeEntity(Entity e) {
		m_entities.remove(e);
	}

	public void update(float time) {
		updateData(time);
		updateComponents(time);
	}

	public void updateData(float time) {
		for (DataManager dm : m_managers) {
			dm.update(time);

			String[] identifiers = dm.getDataIdentifiers();

			for (Entity e : m_entities) {
				for (String identifier : identifiers) {
					if (e.hasDataFor(identifier)) {
						Object newData = dm.updateData(e, identifier);
						if (newData != null)
							e.setData(identifier, newData);
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
