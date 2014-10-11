package test.collision;

import engine.core.frame.Entity;
import engine.core.imp.physics.collision.CollisionEvent;

public class PrintEvent implements CollisionEvent {
	public void collidedWith(Entity entity) {
		System.out.println("Collided");
	}
}
