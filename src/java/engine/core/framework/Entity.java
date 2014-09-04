package engine.core.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entity {
	private Map<String, Data> m_data = new HashMap<String, Data>();
	private List<Component> m_components = new ArrayList<Component>();
	private List<EntityListener> m_listeners = new ArrayList<EntityListener>();

	public void addComponent(Component c) {
		String[] identifiers = c.getRequiredData();
		for (String s : identifiers) {
			if (!hasDataFor(s)) {
				m_data.put(s, new Data(c.createObjectFor(s), 1));
			} else {
				m_data.get(s).componentsUsing++;
			}
		}

		c.setEntity(this);
		m_components.add(c);
	}

	public void removeComponent(Component c) {
		String[] identifiers = c.getRequiredData();
		for (String s : identifiers) {
			m_data.get(s).componentsUsing--;
			if (m_data.get(s).componentsUsing == 0) {
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
		System.out.println("Updating components " + m_components.size());
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

	public Object getData(String identifier) {
		return m_data.get(identifier).data;
	}

	public void setData(String identifier, Object data) {
		if (hasDataFor(identifier)) {
			m_data.get(identifier).data = data;
		}
		m_data.put(identifier, new Data(data, 0));
	}
}
