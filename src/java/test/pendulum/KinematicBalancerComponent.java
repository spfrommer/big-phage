package test.pendulum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.core.frame.Component;
import engine.core.frame.Entity;

/**
 * A balancing component in a Kinematic platform for balancing a Pendulum.
 */
public class KinematicBalancerComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body", "pen_pendulum"));

	@Override
	public void update(float time) {
		Body body = (Body) getData("sys_body");

		Entity pendulum = (Entity) getData("pen_pendulum");
		Body pBody = (Body) pendulum.getData("sys_body");
		float penlength = (Float) pendulum.getData("pen_penlength");

		balancePendulum(body, pBody, penlength, time);
	}

	private void balancePendulum(Body gBody, Body pBody, float penlength, float time) {
		// desired angular acceleration
		float thetaDotDot = -pBody.getAngle() * 3f - pBody.getAngularVelocity() * 1.5f + pBody.getLinearVelocity().x
				* 0.08f;
		float xDotDot = calcAcceleration(gBody, pBody, penlength, thetaDotDot);
		/*System.out.println("Theta - " + pBody.getAngle() + " : Theta dot - " + pBody.getAngularVelocity()
				+ " : Theta dot dot - " + thetaDotDot + " : X dot dot" + xDotDot + " : x Dot "
				+ gBody.getLinearVelocity().x);*/

		gBody.setLinearVelocity(new Vec2(gBody.getLinearVelocity().x + xDotDot * time, 0));
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
