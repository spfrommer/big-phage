package test;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import engine.core.framework.Entity;
import engine.core.framework.World;
import engine.core.imp.SystemFieldInitializer;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.render.lwjgl.RenderComponent;
import glextra.renderer.LWJGLRenderer2D;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.display.LWJGLDisplay;
import gltools.input.Keyboard;
import gltools.utils.Timer;

public class FrameworkTest {
	private World m_world = new World();
	private PhysicsManager physics = new PhysicsManager();

	public FrameworkTest() {
		LWJGLDisplay display = makeDisplay("Framework Test");
		Keyboard keyboard = getKeyboard(display);

		LWJGLRenderer2D renderer = LWJGLRenderer2D.getInstance();
		renderer.init(-100f, 100f, 100f, -100f);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(physics);

		Entity entity = new Entity(m_world);
		entity.addComponent(new RenderComponent(renderer));

		Body body = physics.setBody(entity, makeBodyDef(0, 0, BodyType.DYNAMIC));
		body.createFixture(makeRectangularFixtureDef(0, 0, 5, 5, 0));

		m_world.addEntity(entity);

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
		/*
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
		}*/
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

	/*private void testRendering() {
		ResourceLocator locator = new ClasspathResourceLocator();

		LWJGLDisplay display = new LWJGLDisplay(1024, 1024, true);
		display.setTitle("Renderer2DTest");
		display.init();

		Keyboard keyboard = display.getKeyboard();

		try {
			keyboard.readXMLKeyConfig("Config/Keyboard/lwjgl.xml", locator);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		Material mat = null;
		try {
			mat = MaterialXMLLoader.s_load("Materials/2d.mat", locator).get(0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShaderCompileException e) {
			e.printStackTrace();
		} catch (ProgramLinkException e) {
			e.printStackTrace();
		}

		mat.setColor("materialDiffuseColor", new Color(1, 0, 0));
		try {
			mat.setTexture2D("materialDiffuseTexture", TextureFactory.s_loadTexture("Textures/marble.jpg", locator));
		} catch (IOException e) {
			e.printStackTrace();
		}

		LWJGLRenderer2D renderer = LWJGLRenderer2D.getInstance();
		renderer.init(0, 1024f, 1024f, 0f);

		renderer.setMaterial(mat);

		while (!keyboard.isKeyPressed(keyboard.getKey("ESCAPE")) && !display.closeRequested()) {
			renderer.startGeometry();
			// renderer.rotate((float) (Math.PI * 0.5));
			renderer.translate(100, 100);
			renderer.pushModel();
			renderer.translate(50, 50);
			renderer.rotate((float) (Math.PI * 0.25));
			renderer.translate(-50, -50);
			renderer.fillRect(0, 0, 100, 100);
			renderer.popModel();
			renderer.pushModel();
			renderer.fillRect(0, 0, 100, 100);
			renderer.popModel();
			renderer.finishGeometry();
			display.update(60);
		}
	}*/

	public static void main(String[] main) {
		new FrameworkTest();
	}
}
