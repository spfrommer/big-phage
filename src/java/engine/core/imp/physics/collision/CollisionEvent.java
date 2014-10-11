package engine.core.imp.physics.collision;

import engine.core.frame.Entity;

public interface CollisionEvent {
	public abstract void collidedWith(Entity entity);
}
