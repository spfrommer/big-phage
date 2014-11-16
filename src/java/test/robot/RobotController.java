package test.robot;

import org.jbox2d.dynamics.joints.Joint;

import engine.core.frame.Entity;
import engine.core.frame.World;

public class RobotController extends Entity {
	private RobotControllerComponent m_controller;

	public RobotController(World world, Entity leg1, Entity leg2, Entity connector, Entity body, Joint legJoint1,
			Joint legJoint2, Joint bodyJoint) {
		super(world);

		this.setData("rob_leg1", leg1.getData("sys_body"));
		this.setData("rob_leg2", leg2.getData("sys_body"));
		this.setData("rob_connector", connector.getData("sys_body"));
		this.setData("rob_body", body.getData("sys_body"));
		this.setData("rob_legJoint1", legJoint1);
		this.setData("rob_legJoint2", legJoint2);
		this.setData("rob_bodyJoint", bodyJoint);

		m_controller = new RobotControllerComponent();
		this.addComponent(m_controller);
	}

	public RobotControllerComponent getControllerComponent() {
		return m_controller;
	}
}
