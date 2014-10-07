package engine.core.imp.physics.liquid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.PhysicsManager;

/**
 * A component that adds particles to a Liquid.
 */
public class FountainComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_fountainPosition"));
	private Liquid m_liquid;
	private PhysicsManager m_physics;
	private float m_spawnTime = 1f / 60f;
	private float m_curTime = 0f;

	public FountainComponent(Liquid liquid, PhysicsManager physics) {
		m_liquid = liquid;
		m_physics = physics;
	}

	@Override
	public void update(float time, GameState state) {
		Vector2f position = (Vector2f) getData("sys_fountainPosition");

		m_curTime += time;

		while (m_curTime >= m_spawnTime) {
			BodyDef bodyDef = PhysicsFactory.makeBodyDef(position, BodyType.DYNAMIC, 0f,
					PhysicsConstants.LIQUID_DAMPENING);
			Body body = m_physics.createBody(bodyDef);
			FixtureDef fix = PhysicsFactory.makeCircularFixtureDef(m_liquid.getParticleRadius(), m_liquid.getDensity(),
					PhysicsConstants.LIQUID_FRICTION, PhysicsConstants.LIQUID_RESTITUTION);
			body.createFixture(fix);

			body.setLinearVelocity(new Vec2(((float) Math.random() - 0.5f) * 0.1f,
					((float) Math.random() - 0.5f) * 0.1f));

			m_liquid.addParticle(body);

			m_curTime -= m_spawnTime;
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
