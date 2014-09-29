package test;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import engine.core.framework.Entity;
import engine.core.framework.World;
import engine.core.imp.SystemFieldInitializer;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.physics.Vector2f;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.presets.TexturedSolid;
import glextra.renderer.LWJGLRenderer2D;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.display.LWJGLDisplay;
import gltools.input.Keyboard;
import gltools.utils.Timer;

public class PendulumTest {
	private World m_world = new World();
	private PhysicsManager m_physics = new PhysicsManager();

	public PendulumTest() {
		startTest();
	}

	private void startTest() {
		LWJGLDisplay display = makeDisplay("Framework Test");
		Keyboard keyboard = getKeyboard(display);

		LWJGLRenderer2D renderer = LWJGLRenderer2D.getInstance();
		renderer.init(-5f, 5f, 5f, -5f);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(m_physics);

		// make the ground
		Entity ground = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f),
				BodyType.KINEMATIC, "Textures/metalplate.jpg");
		m_world.addEntity(ground);

		// make the pendulum
		// Entity pendulum = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(0.1f, 1f), 0, new
		// Vector2f(0.2f,
		// 2f), BodyType.DYNAMIC, "Textures/spaceship.png", "Textures/spaceship_n.png", new Vector3f(2f, 0f, 10f));
		TexturedSolid pendulum = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(0.1f, 1f), 0,
				new Vector2f(0.2f, 2f), BodyType.DYNAMIC, "Textures/pendulum.png");
		m_world.addEntity(pendulum);

		// create the joint
		RevoluteJointDef revoluteDef = PhysicsFactory.makeRevoluteDef(m_physics.getBody(ground),
				m_physics.getBody(pendulum), new Vector2f(0f, 0.5f), new Vector2f(0, -1f), false);
		m_physics.createJoint(revoluteDef);

		Timer timer = new Timer();
		while (!keyboard.isKeyPressed(keyboard.getKey("ESCAPE")) && !display.closeRequested()) {
			timer.mark();

			keyboard.poll();
			renderer.clear();
			renderer.startGeometry();
			balancePendulum(ground, pendulum);
			movePendulum(pendulum, keyboard);
			m_world.update(0.0166666666666666f);
			renderer.setMaterial(MaterialFactory.createBasicMaterial());
			renderer.fillRect(0f, 2f, 0.1f, 0.1f);
			renderer.finishGeometry();
			display.update(60);

			float fps = 1 / timer.getDeltaSeconds();
			if (fps != Float.POSITIVE_INFINITY)
				System.out.println("FPS: " + fps);
			System.out.println("-------------------------------------------------");
		}
	}

	private void balancePendulum(Entity ground, Entity pendulum) {
		Body gBody = m_physics.getBody(ground);
		Body pBody = m_physics.getBody(pendulum);

		// desired angular acceleration
		float thetaDotDot = -pBody.getAngle() * 3f - pBody.getAngularVelocity() * 1f + pBody.getLinearVelocity().x
				* 0.05f;
		float xDotDot = calcAcceleration(gBody, pBody, 1f, thetaDotDot);
		System.out.println("Theta - " + pBody.getAngle() + " : Theta dot - " + pBody.getAngularVelocity()
				+ " : Theta dot dot - " + thetaDotDot + " : X dot dot" + xDotDot + " : x Dot "
				+ gBody.getLinearVelocity().x);

		gBody.setLinearVelocity(new Vec2(gBody.getLinearVelocity().x + xDotDot * (1f / 60f), 0));
	}

	private float calcAcceleration(Body ground, Body pendulum, float pendulumLen, float thetaDotDot) {
		float g = 10f;
		float cosTheta = (float) Math.cos(pendulum.getAngle());
		float sinTheta = (float) Math.sin(pendulum.getAngle());
		return (thetaDotDot * pendulumLen - g * sinTheta) / cosTheta;
	}

	private void movePendulum(Entity pendulum, Keyboard keyboard) {
		Body pBody = m_physics.getBody(pendulum);
		if (keyboard.isKeyPressed(keyboard.getKey("LEFT"))) {
			pBody.applyAngularImpulse(0.01f);
		}

		if (keyboard.isKeyPressed(keyboard.getKey("RIGHT")))
			pBody.applyAngularImpulse(-0.01f);
	}

	private static LWJGLDisplay makeDisplay(String title) {
		LWJGLDisplay display = new LWJGLDisplay(1024, 1024, true);
		display.setTitle(title);
		display.init();
		return display;
	}

	private static Keyboard getKeyboard(LWJGLDisplay display) {
		ResourceLocator locator = new ClasspathResourceLocator();

		Keyboard keyboard = display.getKeyboard();

		try {
			keyboard.readXMLKeyConfig("Config/Keyboard/lwjgl.xml", locator);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		return keyboard;
	}

	public static void main(String[] main) {
		new PendulumTest();
	}
}
