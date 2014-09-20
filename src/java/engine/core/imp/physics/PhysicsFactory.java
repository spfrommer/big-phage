package engine.core.imp.physics;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class PhysicsFactory {
	private PhysicsFactory() {

	}

	public static RevoluteJointDef makeRevoluteDef(Body b1, Body b2, Vector2f anchor1, Vector2f anchor2,
			boolean collideConnected) {
		RevoluteJointDef revolute = new RevoluteJointDef();
		revolute.bodyA = b1;
		revolute.bodyB = b2;
		revolute.localAnchorA = new Vec2(anchor1.x, anchor1.y);
		revolute.localAnchorB = new Vec2(anchor2.x, anchor2.y);
		revolute.collideConnected = collideConnected;
		return revolute;
	}

	public static BodyDef makeBodyDef(Vector2f position, BodyType type, float rot) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle = rot;
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = type;
		return bodyDef;
	}

	public static FixtureDef makeRectangularFixtureDef(Vector2f position, Vector2f dimensions, float rot) {
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(dimensions.x / 2, dimensions.y / 2, new Vec2(position.x, position.y), rot);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1;
		fixtureDef.friction = 0.3f;
		return fixtureDef;
	}
}
