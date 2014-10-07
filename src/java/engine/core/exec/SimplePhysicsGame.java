package engine.core.exec;

import engine.core.frame.World;
import engine.core.imp.SystemFieldInitializer;
import engine.core.imp.physics.PhysicsManager;
import engine.core.presets.PhysicsGameFactory;

/**
 * A basic game with physics and only one world.
 */
public abstract class SimplePhysicsGame extends Game {
	private World m_world;
	private PhysicsManager m_physics;
	private PhysicsGameFactory m_factory;

	public SimplePhysicsGame(String title) {
		super(title);
		m_world = new World();
		m_physics = new PhysicsManager();
	}

	@Override
	public void init() {
		m_factory = new PhysicsGameFactory(m_world, m_physics);

		m_world.addFieldInitializer(new SystemFieldInitializer());
		m_world.addDataManager(m_physics);
		setCurrentWorld(m_world);
	}

	public World getWorld() {
		return m_world;
	}

	public PhysicsGameFactory getGameFactory() {
		return m_factory;
	}

	public PhysicsManager getPhysicsManager() {
		return m_physics;
	}
}
