package engine.core.exec;

import engine.core.frame.World;
import engine.core.imp.render.MaterialFactory;
import glcommon.util.ResourceLocator;
import glcommon.util.ResourceLocator.ClasspathResourceLocator;
import glextra.renderer.LWJGLRenderer2D;
import glextra.renderer.Light.PointLight;
import gltools.display.Window;
import gltools.gl.lwjgl.glfw.GLFWWindow;
import gltools.input.Keyboard;
import gltools.input.Mouse;

public abstract class Game {
	private World m_current;

	private GameState m_state = new GameState();

	private String m_title;
	private int m_fps;

	private int m_displayWidth;
	private int m_displayHeight;
	private float m_pixelsPerMeter;

	private boolean m_autoStep = true;

	public Game(String title, int displayWidth, int displayHeight, float pixelsPerMeter) {
		m_title = title;
		m_fps = 60;
		m_displayWidth = displayWidth;
		m_displayHeight = displayHeight;
		m_pixelsPerMeter = pixelsPerMeter;
	}

	public void start() {
		m_state.display = makeDisplay(m_title);
		m_state.renderer = LWJGLRenderer2D.getInstance();
		float widthMeters = m_displayWidth / (m_pixelsPerMeter * 2);
		float heightMeters = m_displayHeight / (m_pixelsPerMeter * 2);
		m_state.renderer.init(widthMeters, widthMeters,
				heightMeters, heightMeters, m_state.display);
		readDevices(m_state.display, m_state);

		PointLight.init(m_state.renderer.getGL());
		init();
		createMaterials(new MaterialFactory(m_state.renderer));
		onStart();

		if (m_autoStep) {
			while (!m_state.keyboard.isKeyPressed(m_state.keyboard.getKey("ESCAPE"))
					&& !m_state.display.closeRequested()) {
				doStep();
			}
		}
	}

	public void doStep() {
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
		m_state.display.update();
	}

	public void close() {
		m_state.display.destroy();
		System.exit(0);
	}

	protected abstract void init();

	public abstract void createMaterials(MaterialFactory factory);

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

	/**
	 * Sets whether or not the simulation should automatically update itself or whether updates are done manually with
	 * the doStep() call.
	 * 
	 * @param autoStep
	 */
	public void setAutoStep(boolean autoStep) {
		m_autoStep = autoStep;
	}

	private Window makeDisplay(String title) {
		GLFWWindow display = new GLFWWindow(m_displayWidth, m_displayHeight);
		display.setTitle(title);
		display.init();
		return display;
	}

	private void readDevices(Window display, GameState state) {
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
