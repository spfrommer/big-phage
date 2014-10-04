package engine.core.imp.physics.liquid;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.commons.utils.Vector2f;

public class Liquid {
	private GridManager m_manager;
	private List<Body> m_particles;
	private float m_particleRadius;
	private float m_density;

	public Liquid(List<Body> particles, float particleRadius, float density) {
		m_particles = particles;
		m_particleRadius = particleRadius;
		m_manager = new GridManager(new Vector2f(particleRadius * 20, particleRadius * 20));

		for (Body b : m_particles) {
			m_manager.getGrid(new Vector2f(b.getPosition())).addParticle(b);
		}

		m_density = density;
	}

	public List<Body> getParticles() {
		return m_particles;
	}

	public void addParticle(Body particle) {
		m_manager.getGrid(new Vector2f(particle.getPosition())).addParticle(particle);
		m_particles.add(particle);
	}

	public float getParticleRadius() {
		return m_particleRadius;
	}

	public float getDensity() {
		return m_density;
	}

	public void applyForces(float time) {
		List<Grid> grids = m_manager.getGrids();

		for (int i = 0; i < grids.size(); i++) {
			Grid g = grids.get(i);
			List<Body> particles = g.getParticles();

			if (particles.size() == 0) {
				m_manager.removeGrid(g);
				i--;
				continue;
			}

			/*int x = g.getX();
			int y = g.getY();

			List<Body> surroundingParticles = new ArrayList<Body>();
			surroundingParticles.addAll(particles);
			surroundingParticles.addAll(m_manager.getGrid(x - 1, y - 1).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x, y - 1).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x + 1, y - 1).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x - 1, y).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x - 1, y + 1).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x, y + 1).getParticles());
			surroundingParticles.addAll(m_manager.getGrid(x + 1, y + 1).getParticles());*/

			for (int j = 0; j < particles.size(); j++) {
				Body p1 = particles.get(j);
				if (!g.isInGrid(p1)) {
					g.removeParticle(p1);
					Grid newGrid = m_manager.getGrid(new Vector2f(p1.getPosition()));
					newGrid.addParticle(p1);
					j--;
				}

				for (Body p2 : particles) {
					if (p1 != p2) {
						Vec2 dist = p1.getPosition().sub(p2.getPosition());
						if (dist.length() < m_particleRadius * 10) {
							p1.applyForceToCenter(dist.mul((10f * p1.getMass() * p2.getMass()) / dist.lengthSquared()));
						}
					}
				}
			}
		}
	}
}
