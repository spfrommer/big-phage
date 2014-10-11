package engine.core.imp.physics.collision;

import engine.core.frame.Entity;

/**
 * Allows collisions with all but one Entity.
 */
public class ExclusiveEntityFilter implements CollisionFilter {
	private Entity m_exclude;

	public ExclusiveEntityFilter(Entity exclude) {
		m_exclude = exclude;
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return entity != m_exclude;
	}
}
