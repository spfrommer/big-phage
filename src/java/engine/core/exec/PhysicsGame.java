package engine.core.exec;

import engine.core.frame.World;
import engine.core.imp.SystemFieldInitializer;
import engine.core.imp.physics.PhysicsManager;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Light.PointLight;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.display.LWJGLDisplay;
import gltools.input.Keyboard;
import gltools.input.Mouse;
import gltools.util.Timer;

/**
 * A basic game with physics and rendering.
 */
public abstract class PhysicsGame {
	private World m_world;
	private PhysicsManager m_physics;
	private PhysicsGameFactory m_factory;

	private LWJGLDisplay m_display;
	private LWJGLRenderer2D m_renderer;
	private Keyboard m_keyboard;
	private Mouse m_mouse;

	private String m_title;
	private int m_fps;

	private GameState m_state = new GameState();

	public PhysicsGame(String title) {
		m_world = new World();
		m_physics = new PhysicsManager();
		m_title = title;
		m_fps = 60;
	}

	public void start() {
		makeDisplay(m_title);
		readDevices(m_display);

		m_renderer = LWJGLRenderer2D.getInstance();
		m_renderer.init(m_display.getWidth(), m_display.getHeight(), -5f, 5f, 5f, -5f);

		m_factory = new PhysicsGameFactory(m_world, m_physics);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(m_physics);

		PointLight.init();
		m_state.renderer = m_renderer;
		m_state.keyboard = m_keyboard;
		m_state.mouse = m_mouse;
		onStart();

		Timer timer = new Timer();
		while (!m_keyboard.isKeyPressed(m_keyboard.getKey("ESCAPE")) && !m_display.closeRequested()) {
			timer.mark();

			m_keyboard.poll();
			m_mouse.poll();
			m_renderer.clear();
			m_renderer.startLighted();
			m_renderer.startGeometry();

			preUpdate();
			m_world.update(0.0166666666666666f, m_state);
			postUpdate();

			m_renderer.finishGeometry();
			m_renderer.finishLighted();
			m_renderer.doLightingComputations();
			m_display.update(m_fps);
		}
	}

	public abstract void onStart();

	public abstract void preUpdate();

	public abstract void postUpdate();

	public void setFPS(int fps) {
		m_fps = fps;
	}

	public World getWorld() {
		return m_world;
	}

	public Keyboard getKeyboard() {
		return m_keyboard;
	}

	public Mouse getMouse() {
		return m_mouse;
	}

	public PhysicsGameFactory getGameFactory() {
		return m_factory;
	}

	public PhysicsManager getPhysicsManager() {
		return m_physics;
	}

	private void makeDisplay(String title) {
		m_display = new LWJGLDisplay(1024, 1024, true);
		m_display.setTitle(title);
		m_display.init();
	}

	private void readDevices(LWJGLDisplay display) {
		ResourceLocator locator = new ClasspathResourceLocator();

		Mouse mouse = display.getMouse();
		Keyboard keyboard = display.getKeyboard();

		try {
			keyboard.readXMLKeyConfig("Config/Keyboard/lwjgl.xml", locator);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		m_keyboard = keyboard;
		m_mouse = mouse;
	}

}
