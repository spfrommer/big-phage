package test;

import java.awt.Graphics2D;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import engine.core.framework.Entity;
import engine.core.framework.World;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.render.GraphicsContext;
import engine.core.imp.render.Painter;
import engine.core.imp.render.RenderComponent;
import engine.core.imp.render.SWINGDisplay;

public class FrameworkTest implements Painter {
	private World m_world = new World();
	private PhysicsManager physics = new PhysicsManager();
	private GraphicsContext m_graphics = new GraphicsContext();

	public FrameworkTest() {
		SWINGDisplay display = new SWINGDisplay();
		display.setSize(500, 500);
		display.setVisible(true);
		display.addPainter(this);

		m_world.addDataManager(physics);

		Entity entity = new Entity();
		entity.addComponent(new RenderComponent(m_graphics));
		// entity.addComponent(new PositionPrinterComponent());

		Body body = physics.setBody(entity, makeBodyDef(0, -10, BodyType.DYNAMIC));
		body.createFixture(makeRectangularFixtureDef(0, 0, 5, 5, 0));

		m_world.addEntity(entity);

		while (true) {
			display.repaint();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private long lastPaint = System.currentTimeMillis();

	@Override
	public void paint(Graphics2D g2d) {
		System.out.println(System.currentTimeMillis() - lastPaint);
		m_graphics.graphics = g2d;
		m_world.update(1);
		lastPaint = System.currentTimeMillis();
	}

	public static BodyDef makeBodyDef(float x, float y, BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(x, y);
		bodyDef.type = type;
		return bodyDef;
	}

	public static FixtureDef makeRectangularFixtureDef(float x, float y, float width, float height, float rot) {
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(width / 2, height / 2, new Vec2(x, y), rot);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1;
		fixtureDef.friction = 0.3f;
		return fixtureDef;
	}

	public static void main(String[] main) {
		new FrameworkTest();
	}
	/*public static void testPhysics() {
		// Static Body
		Vec2 gravity = new Vec2(0, -10);
		World world = new World(gravity);

		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(0, -10);
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(50, 10);
		groundBody.createFixture(groundBox, 0);

		// Dynamic Body
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DYNAMIC;
		bodyDef.position.set(0, 4);
		Body body = world.createBody(bodyDef);
		PolygonShape dynamicBox = new PolygonShape();
		dynamicBox.setAsBox(1, 1);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = dynamicBox;
		fixtureDef.density = 1;
		fixtureDef.friction = 0.3f;
		body.createFixture(fixtureDef);

		// Setup world
		float timeStep = 1.0f / 60.0f;
		int velocityIterations = 6;
		int positionIterations = 2;

		// Run loop
		for (int i = 0; i < 60; ++i) {
			world.step(timeStep, velocityIterations, positionIterations);
			Vec2 position = body.getPosition();
			float angle = body.getAngle();
			System.out.printf("%4.2f %4.2f %4.2f\n", position.x, position.y, angle);
		}
	}*/
}
