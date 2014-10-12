package engine.core.imp.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import engine.core.frame.DataManager;
import engine.core.frame.Entity;

public class GroupManager extends DataManager {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_groups"));

	private Map<String, List<Entity>> m_groupMap;

	public GroupManager() {
		m_groupMap = new HashMap<String, List<Entity>>();
	}

	public List<Entity> getEntities(String group) {
		if (!m_groupMap.containsKey(group))
			m_groupMap.put(group, new ArrayList<Entity>());
		return m_groupMap.get(group);
	}

	public void removeEntity(Entity entity) {
		for (List<Entity> entities : m_groupMap.values()) {
			if (entities.contains(entity))
				entities.remove(entity);
		}
	}

	@Override
	public void entityRegistered(Entity entity) {
		TagList groups = (TagList) entity.getData("sys_groups");
		for (String s : groups) {
			if (!m_groupMap.containsKey(s))
				m_groupMap.put(s, new ArrayList<Entity>());

			if (!m_groupMap.get(s).contains(entity))
				m_groupMap.get(s).add(entity);
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public void setData(Entity entity, String identifier, Object data) {
		if (identifier.equals("sys_groups")) {
			TagList groups = (TagList) data;
			for (String s : groups) {
				if (!m_groupMap.containsKey(s))
					m_groupMap.put(s, new ArrayList<Entity>());
				if (!m_groupMap.get(s).contains(entity))
					m_groupMap.get(s).add(entity);
			}
			entity.directSetData(identifier, data);
		}
	}

	@Override
	protected void update(float time) {

	}

	@Override
	public void updateData(Entity entity, String identifier) {

	}
}
