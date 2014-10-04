package engine.core.imp.physics.liquid;

import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.commons.utils.Vector2f;

public class Grid {
	private Vector2f m_bleft;
	private Vector2f m_tright;
	private Vector2f m_dimensions;
	private List<Body> m_particles;

	public Grid(Vector2f position, Vector2f dimensions) {
		m_bleft = position;
		m_tright = new Vector2f(position.x + dimensions.x, position.y + dimensions.y);
		m_particles = new ArrayList<Body>();
		m_dimensions = dimensions;
	}

	public void addParticle(Body particle) {
		m_particles.add(particle);
	}

	public void removeParticle(Body particle) {
		m_particles.remove(particle);
	}

	public boolean isInGrid(Body particle) {
		Vec2 position = particle.getPosition();
		if (m_bleft.x <= position.x && m_tright.x >= position.x && m_bleft.y <= position.y && m_tright.y >= position.y)
			return true;
		return false;
	}

	public boolean isInGrid(Vector2f position) {
		if (m_bleft.x <= position.x && m_tright.x >= position.x && m_bleft.y <= position.y && m_tright.y >= position.y)
			return true;
		return false;
	}

	public int getX() {
		return (int) Math.floor(m_bleft.x - m_dimensions.x);
	}

	public int getY() {
		return (int) Math.floor(m_bleft.y - m_dimensions.y);
	}

	public List<Body> getParticles() {
		return m_particles;
	}
}
