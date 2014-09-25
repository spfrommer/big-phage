package engine.core.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Entity {
	private World m_world;

	private Map<String, ManagedData> m_data = new HashMap<String, ManagedData>();
	private List<Component> m_components = new ArrayList<Component>();
	private List<EntityListener> m_listeners = new ArrayList<EntityListener>();

	public Entity(World world) {
		m_world = world;
	}

	public void setWorld(World world) {
		m_world = world;

		for (Component c : m_components) {
			Set<String> identifiers = c.getDataIdentifiers();
			for (String s : identifiers) {
				DataManager registered = m_world.getRegisteredManager(s);
				m_data.get(s).manager = registered;
				registered.checkRegister(this);
			}
		}
	}

	public void addComponent(Component c) {
		Set<String> identifiers = c.getDataIdentifiers();
		for (String s : identifiers) {
			if (!hasDataFor(s)) {
				DataManager registered = m_world.getRegisteredManager(s);
				m_data.put(s, new ManagedData(new Data(m_world.getRegisteredInitializer(s).createObjectFor(s), 1),
						registered));
				registered.checkRegister(this);
			} else {
				m_data.get(s).data.componentsUsing++;
			}
		}

		c.setEntity(this);
		m_components.add(c);
	}

	public void removeComponent(Component c) {
		Set<String> identifiers = c.getDataIdentifiers();
		for (String s : identifiers) {
			m_data.get(s).data.componentsUsing--;
			if (m_data.get(s).data.componentsUsing == 0) {
				m_data.remove(s);
			}
		}

		c.setEntity(null);
		m_components.remove(c);
	}

	/*public List<Component> getComponents() {
		return m_components;
	}*/

	public void updateComponents(float time) {
		for (Component c : m_components) {
			c.update(time);
		}
	}

	public void fireDataChanged(String identifier) {
		for (EntityListener e : m_listeners)
			e.dataChanged(this, identifier);
	}

	public boolean hasDataFor(String identifier) {
		return m_data.containsKey(identifier);
	}

	public Set<String> getDataIdentifiers() {
		return m_data.keySet();
	}

	public Object getData(String identifier) {
		return m_data.get(identifier).data.data;
	}

	public void setData(String identifier, Object data) {
		if (!hasDataFor(identifier)) {
			m_data.put(identifier, new ManagedData(new Data(null, 0), m_world.getRegisteredManager(identifier)));
		}

		m_data.get(identifier).manager.setData(this, identifier, data);
	}

	public void directSet(String identifier, Object data) {
		if (!hasDataFor(identifier)) {
			m_data.put(identifier, new ManagedData(new Data(data, 0), m_world.getRegisteredManager(identifier)));
		} else {
			m_data.get(identifier).data.data = data;
		}
	}

	public class ManagedData {
		public Data data;
		public DataManager manager;

		public ManagedData(Data data, DataManager dataManager) {
			this.data = data;
			this.manager = dataManager;
		}
	}
}
