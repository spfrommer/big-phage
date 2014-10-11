package engine.core.imp.group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * An immutable list of tags - used for groups.
 */
public class TagList implements Iterable<String> {
	private ArrayList<String> m_tags = new ArrayList<String>();

	public TagList(String... strings) {
		m_tags.addAll(Arrays.asList(strings));
	}

	public Iterator<String> iterator() {
		return m_tags.iterator();
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
