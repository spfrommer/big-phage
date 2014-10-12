package engine.core.exec;

import engine.core.frame.World;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Light.PointLight;
import gltools.ResourceLocator;
import gltools.ResourceLocator.ClasspathResourceLocator;
import gltools.display.Display;
import gltools.display.LWJGLDisplay;
import gltools.input.Keyboard;
import gltools.input.Mouse;

public abstract class Game {
	private World m_current;

	private Display m_display;
	private GameState m_state = new GameState();

	private String m_title;
	private int m_fps;

	public Game(String title) {
		m_title = title;
		m_fps = 60;
	}

	public void start() {
		m_display = makeDisplay(m_title);
		m_state.renderer = LWJGLRenderer2D.getInstance();
		m_state.renderer.init(m_display.getWidth(), m_display.getHeight(), -8.5f, 8.5f, 5f, -5f);
		readDevices(m_display, m_state);

		PointLight.init();
		init();
		createMaterials();
		onStart();

		while (!m_state.keyboard.isKeyPressed(m_state.keyboard.getKey("ESCAPE")) && !m_display.closeRequested()) {
			m_state.keyboard.poll();
			m_state.mouse.poll();
			m_state.renderer.clear();
			m_state.renderer.startLighted();
			m_state.renderer.startGeometry();

			preUpdate();
			m_current.update(1 / (float) m_fps, m_state);
			postUpdate();

			m_state.renderer.finishGeometry();
			m_state.renderer.finishLighted();
			m_state.renderer.doLightingComputations();
			m_display.update(m_fps);
		}
	}

	protected abstract void init();

	public abstract void createMaterials();

	public abstract void onStart();

	protected abstract void preUpdate();

	protected abstract void postUpdate();

	public World getCurrentWorld() {
		return m_current;
	}

	public void setCurrentWorld(World world) {
		m_current = world;
	}

	public GameState getGameState() {
		return m_state;
	}

	public void setFPS(int fps) {
		m_fps = fps;
	}

	private Display makeDisplay(String title) {
		LWJGLDisplay display = new LWJGLDisplay(1200, 700, true);
		display.setTitle(title);
		display.init();
		return display;
	}

	private void readDevices(Display display, GameState state) {
		ResourceLocator locator = new ClasspathResourceLocator();

		Mouse mouse = display.getMouse();
		Keyboard keyboard = display.getKeyboard();

		try {
			keyboard.readXMLKeyConfig("Config/Keyboard/lwjgl.xml", locator);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		state.keyboard = keyboard;
		state.mouse = mouse;
	}
}
