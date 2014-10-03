package test.pendulum;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;

public class PendulumTest extends PhysicsGame {

	public PendulumTest() {
		super("Pendulum Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the pendulum
		Entity pendulum = factory.createTexturedSolid(new Vector2f(-0.1f, 1f), 0, new Vector2f(0.2f, 2f),
				BodyType.DYNAMIC, MaterialFactory.createBasicMaterial("Textures/pendulum.png"));
		pendulum.setData("pen_penlength", 1f); // sets the length of the pendulum to be right in the middle
		pendulum.addComponent(new KeyboardRotateComponent(this.getKeyboard()));
		this.getWorld().addEntity(pendulum);

		// make the ground
		Entity platform = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		platform.setData("pen_pendulum", pendulum);
		platform.addComponent(new KinematicBalancerComponent());
		this.getWorld().addEntity(platform);

		// create the joint
		RevoluteJointDef revoluteDef = PhysicsFactory.makeRevoluteDef((Body) platform.getData("sys_body"),
				(Body) pendulum.getData("sys_body"), new Vector2f(0f, 0.5f), new Vector2f(0, -1f), false);
		this.getPhysicsManager().createJoint(revoluteDef);
	}

	@Override
	public void preUpdate() {

	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		PendulumTest test = new PendulumTest();
		test.start();
	}
}
