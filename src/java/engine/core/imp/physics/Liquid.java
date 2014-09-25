package engine.core.imp.physics;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

public class Liquid {
	private List<Body> m_particles;
	private float m_particleSize;

	public Liquid(List<Body> particles, float particleSize) {
		m_particles = particles;
		m_particleSize = particleSize;
	}

	public List<Body> getParticles() {
		return m_particles;
	}

	public float getParticleSize() {
		return m_particleSize;
	}

	public void applyForces(float time) {
		for (Body p1 : m_particles) {
			for (Body p2 : m_particles) {
				if (p1 != p2) {
					Vec2 dist = p1.getPosition().sub(p2.getPosition());
					if (dist.length() < m_particleSize * 10) {
						p1.applyForceToCenter(dist.mul(0.1f / dist.lengthSquared()).mul(1.5f * p1.getMass()));
						// p1.applyForceToCenter(dist.mul(0.000001f / dist.length()));
					}
				}
			}
		}
	}
}
