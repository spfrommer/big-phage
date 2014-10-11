package engine.core.imp.physics.collision;

import engine.core.frame.Entity;

public interface CollisionFilter {
	public boolean canCollideWith(Entity entity);
}
