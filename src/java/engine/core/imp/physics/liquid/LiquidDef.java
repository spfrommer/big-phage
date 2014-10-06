package engine.core.imp.physics.liquid;

import java.util.ArrayList;
import java.util.List;

import engine.commons.utils.Vector2f;

public class LiquidDef {
	private List<Vector2f> m_particlePositions;
	private float m_particleRadius;
	private float m_density;
	private Vector2f m_origin;
	private float m_maxDist;

	public LiquidDef() {
		m_particlePositions = new ArrayList<Vector2f>();
		m_particleRadius = 0.1f;
		m_density = 0.1f;
		m_origin = new Vector2f(0f, 0f);
		m_maxDist = 10f;
	}

	public LiquidDef(List<Vector2f> particlePositions, float particleSize, float density, Vector2f origin, float maxDist) {
		m_particlePositions = particlePositions;
		m_particleRadius = particleSize;
		m_density = density;
		m_origin = origin;
		m_maxDist = maxDist;
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

	public float getDensity() {
		return m_density;
	}

	public void setDensity(float density) {
		m_density = density;
	}

	public Vector2f getOrigin() {
		return m_origin;
	}

	public void setOrigin(Vector2f origin) {
		m_origin = origin;
	}

	public float getMaxDist() {
		return m_maxDist;
	}

	public void setMaxDist(float maxDist) {
		m_maxDist = maxDist;
	}
}
