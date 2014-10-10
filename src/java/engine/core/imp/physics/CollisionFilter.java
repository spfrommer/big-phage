package engine.core.imp.physics;

import engine.core.frame.Entity;

public interface CollisionFilter {
	public boolean canCollideWith(Entity entity);
}
