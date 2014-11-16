package engine.core.imp.physics;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.dynamics.joints.WeldJointDef;

import engine.commons.utils.Vector2f;
import engine.core.imp.physics.liquid.LiquidDef;

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

	public static RevoluteJointDef makeRevoluteDef(Body b1, Body b2, Vector2f anchor1, Vector2f anchor2,
			boolean collideConnected, float referenceAngle) {
		RevoluteJointDef revolute = makeRevoluteDef(b1, b2, anchor1, anchor2, collideConnected);
		revolute.referenceAngle = referenceAngle;
		return revolute;
	}

	public static WeldJointDef makeWeldDef(Body b1, Body b2, Vector2f anchor1, Vector2f anchor2,
			boolean collideConnected, float referenceAngle) {
		WeldJointDef weld = new WeldJointDef();
		weld.bodyA = b1;
		weld.bodyB = b2;
		weld.localAnchorA.set(anchor1.x, anchor1.y);
		weld.localAnchorB.set(anchor2.x, anchor2.y);
		weld.collideConnected = collideConnected;
		weld.referenceAngle = referenceAngle;
		return weld;
	}

	public static BodyDef makeBodyDef(Vector2f position, BodyType type, float rot, float linearDampening) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.angle = rot;
		bodyDef.position.set(position.x, position.y);
		bodyDef.type = type;
		bodyDef.linearDamping = linearDampening;
		return bodyDef;
	}

	public static FixtureDef makeRectangularFixtureDef(Vector2f dimensions, float rot, float density, float friction,
			float restitution) {
		PolygonShape rectangle = new PolygonShape();
		rectangle.setAsBox(dimensions.x / 2, dimensions.y / 2, new Vec2(0, 0), rot);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = rectangle;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	public static FixtureDef makeTriangularFixtureDef(Vector2f dimensions, float rot, float density, float friction,
			float restitution) {
		PolygonShape triangle = new PolygonShape();
		triangle.set(new Vec2[] { new Vec2(-dimensions.x / 2, -dimensions.y / 2),
				new Vec2(dimensions.x / 2, -dimensions.y / 2), new Vec2(0, dimensions.y / 2) }, 3);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = triangle;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	public static FixtureDef makeCircularFixtureDef(float radius, float density, float friction, float restitution) {
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restitution;
		return fixtureDef;
	}

	/**
	 * Makes a LiquidDef by populating a polygon with particles particleDistance distance apart and of radius
	 * particleRadius.
	 * 
	 * @param poly
	 * @param particleDistance
	 * @param particleRadius
	 * @param particleDensity
	 * @param origin
	 * @param maxDist
	 * @return the LiquidDef
	 */
	public static LiquidDef makeLiquidDef(Path2D poly, float particleDistance, float particleRadius,
			float particleDensity, Vector2f origin, float maxDist) {
		LiquidDef liquid = new LiquidDef();
		liquid.setParticleRadius(particleRadius);
		liquid.setDensity(particleDensity);
		Rectangle2D bounds = poly.getBounds2D();
		float x = (float) bounds.getMinX();
		float y = (float) bounds.getMinY();

		while (y < bounds.getMaxY()) {
			while (x < bounds.getMaxX()) {
				x += particleDistance;
				if (poly.contains(x, y)) {
					liquid.addParticle(new Vector2f(x, y));
				}
			}

			y += particleDistance;
			x = (float) bounds.getMinX();
		}
		liquid.setOrigin(origin);
		liquid.setMaxDist(maxDist);
		return liquid;
	}
}