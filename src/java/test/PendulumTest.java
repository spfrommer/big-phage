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
		Entity pendulum = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(0.1f, 1f), 0, new Vector2f(0.2f,
				2f), BodyType.DYNAMIC, "Textures/pendulum.png");
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
			renderer.finishGeometry();
			display.update(60);

			float fps = 1 / timer.getDeltaSeconds();
			if (fps != Float.POSITIVE_INFINITY)
				System.out.println("FPS: " + fps);
			System.out.println("-------------------------------------------------");
		}
	}

	// private float ierror = 0;
	private float lastperror = 0;

	private void balancePendulum(Entity ground, Entity pendulum) {
		Body gBody = m_physics.getBody(ground);
		Body pBody = m_physics.getBody(pendulum);

		/*float perror = pBody.getAngle();
		float derror = pBody.getAngularVelocity();
		ierror += perror;
		float correction = -perror * 30f + -ierror * 10f + (-derror * 1f);*/
		float perror = pBody.getLinearVelocity().x;
		float ierror = pBody.getPosition().x;
		float derror = (perror - lastperror) * 60f /*delta time*/;
		float correction = perror * 1.0f + ierror * 1.0f + derror * 0.5f;

		lastperror = perror;

		System.out.println("Proportional: " + perror);
		System.out.println("Derivative: " + derror);
		System.out.println("Integral: " + ierror);

		gBody.setLinearVelocity(new Vec2(correction, 0));
	}

	private void movePendulum(Entity pendulum, Keyboard keyboard) {
		Body pBody = m_physics.getBody(pendulum);
		if (keyboard.isKeyPressed(keyboard.getKey("LEFT"))) {
			pBody.applyAngularImpulse(0.2f);
		}

		if (keyboard.isKeyPressed(keyboard.getKey("RIGHT")))
			pBody.applyAngularImpulse(-0.2f);
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
