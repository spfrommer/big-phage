package engine.core.imp.physics.collision;

import engine.core.frame.Entity;

public class NoCollisionFilter implements CollisionFilter {
	public boolean canCollideWith(Entity entity) {
		return false;
	}
}
