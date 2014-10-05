package test;

import java.awt.geom.Path2D;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.frame.Entity;
import engine.core.frame.World;
import engine.core.imp.SystemFieldInitializer;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.render.lwjgl.LiquidRenderComponent;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.LWJGLRenderer2D;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.display.LWJGLDisplay;
import gltools.input.Key;
import gltools.input.KeyListener;
import gltools.input.Keyboard;
import gltools.texture.Color;
import gltools.utils.Timer;

public class LiquidTest {
	private World m_world = new World();
	private PhysicsManager m_physics = new PhysicsManager();
	private PhysicsGameFactory m_factory;

	public LiquidTest() {
		startTest();
	}

	private void startTest() {
		LWJGLDisplay display = makeDisplay("Framework Test");
		Keyboard keyboard = getKeyboard(display);

		final LWJGLRenderer2D renderer = LWJGLRenderer2D.getInstance();
		renderer.init(display.getWidth(), display.getHeight(), -5f, 5f, 5f, -5f);
		m_factory = new PhysicsGameFactory(m_world, m_physics, renderer);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(m_physics);

		// make the ground
		Entity ground = m_factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		m_world.addEntity(ground);

		// make the walls
		Entity wall1 = m_factory.createTexturedSolid(new Vector2f(1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		m_world.addEntity(wall1);

		Entity wall2 = m_factory.createTexturedSolid(new Vector2f(-1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		m_world.addEntity(wall2);

		keyboard.addListener(new KeyListener() {
			@Override
			public void keyReleased(Keyboard k, Key key) {

			}

			@Override
			public void keyPressed(Keyboard k, Key key) {
				Entity box = m_factory.createTexturedSolid(new Vector2f((float) (Math.random() - 0.5f) * 2f, 5f), 0f,
						new Vector2f(0.3f, 0.5f), BodyType.DYNAMIC,
						MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
				m_world.addEntity(box);
			}
		});

		// make the liquid
		Entity liquid = new Entity(m_world);
		liquid.addComponent(new LiquidRenderComponent(renderer));
		Path2D.Float path = new Path2D.Float();
		float y = 0.5f;
		float x = 0.0f;
		path.moveTo(-1.5f + x, 0f + y);
		path.lineTo(-1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 0f + y);
		// path.lineTo(-0.3f + x, 0.5f + y);
		m_physics.createLiquid(liquid, PhysicsFactory.makeLiquidDef(path, 0.11f, 0.05f));
		liquid.setData("sys_material", MaterialFactory.createBasicMaterial(new Color(0f, 0f, 1f)));
		m_world.addEntity(liquid);

		Timer timer = new Timer();
		while (!keyboard.isKeyPressed(keyboard.getKey("ESCAPE")) && !display.closeRequested()) {
			timer.mark();

			keyboard.poll();
			renderer.clear();
			renderer.startGeometry();
			m_world.update(0.0166666666666666f);
			renderer.finishGeometry();
			display.update(60);

			/*float fps = 1 / timer.getDeltaSeconds();
			if (fps != Float.POSITIVE_INFINITY)
				System.out.println("FPS: " + fps);
			System.out.println("-------------------------------------------------");*/
		}
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

	public static void main(String[] args) {
		new LiquidTest();
	}
}
