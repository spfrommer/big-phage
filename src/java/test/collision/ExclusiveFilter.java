package test.collision;

import engine.core.frame.Entity;
import engine.core.imp.physics.CollisionFilter;

/**
 * Allows collisions with all but one Entity.
 */
public class ExclusiveFilter implements CollisionFilter {
	private Entity m_exclude;

	public ExclusiveFilter(Entity exclude) {
		m_exclude = exclude;
	}

	@Override
	public boolean canCollideWith(Entity entity) {
		return entity != m_exclude;
	}
}
