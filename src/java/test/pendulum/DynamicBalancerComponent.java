package test.pendulum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.frame.Entity;

public class DynamicBalancerComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body", "pen_pendulum"));

	@Override
	public void update(float time, GameState state) {
		Body body = (Body) getData("sys_body");

		Entity pendulum = (Entity) getData("pen_pendulum");
		Body pBody = (Body) pendulum.getData("sys_body");
		float penlength = (Float) pendulum.getData("pen_penlength");

		balancePendulum(body, pBody, penlength, time);
	}

	private float angularIntegral = 0;
	private boolean firstTime = true;

	private void balancePendulum(Body gBody, Body pBody, float penlength, float time) {
		if (firstTime) {
			gBody.setLinearVelocity(new Vec2(0f, 0f));
			firstTime = false;
		}
		// desired angular acceleration
		float thetaDotDot = -pBody.getAngle() * 3f - pBody.getAngularVelocity() * 1.5f + pBody.getLinearVelocity().x
				* 0.08f;
		float xDotDot = calcAcceleration(gBody, pBody, penlength, thetaDotDot);

		gBody.applyForceToCenter(new Vec2((gBody.getMass() + pBody.getMass()) * xDotDot, (gBody.getMass() + pBody
				.getMass()) * 10f));

		// torque the ground
		float angle = gBody.getAngle();
		float angularV = gBody.getAngularVelocity();
		angularIntegral += angle;

		float controller = -angle * 10f - angularV * 2f - angularIntegral * 0f;
		gBody.applyTorque(controller);
	}

	private float calcAcceleration(Body ground, Body pendulum, float pendulumLen, float thetaDotDot) {
		float g = 10f;
		float cosTheta = (float) Math.cos(pendulum.getAngle());
		float sinTheta = (float) Math.sin(pendulum.getAngle());
		return (thetaDotDot * pendulumLen - g * sinTheta) / cosTheta;
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
