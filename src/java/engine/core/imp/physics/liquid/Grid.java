package engine.core.imp.physics.liquid;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.core.imp.physics.Vector2f;

public class Grid {
	private Vector2f m_bleft;
	private Vector2f m_tright;
	private List<Body> m_particles;

	public Grid(Vector2f position, Vector2f dimensions) {
		m_bleft = position;
		m_tright = new Vector2f(position.x + dimensions.x, position.y + dimensions.y);
	}

	public void addParticle(Body particle) {
		m_particles.add(particle);
	}

	public void removeParticle(Body particle) {
		m_particles.remove(particle);
	}

	public boolean isInGrid(Body particle) {
		Vec2 position = particle.getPosition();
		if (m_bleft.x < position.x && m_tright.x >= position.x && m_bleft.y < position.y && m_tright.y >= position.y)
			return true;
		return false;
	}

	public List<Body> getParticles() {
		return m_particles;
	}
}
