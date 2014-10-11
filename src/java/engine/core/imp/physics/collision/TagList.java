package engine.core.imp.physics.collision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagList {
	private ArrayList<String> m_tags = new ArrayList<String>();

	public TagList() {
	}

	public TagList(String... strings) {
		m_tags.addAll(Arrays.asList(strings));
	}

	public void addGroup(String group) {
		m_tags.add(group);
	}

	public void removeGroup(String group) {
		m_tags.remove(group);
	}

	public boolean containsGroup(String group) {
		return m_tags.contains(group);
	}

	@SuppressWarnings("unchecked")
	public boolean intersects(TagList tags) {
		List<String> tagsClone = (List<String>) m_tags.clone();
		tagsClone.retainAll(tags.m_tags);
		return !tagsClone.isEmpty();
	}
}
