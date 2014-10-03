package engine.core.imp.physics.liquid;

import java.util.ArrayList;
import java.util.List;

import engine.commons.utils.Vector2f;

public class LiquidDef {
	private List<Vector2f> m_particlePositions;
	private float m_particleRadius;

	public LiquidDef() {
		m_particlePositions = new ArrayList<Vector2f>();
		m_particleRadius = 0.1f;
	}

	public LiquidDef(List<Vector2f> particlePositions, float particleSize) {
		m_particlePositions = particlePositions;
		m_particleRadius = particleSize;
	}

	public List<Vector2f> getParticles() {
		return m_particlePositions;
	}

	public void addParticle(Vector2f particle) {
		m_particlePositions.add(particle);
	}

	public float getParticleRadius() {
		return m_particleRadius;
	}

	public void setParticleRadius(float particleRadius) {
		m_particleRadius = particleRadius;
	}
}
