package engine.core.imp.physics.liquid;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.commons.utils.Vector2f;
import engine.core.imp.physics.PhysicsManager;

public class Liquid {
	private GridManager m_manager;
	private List<Body> m_particles;
	private float m_particleRadius;
	private float m_density;
	private Vec2 m_origin;
	private float m_maxDistSquared;

	public Liquid(List<Body> particles, float particleRadius, float density, Vector2f origin, float maxDist) {
		m_particles = particles;
		m_particleRadius = particleRadius;
		m_manager = new GridManager(new Vector2f(particleRadius * PhysicsConstants.GRID_PARTICLE_RATIO, particleRadius
				* PhysicsConstants.GRID_PARTICLE_RATIO));

		for (Body b : m_particles) {
			m_manager.getGrid(new Vector2f(b.getPosition())).addParticle(b);
		}

		m_density = density;
		m_origin = new Vec2(origin.x, origin.y);
		m_maxDistSquared = maxDist * maxDist;
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

	public void applyForces(float time, PhysicsManager manager) {
		List<Grid> grids = m_manager.getGrids();

		for (int i = 0; i < grids.size(); i++) {
			Grid g = grids.get(i);
			List<Body> gparticles = g.getParticles();

			if (gparticles.size() == 0) {
				m_manager.removeGrid(g);
				i--;
				continue;
			}

			for (int j = 0; j < gparticles.size(); j++) {
				Body p1 = gparticles.get(j);

				if (p1.getPosition().sub(m_origin).lengthSquared() > m_maxDistSquared) {
					g.removeParticle(p1);
					m_particles.remove(p1);
					manager.destroyBody(p1);
					j--;
					continue;
				}
				if (!g.isInGrid(p1)) {
					g.removeParticle(p1);
					Grid newGrid = m_manager.getGrid(new Vector2f(p1.getPosition()));
					newGrid.addParticle(p1);
					j--;
					continue;
				}

				for (Body p2 : gparticles) {
					if (p1 != p2) {
						Vec2 dist = p1.getPosition().sub(p2.getPosition());
						// if (dist.length() < m_particleRadius * PhysicsConstants.) {
						p1.applyForceToCenter(dist.mul((PhysicsConstants.PARTICLE_FORCE_CONSTANT * p1.getMass())
								/ dist.lengthSquared()));
						// }
					}
				}
			}
		}
	}
}
