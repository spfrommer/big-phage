package engine.core.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.core.exec.GameState;

/**
 * An entity in the world. Data will automatically be assigned the default world manager, unless otherwise specified
 * with setDataManager(identifer, manager).
 */
public class Entity {
	private World m_world;

	private Map<String, ManagedData> m_data = new HashMap<String, ManagedData>();
	private List<Component> m_components = new ArrayList<Component>();
	private List<EntityListener> m_listeners = new ArrayList<EntityListener>();

	private int m_updateOrder = 0;

	public Entity(World world) {
		m_world = world;
	}

	public void setUpdateOrder(UpdateOrder order) {
		m_world.setUpdateOrder(this, order);
	}

	public void setUpdateOrder(int order) {
		m_world.setUpdateOrder(this, order);
	}

	/**
	 * Do not call this method. Call setOrder(order) instead.
	 * 
	 * @param order
	 */
	protected void directSetOrder(int order) {
		m_updateOrder = order;
	}

	public int getUpdateOrder() {
		return m_updateOrder;
	}

	public void setWorld(World world) {
		m_world = world;
	}

	public void resetWorld(World world) {
		m_world = world;

		for (Component c : m_components) {
			Set<String> identifiers = c.getDataIdentifiers();
			for (String s : identifiers) {
				DataManager registered = m_world.getRegisteredManager(s);
				m_data.get(s).manager = registered;
				// registered.checkRegister(this);
			}
		}
	}

	public void addComponent(Component comp) {
		Set<String> identifiers = comp.getDataIdentifiers();
		for (String initializer : identifiers) {
			if (!hasDataFor(initializer)) {
				DataManager registered = m_world.getRegisteredManager(initializer);

				m_data.put(initializer, new ManagedData(new Data(m_world.getRegisteredInitializer(initializer)
						.createObjectFor(initializer)), registered));
				// registered.checkRegister(this);
			}
		}

		comp.setEntity(this);
		comp.setWorld(m_world);
		m_components.add(comp);
	}

	public void removeComponent(Component comp) {
		comp.setEntity(null);
		comp.setWorld(null);
		m_components.remove(comp);
	}

	public void updateData(float time) {
		for (String identifier : m_data.keySet()) {
			m_data.get(identifier).manager.updateData(this, identifier);
		}
	}

	public void updateComponents(float time, GameState state) {
		for (Component c : m_components) {
			c.update(time, state);
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
		if (!hasDataFor(identifier)) {
			DataManager registered = m_world.getRegisteredManager(identifier);

			m_data.put(identifier, new ManagedData(new Data(m_world.getRegisteredInitializer(identifier)
					.createObjectFor(identifier)), registered));
		}
		return m_data.get(identifier).data.data;
	}

	public void setData(String identifier, Object data) {
		if (!hasDataFor(identifier)) {
			m_data.put(identifier, new ManagedData(new Data(null), m_world.getRegisteredManager(identifier)));
		}

		m_data.get(identifier).manager.setData(this, identifier, data);
	}

	public void setManager(String identifier, DataManager manager) {
		m_data.get(identifier).manager = manager;
	}

	/**
	 * Do not call this method. Call setData(identifier, data) instead.
	 * 
	 * @param identifier
	 * @param data
	 */
	public void directSetData(String identifier, Object data) {
		if (!hasDataFor(identifier)) {
			m_data.put(identifier, new ManagedData(new Data(data), m_world.getRegisteredManager(identifier)));
		} else {
			m_data.get(identifier).data.data = data;
		}
	}

	public void dumpData() {
		for (String s : m_data.keySet()) {
			System.out.println(s + " : " + m_data.get(s).data.data);
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
