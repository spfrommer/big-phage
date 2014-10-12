package engine.core.imp.physics.collision;

import engine.core.frame.Entity;
import engine.core.imp.group.TagList;

/**
 * Allows collisions with all Entities who belong to certain groups
 * ("sys_groups" field).
 */
public class InclusiveGroupFilter implements CollisionFilter {
	private TagList m_include;

	public InclusiveGroupFilter(TagList include) {
		m_include = include;
	}

	public boolean canCollideWith(Entity entity) {
		TagList tags = (TagList) entity.getData("sys_groups");
		if (m_include.intersects(tags))
			return true;

		return false;
	}
}
