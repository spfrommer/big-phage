package engine.core.imp.physics.collision;

import engine.core.frame.Entity;

/**
 * Allows collisions with all Entities but those who belong to certain groups ("sys_groups" field).
 */
public class ExclusiveGroupFilter implements CollisionFilter {
	private TagList m_exclude;

	public ExclusiveGroupFilter(TagList exclude) {
		m_exclude = exclude;
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		TagList tags = (TagList) entity.getData("sys_groups");
		if (m_exclude.intersects(tags))
			return false;

		return true;
	}
}
