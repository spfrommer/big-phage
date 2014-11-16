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

		Joint legJoint1 = (Joint) getData("rob_legJoint1");
		Joint legJoint2 = (Joint) getData("rob_legJoint2");
		Joint bodyJoint = (Joint) getData("rob_bodyJoint");

		// PI / 2 factor is because the rotation starts at north and goes counter clockwise
		float contact1x = (float) Math.cos(leg1.getAngle() + Math.PI / 2) * legLenHalf;
		float contact1y = (float) Math.sin(leg1.getAngle() + Math.PI / 2) * legLenHalf;
		MVector contact1 = leg1Pos.add(new MVector(contact1x, contact1y)).toVector();

		float contact2x = (float) Math.cos(leg2.getAngle() + Math.PI / 2) * legLenHalf;
		float contact2y = (float) Math.sin(leg2.getAngle() + Math.PI / 2) * legLenHalf;
		MVector contact2 = leg2Pos.add(new MVector(contact2x, contact2y)).toVector();

		// System.out.println("Left leg: " + leg1Pos + " : " + leg1Mass);
		// System.out.println("Right leg: " + leg2Pos + " : " + leg2Mass);
		// System.out.println("Connector: " + connectorPos + " : " + connectorMass);
		// System.out.println("Body: " + bodyPos + " : " + bodyMass);

		float totalMass = leg1Mass + leg2Mass + connectorMass + bodyMass;
		Matrix posSum = leg1Pos.scalarMultiply(leg1Mass).add(leg2Pos.scalarMultiply(leg2Mass))
				.add(connectorPos.scalarMultiply(connectorMass)).add(bodyPos.scalarMultiply(bodyMass));
		MVector com = posSum.scalarMultiply(1 / totalMass).toVector();

		MVector gaf = m_leftForce.add(m_rightForce).toVector();

		implementAlgorithms(Arrays.asList(leg1, leg2, connector, body), Arrays.asList(legJoint1, legJoint2, bodyJoint),
				Arrays.asList(contact1, contact2), Arrays.asList(m_leftForce, m_rightForce), com, gaf);

		System.out.println("Total mass: " + totalMass);
		System.out.println("COM: " + com);

		System.out.println("Ground applied force: " + gaf);

		System.out.println("Left contact position: " + contact1);
		System.out.println("Right contact position: " + contact2);

		System.out.println("Left leg GRF: " + m_leftForce);
		System.out.println("Right leg GRF: " + m_rightForce);

		System.out.println("---------------------------------------------------------");
	}

	private void implementAlgorithms(List<Body> bodies, List<Joint> joints, List<MVector> contactPositions,
			List<MVector> contactForces, MVector com, MVector gaf) {
		float xpNumerator = 0;
		float xpDenominator = 0;
		for (int i = 0; i < contactPositions.size(); i++) {
			MVector cPosition = contactPositions.get(i);
			MVector cForce = contactForces.get(i);
			xpNumerator += cPosition.getX() * cForce.getY();
			xpDenominator += cForce.getY();
		}

		Matrix cop = new Matrix(2, 1, new float[] { xpNumerator / xpDenominator, 1 });
		Matrix descop = new Matrix(2, 1, new float[] { 0f, 1f });

		// the Az matrix - we're really using the y coordinate though because we're in two dimensions
		Matrix Az = stackX(contactPositions).transpose().augment();
		Matrix Aztranspose = Az.transpose();

		Matrix AzTimesTranspose = Az.multiply(Aztranspose);
		Matrix AzTTInvert = AzTimesTranspose.invert();

		Matrix Asharpz = Aztranspose.multiply(AzTTInvert);

		// Matrix Asharpz = Aztranspose.multiply((Az.multiply(Aztranspose)).invert());
		Matrix desContactForces = Asharpz.multiply(descop.scalarMultiply(5));

		System.out.println("DesContactForces: \n" + desContactForces);

		System.out.println("COP: " + cop);
		System.out.println("AZ: \n" + Az);
		System.out.println("AZ transpose: \n" + Aztranspose);
		System.out.println("A sharp Z: \n" + Asharpz);
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
		// MVector normal = new MVector(normalImpulses[0], normalImpulses[1]);
		// MVector tangent = new MVector(tangentImpulses[0], tangentImpulses[1]);

		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();

		Body leg1 = (Body) getData("rob_leg1");
		Body leg2 = (Body) getData("rob_leg2");

		if (body1 == leg1 || body2 == leg1) {
			/*if (m_updatedLeft) {
				m_leftForce = m_leftForce.add((normal.add(tangent)).scalarMultiply(1 / m_lastTime)).toVector();
			} else {
				m_leftForce = (normal.add(tangent)).scalarMultiply(1 / m_lastTime).toVector();
			}*/
			if (!m_updatedLeft) {
				m_leftForce = new MVector(tangentImpulses[0], normalImpulses[0]).scalarMultiply(-1 / m_lastTime)
						.toVector();
			}
			m_updatedLeft = true;
		}
		if (body1 == leg2 || body2 == leg2) {
			/*if (m_updatedRight) {
				m_rightForce = m_rightForce.add((normal.add(tangent)).scalarMultiply(1 / m_lastTime)).toVector();
			} else {
				m_rightForce = (normal.add(tangent)).scalarMultiply(1 / m_lastTime).toVector();
			}*/
			if (!m_updatedRight) {
				m_rightForce = new MVector(tangentImpulses[0], normalImpulses[0]).scalarMultiply(-1 / m_lastTime)
						.toVector();
			}
			m_updatedRight = true;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {

	}
}