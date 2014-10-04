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

	public FountainComponent(Liquid liquid, PhysicsManager physics) {
		m_liquid = liquid;
		m_physics = physics;
	}

	@Override
	public void update(float time) {
		Vector2f position = (Vector2f) getData("sys_fountainPosition");

		BodyDef bodyDef = PhysicsFactory.makeBodyDef(position, BodyType.DYNAMIC, 0f, PhysicsConstants.LIQUID_DAMPENING);
		Body body = m_physics.createBody(bodyDef);
		FixtureDef fix = PhysicsFactory.makeCircularFixtureDef(m_liquid.getParticleRadius(), m_liquid.getDensity(),
				PhysicsConstants.LIQUID_FRICTION, PhysicsConstants.LIQUID_RESTITUTION);
		body.createFixture(fix);

		body.setLinearVelocity(new Vec2(((float) Math.random() - 0.5f) * 2f, ((float) Math.random() - 0.5f) * 2f));

		m_liquid.addParticle(body);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
