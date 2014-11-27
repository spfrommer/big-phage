package test.robot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.WheelJoint;

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

	private static final float PROPORTIONAL_GAIN = 0.5f;
	private static final float INTEGRAL_GAIN = 0.2f;
	private static final float DERIVATIVE_GAIN = 0.5f;
	private MVector m_lastCOM;
	private MVector m_integralCOM = new MVector(0, 0);
	private float m_startingCOMHeight = -1;

	private static final float GRAVITY = 10f;
	private static final float MAX_TORQUE = 1f;
	// the gains for the verticalize joint
	private static final float JOINT_PROPORTIONAL_FACTOR = 20f;
	private static final float JOINT_DERIVATIVE_FACTOR = 2f;

	@Override
	public void update(float time, GameState state) {
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

		Body head = (Body) getData("rob_body");
		MVector headPos = new MVector(head.getPosition().x, head.getPosition().y);
		float bodyMass = head.getMass();

		WheelJoint legJoint1 = (WheelJoint) getData("rob_legJoint1");
		WheelJoint legJoint2 = (WheelJoint) getData("rob_legJoint2");

		float totalMass = leg1Mass + leg2Mass + connectorMass + bodyMass;
		Matrix posSum = leg1Pos.scalarMultiply(leg1Mass).add(leg2Pos.scalarMultiply(leg2Mass))
				.add(connectorPos.scalarMultiply(connectorMass)).add(headPos.scalarMultiply(bodyMass));
		MVector com = posSum.scalarMultiply(1 / totalMass).toVector();
		if (m_startingCOMHeight == -1) {
			m_startingCOMHeight = com.getY();
		}

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

		verticalJoint(legJoint1, leg1);

		if (m_lastCOM == null) {
			m_lastCOM = com;
		} else {
			System.out.println("Total mass: " + totalMass);
			MVector gaf = new MVector(0f, 0f);
			if (m_updatedLeft)
				gaf = gaf.add(m_leftForce).toVector();
			if (m_updatedRight)
				gaf = gaf.add(m_rightForce).toVector();
			List<Body> bodies = Arrays.asList(leg1, leg2, connector, head);
			List<WheelJoint> joints = new ArrayList<WheelJoint>();
			List<MVector> jointPositions = new ArrayList<MVector>();
			List<MVector> contactPositions = new ArrayList<MVector>();
			List<MVector> contactForces = new ArrayList<MVector>();

			if (m_updatedLeft) {
				System.out.println("Updated left");
				joints.add(legJoint1);
				jointPositions.add(joint1);
				contactPositions.add(contact1);
				contactForces.add(m_leftForce);
			} else {
				verticalJoint(legJoint1, leg1);
			}
			if (m_updatedRight) {
				System.out.println("Updated right");
				joints.add(legJoint2);
				jointPositions.add(joint2);
				contactPositions.add(contact2);
				contactForces.add(m_rightForce);
			} else {
				verticalJoint(legJoint2, leg2);
			}

			implementAlgorithms(bodies, joints, jointPositions, contactPositions, contactForces, com, gaf, totalMass,
					time);
		}

		System.out.println("---------------------------------------------------------");

		m_updatedLeft = false;
		m_updatedRight = false;
	}

	private void verticalJoint(WheelJoint joint, Body body) {
		float angle = (float) Math.PI - body.getAngle();
		float twoPI = (float) Math.PI * 2;
		float sign = angle / Math.abs(angle);
		while (Math.abs(angle) > Math.PI) {
			angle -= twoPI * sign;
		}

		float speed = joint.getJointSpeed();

		System.out.println("Dif angle: " + angle);
		System.out.println("Speed: " + speed);

		joint.enableMotor(true);
		joint.setMaxMotorTorque(MAX_TORQUE);
		joint.setMotorSpeed(-angle * JOINT_PROPORTIONAL_FACTOR + speed * JOINT_DERIVATIVE_FACTOR);
	}

	private void implementAlgorithms(List<Body> bodies, List<WheelJoint> joints, List<MVector> jointPositions,
			List<MVector> contactPositions, List<MVector> contactForces, MVector com, MVector gaf, float totalMass,
			float time) {
		MVector antigravityForce = new MVector(0, -totalMass * GRAVITY);
		MVector userTaskForce = getUserTaskForce(com, contactPositions, totalMass, time);
		MVector desGaF = userTaskForce.add(antigravityForce).toVector();
		MVector desCoP = getDesiredCoP(com, userTaskForce, totalMass);

		System.out.println("Des ground applied force: \n" + desGaF);
		System.out.println("Des center of pressure: \n" + desCoP.add(com) + "\n");

		System.out.println("Center of mass: \n" + com);

		if (contactPositions.size() >= 2) {
			distributeForces(bodies, joints, jointPositions, contactPositions, com, desCoP, desGaF);
		} else if (contactPositions.size() == 1) {
			System.out.println("Only one contact position: " + joints.size());
			WheelJoint joint = joints.get(0);
			MVector jPos = jointPositions.get(0);
			MVector cPos = contactPositions.get(0);

			MVector r = cPos.subtract(jPos).toVector();

			float torque = r.crossProduct(desGaF);
			if (torque > 0) {
				torque = Math.min(MAX_TORQUE, torque);
			} else {
				torque = Math.max(-MAX_TORQUE, torque);
			}

			System.out.println("\tTorque: " + torque);

			joint.setMaxMotorTorque(Math.abs(torque));
			joint.setMotorSpeed(-(torque / Math.abs(torque)) * 1000000);
		}
	}

	private MVector getUserTaskForce(MVector com, List<MVector> contactPositions, float totalMass, float time) {
		float sum = 0f;

		for (MVector contact : contactPositions) {
			sum += contact.getX() + com.getX();
			System.out.println("X : " + contact.getX());
		}

		float average = sum / contactPositions.size();

		MVector desCoM;
		if (contactPositions.size() == 0)
			desCoM = com;
		else
			desCoM = new MVector(average, m_startingCOMHeight);

		System.out.println("Des COM:" + desCoM);
		Matrix comDerivative = com.subtract(m_lastCOM).scalarMultiply(1 / time);
		m_integralCOM = m_integralCOM.add(com).toVector();

		// desired com derivative is 0 and des com is starting com for a balancing problem
		Matrix proportional = (com.subtract(desCoM)).scalarMultiply(-PROPORTIONAL_GAIN);
		Matrix derivative = comDerivative.scalarMultiply(-DERIVATIVE_GAIN);
		Matrix integral = comDerivative.scalarMultiply(-INTEGRAL_GAIN);
		// System.out.println("Proportional: \n" + proportional);
		// System.out.println("Derivative: \n" + derivative);
		// System.out.println("Integral: \n" + integral);
		Matrix userTaskForce = proportional.add(derivative).add(integral).scalarMultiply(-1);
		System.out.println("Task force: \n" + userTaskForce);

		m_lastCOM = com;

		return userTaskForce.toVector();
	}

	/**
	 * Returns the desired CoP (in the x direction) relative to the com of the robot.
	 * 
	 * @param com
	 * @param desiredGaF
	 * @param totalMass
	 * @return
	 */
	private MVector getDesiredCoP(MVector com, MVector userTaskForce, float totalMass) {
		float zp = -com.getY();
		float numerator = zp * userTaskForce.getX();
		float denominator = totalMass * GRAVITY + userTaskForce.getY();
		float xp = numerator / denominator;
		return new MVector(xp, zp);
	}

	private void distributeForces(List<Body> bodies, List<WheelJoint> joints, List<MVector> jointPositions,
			List<MVector> contactPositions, MVector com, Matrix desCopPos, MVector desCopForce) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		// the Az matrix - we're really using the y coordinate though because we're in two dimensions
		Matrix Az = stackX(contactPositions).transpose().augment();
		System.out.println("AZ: \n" + Az);
		Matrix Aztranspose = Az.transpose();
		System.out.println("AZ transpose: \n" + Aztranspose);

		Matrix AzTimesTranspose = Az.multiply(Aztranspose);
		System.out.println("AZ Times Transpose: \n" + AzTimesTranspose);
		Matrix AzTTInvert = AzTimesTranspose.invert();

		// same for both x and z(y) because we're ommiting the "depth" dimension
		Matrix Asharpz = Aztranspose.multiply(AzTTInvert);
		System.out.println("A Sharp Z: \n" + Asharpz);

		Matrix desCopPosX = new MVector(desCopPos.getVal(0, 0), 1);

		Matrix desContactForcesZ = Asharpz.multiply(desCopPosX.scalarMultiply(desCopForce.getY()));
		Matrix desContactForcesX = Asharpz.multiply(desCopPosX.scalarMultiply(desCopForce.getX()));

		System.out.println("Des contact forces Z: \n" + desContactForcesZ);
		System.out.println("Des cop position: \n" + desCopPosX);
		System.out.println("Cop: \n" + desCopPos);

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		for (int i = 0; i < joints.size(); i++) {
			WheelJoint joint = joints.get(i);
			MVector jPos = jointPositions.get(i);
			MVector cPos = contactPositions.get(i);
			MVector force = new MVector(desContactForcesX.getVal(i, 0), desContactForcesZ.getVal(i, 0));

			MVector r = cPos.subtract(jPos).toVector();
			System.out.println("Joint " + i + ":");
			System.out.println("\tR vector: \n\t" + r);
			System.out.println("\tForce vector: \n\t" + force);

			float torque = r.crossProduct(force);
			if (torque > 0) {
				torque = Math.min(MAX_TORQUE, torque);
			} else {
				torque = Math.max(-MAX_TORQUE, torque);
			}

			System.out.println("\tTorque: " + torque);

			joint.setMaxMotorTorque(Math.abs(torque));
			joint.setMotorSpeed(-(torque / Math.abs(torque)) * 1000000);
		}
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