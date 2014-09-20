package test;

import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.Joint;
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

public class FrameworkTest {
	private World m_world = new World();
	private PhysicsManager m_physics = new PhysicsManager();

	public FrameworkTest() {
		LWJGLDisplay display = makeDisplay("Framework Test");
		Keyboard keyboard = getKeyboard(display);

		LWJGLRenderer2D renderer = LWJGLRenderer2D.getInstance();
		renderer.init(-10f, 10f, 10f, -10f);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(m_physics);

		Entity ground = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(0f, 0f), 2f,
				new Vector2f(10f, 1f), BodyType.STATIC, "Textures/marble.jpg");
		m_world.addEntity(ground);

		Entity falling1 = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(1.0f, 10f), 0, new Vector2f(2f,
				1f), BodyType.DYNAMIC, "Textures/orange.jpg");
		m_world.addEntity(falling1);

		Entity falling2 = new TexturedSolid(m_world, m_physics, renderer, new Vector2f(3.0f, 10f), 0, new Vector2f(4f,
				0.5f), BodyType.DYNAMIC, "Textures/orange.jpg");
		m_world.addEntity(falling2);

		RevoluteJointDef revoluteDef = PhysicsFactory.makeRevoluteDef(m_physics.getBody(falling1),
				m_physics.getBody(falling2), new Vector2f(1f, 0.5f), new Vector2f(0, 0), false);
		Joint joint = m_physics.createJoint(revoluteDef);

		Timer timer = new Timer();
		while (!keyboard.isKeyPressed(keyboard.getKey("ESCAPE")) && !display.closeRequested()) {
			timer.mark();
			renderer.clear();
			renderer.startGeometry();
			m_world.update(1);
			renderer.finishGeometry();
			display.update(60);

			float fps = 1 / timer.getDeltaSeconds();
			if (fps != Float.POSITIVE_INFINITY)
				System.out.println("FPS: " + fps);
			System.out.println("-------------------------------------------------");
		}
	}

	public static LWJGLDisplay makeDisplay(String title) {
		LWJGLDisplay display = new LWJGLDisplay(1024, 1024, true);
		display.setTitle(title);
		display.init();
		return display;
	}

	public static Keyboard getKeyboard(LWJGLDisplay display) {
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
		new FrameworkTest();
	}
}
