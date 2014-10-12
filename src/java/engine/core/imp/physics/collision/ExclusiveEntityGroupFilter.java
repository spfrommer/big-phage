package engine.core.imp.physics.collision;

import engine.core.frame.Entity;
import engine.core.imp.group.TagList;

public class ExclusiveEntityGroupFilter implements CollisionFilter {
	private TagList m_exclude;
	private Entity m_excludeEntity;

	public ExclusiveEntityGroupFilter(TagList exclude, Entity excludeEntity) {
		m_exclude = exclude;
		m_excludeEntity = excludeEntity;
	}

	public boolean canCollideWith(Entity entity) {
		TagList tags = (TagList) entity.getData("sys_groups");
		if (entity.equals(m_excludeEntity) || m_exclude.intersects(tags))
			return false;

		return true;
	}
}
