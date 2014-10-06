package test.pendulum;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

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
		pendulum.addComponent(new KeyboardRotateComponent());
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

		// add the light
		Entity light = new Entity(getWorld());
		light.addComponent(new LightComponent());
		light.setData("sys_light", new PointLight(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, 0.05f), new Color(1f,
				1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f)));
		getWorld().addEntity(light);
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
