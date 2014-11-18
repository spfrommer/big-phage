package test.robot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.imp.physics.collision.CompleteListener;

/**
 * Implements the algorithm described in Compliant Terrain Adaptation for Biped Humanoids Without Measuring Ground
 * Surface and Contact Forces.
 */
public class RobotControllerComponent extends Component implements CompleteListener {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("rob_leg1", "rob_leg2",
			"rob_connector", "rob_body", "rob_legJoint1", "rob_legJoint2", "rob_bodyJoint"));

	private boolean m_updatedLeft;
	private boolean m_updatedRight;
	private float m_lastTime;
	private MVector m_leftForce;
	private MVector m_rightForce;

	private static final float PROPORTIONAL_GAIN = 0.1f;
	private static final float DERIVATIVE_GAIN = 0.1f;
	private MVector m_lastCOM;

	private static final float GRAVITY = 10f;

	@Override
	public void update(float time, GameState state) {
		m_updatedLeft = false;
		m_updatedRight = false;
		m_lastTime = time;

		float legLenHalf = 0.5f;

		Body leg1 = (Body) getData("rob_leg1");
		MVector leg1Pos = new MVector(leg1.getPosition().x, leg1.getPosition().y);
		float leg1Mass = leg1.getMass();

		Body leg2 = (Body) getData("rob_leg2");
		MVector leg2Pos = new MVector(leg2.getPosition().x, leg2.getPosition().y);
		float leg2Mass = leg2.getMass();

		Body connector = (Body) getData("rob_connector");
		MVector connectorPos = new MVector(connector.getPosition().x, connector.getPosition().y);
		float connectorMass = connector.getMass();

		Body body = (Body) getData("rob_body");
		MVector bodyPos = new MVector(body.getPosition().x, body.getPosition().y);
		float bodyMass = body.getMass();

		RevoluteJoint legJoint1 = (RevoluteJoint) getData("rob_legJoint1");
		RevoluteJoint legJoint2 = (RevoluteJoint) getData("rob_legJoint2");
		Joint bodyJoint = (Joint) getData("rob_bodyJoint");

		float totalMass = leg1Mass + leg2Mass + connectorMass + bodyMass;
		Matrix posSum = leg1Pos.scalarMultiply(leg1Mass).add(leg2Pos.scalarMultiply(leg2Mass))
				.add(connectorPos.scalarMultiply(connectorMass)).add(bodyPos.scalarMultiply(bodyMass));
		MVector com = posSum.scalarMultiply(1 / totalMass).toVector();

		// PI / 2 factor is because the rotation starts at north and goes counter clockwise
		float contact1x = (float) Math.cos(leg1.getAngle() + Math.PI / 2) * legLenHalf;
		float contact1y = (float) Math.sin(leg1.getAngle() + Math.PI / 2) * legLenHalf;
		MVector contact1 = leg1Pos.add(new MVector(contact1x, contact1y)).subtract(com).toVector();

		float contact2x = (float) Math.cos(leg2.getAngle() + Math.PI / 2) * legLenHalf;
		float contact2y = (float) Math.sin(leg2.getAngle() + Math.PI / 2) * legLenHalf;
		MVector contact2 = leg2Pos.add(new MVector(contact2x, contact2y)).subtract(com).toVector();

		float joint1x = (float) Math.cos(leg1.getAngle() - Math.PI / 2) * legLenHalf;
		float joint1y = (float) Math.sin(leg1.getAngle() - Math.PI / 2) * legLenHalf;
		MVector joint1 = leg1Pos.add(new MVector(joint1x, joint1y)).subtract(com).toVector();

		float joint2x = (float) Math.cos(leg2.getAngle() - Math.PI / 2) * legLenHalf;
		float joint2y = (float) Math.sin(leg2.getAngle() - Math.PI / 2) * legLenHalf;
		MVector joint2 = leg2Pos.add(new MVector(joint2x, joint2y)).subtract(com).toVector();

		// System.out.println("Left leg: " + leg1Pos + " : " + leg1Mass);
		// System.out.println("Right leg: " + leg2Pos + " : " + leg2Mass);
		// System.out.println("Connector: " + connectorPos + " : " + connectorMass);
		// System.out.println("Body: " + bodyPos + " : " + bodyMass);

		if (m_lastCOM == null) {
			m_lastCOM = com;
		} else if (m_leftForce != null && m_rightForce != null) {
			MVector gaf = m_leftForce.add(m_rightForce).toVector();
			implementAlgorithms(Arrays.asList(leg1, leg2, connector, body), Arrays.asList(legJoint1, legJoint2),
					Arrays.asList(joint1, joint2), Arrays.asList(contact1, contact2),
					Arrays.asList(m_leftForce, m_rightForce), com, gaf, totalMass, time);

			/*System.out.println("Ground applied force: " + gaf);

			System.out.println("Total mass: " + totalMass);
			System.out.println("COM: " + com);

			System.out.println("Left contact position: " + contact1);
			System.out.println("Right contact position: " + contact2);

			System.out.println("Left leg GRF: " + m_leftForce);
			System.out.println("Right leg GRF: " + m_rightForce);*/

			System.out.println("---------------------------------------------------------");
		}
	}

	private void implementAlgorithms(List<Body> bodies, List<RevoluteJoint> joints, List<MVector> jointPositions,
			List<MVector> contactPositions, List<MVector> contactForces, MVector com, MVector gaf, float totalMass,
			float time) {
		float xpNumerator = 0;
		float xpDenominator = 0;
		for (int i = 0; i < contactPositions.size(); i++) {
			MVector cPosition = contactPositions.get(i);
			MVector cForce = contactForces.get(i);
			xpNumerator += cPosition.getX() * cForce.getY();
			xpDenominator += cForce.getY();
		}

		// Matrix cop = new Matrix(2, 1, new float[] { xpNumerator / xpDenominator, 1 });
		// Matrix descop = new Matrix(2, 1, new float[] { 0f, 1f });

		MVector desGaF = getDesiredGaF(com, totalMass, time);
		MVector desCoP = getDesiredCoP(com, desGaF, totalMass);

		distributeForces(bodies, joints, jointPositions, contactPositions, com, desCoP, desGaF);
	}

	private MVector getDesiredGaF(MVector com, float totalMass, float time) {
		Matrix comDerivative = com.subtract(m_lastCOM).scalarMultiply(1 / time);

		// since desired com and com derivative are 0 for balancing problem, the equations are simplified to remove
		// these two terms
		Matrix proportional = com.scalarMultiply(-PROPORTIONAL_GAIN);
		Matrix derivative = comDerivative.scalarMultiply(-DERIVATIVE_GAIN);
		Matrix userTaskForce = proportional.add(derivative);

		m_lastCOM = com;

		MVector antigravityForce = new MVector(0, totalMass * GRAVITY);

		return userTaskForce.add(antigravityForce).toVector();
	}

	/**
	 * Returns the desired CoP (in the x direction) relative to the com of the robot.
	 * 
	 * @param com
	 * @param desiredGaF
	 * @param totalMass
	 * @return
	 */
	private MVector getDesiredCoP(MVector com, MVector desiredGaF, float totalMass) {
		float zp = -com.getY();
		float numerator = zp * desiredGaF.getX();
		float denominator = totalMass * GRAVITY + desiredGaF.getY();
		float xp = numerator / denominator;
		return new MVector(xp, zp);
	}

	private void distributeForces(List<Body> bodies, List<RevoluteJoint> joints, List<MVector> jointPositions,
			List<MVector> contactPositions, MVector com, Matrix desCopPos, MVector desCopForce) {

		// the Az matrix - we're really using the y coordinate though because we're in two dimensions
		Matrix Az = stackX(contactPositions).transpose().augment();
		Matrix Aztranspose = Az.transpose();

		Matrix AzTimesTranspose = Az.multiply(Aztranspose);
		Matrix AzTTInvert = AzTimesTranspose.invert();

		// same for both x and z(y) because we're ommiting the "depth" dimension
		Matrix Asharpz = Aztranspose.multiply(AzTTInvert);

		Matrix desContactForcesZ = Asharpz.multiply(desCopPos.scalarMultiply(desCopForce.getY()));
		Matrix desContactForcesX = Asharpz.multiply(desCopPos.scalarMultiply(desCopForce.getX()));

		for (int i = 0; i < joints.size(); i++) {
			RevoluteJoint joint = joints.get(i);
			MVector jPos = jointPositions.get(i);
			MVector cPos = contactPositions.get(i);
			MVector force = new MVector(desContactForcesX.getVal(i, 0), desContactForcesZ.getVal(i, 0));

			MVector r = cPos.subtract(jPos).toVector();

			float torque = r.crossProduct(force);

			System.out.println(torque);
			joint.setMaxMotorTorque(Math.abs(torque));
			joint.setMotorSpeed(-torque * 1000);
			// System.out.println("Joint position: \n" + jPos);
			// System.out.println("Contact point position: \n" + cPos);
			// System.out.println("R: \n" + r);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}

		/*System.out.println("Desired CoP position: \n" + desCopPos);
		System.out.println("Desired CoP force: \n" + desCopForce);

		System.out.println("Vertical contact forces: \n" + desContactForcesZ);
		System.out.println("Horizontal contact forces: \n" + desContactForcesX);*/
	}

	private Matrix stackVectors(List<MVector> vectors) {
		float[] values = new float[vectors.size() * 2];
		for (int i = 0; i < vectors.size(); i++) {
			MVector vector = vectors.get(i);
			values[i] = vector.getX();
			values[i + 1] = vector.getY();
		}
		Matrix stack = new Matrix(vectors.size() * 2, 1, values);
		return stack;
	}

	private Matrix stackX(List<MVector> vectors) {
		float[] values = new float[vectors.size()];
		for (int i = 0; i < vectors.size(); i++) {
			MVector vector = vectors.get(i);
			values[i] = vector.getX();
		}
		Matrix stack = new Matrix(vectors.size(), 1, values);
		return stack;
	}

	private Matrix stackY(List<MVector> vectors) {
		float[] values = new float[vectors.size()];
		for (int i = 0; i < vectors.size(); i++) {
			MVector vector = vectors.get(i);
			values[i] = vector.getY();
		}
		Matrix stack = new Matrix(vectors.size(), 1, values);
		return stack;
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public void beginContact(Contact contact) {

	}

	@Override
	public void endContact(Contact contact) {

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		float[] normalImpulses = impulse.normalImpulses;
		float[] tangentImpulses = impulse.tangentImpulses;

		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();

		Body leg1 = (Body) getData("rob_leg1");
		Body leg2 = (Body) getData("rob_leg2");

		if (m_lastTime != 0) {
			if (body1 == leg1 || body2 == leg1) {
				if (!m_updatedLeft) {
					m_leftForce = new MVector(tangentImpulses[0], normalImpulses[0]).scalarMultiply(-1 / m_lastTime)
							.toVector();
				}
				m_updatedLeft = true;
			}
			if (body1 == leg2 || body2 == leg2) {
				if (!m_updatedRight) {
					m_rightForce = new MVector(tangentImpulses[0], normalImpulses[0]).scalarMultiply(-1 / m_lastTime)
							.toVector();
				}
				m_updatedRight = true;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}
}