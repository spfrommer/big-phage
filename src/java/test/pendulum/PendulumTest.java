package test.pendulum;

import java.util.Arrays;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.renderer.Light.PointLight;

public class PendulumTest extends SimplePhysicsGame {

	public PendulumTest() {
		super("Pendulum Test");
	}

	@Override
	public void createMaterials(MaterialFactory factory) {
		MaterialPool.materials.put("metalplate", factory.createBasicMaterial("Textures/metalplate.png"));
		MaterialPool.materials.put("pendulum", factory.createBasicMaterial("Textures/pendulum.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the pendulum
		Entity pendulum = factory.createTexturedSolid(new Vector2f(-0.1f, 1f), 0, new Vector2f(0.2f, 2f),
				BodyType.DYNAMIC, MaterialPool.materials.get("pendulum"));
		pendulum.setData("pen_penlength", 1f); // sets the length of the pendulum to be right in the middle
		pendulum.addComponent(new KeyboardRotateComponent());
		this.getWorld().addEntity(pendulum);

		// make the ground
		Entity platform = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f), BodyType.DYNAMIC,
				MaterialPool.materials.get("metalplate"));
		platform.setData("pen_pendulum", pendulum);
		platform.addComponent(new DynamicBalancerComponent());
		this.getWorld().addEntity(platform);

		// create the joint
		RevoluteJointDef revoluteDef = PhysicsFactory.makeRevoluteDef((Body) platform.getData("sys_body"),
				(Body) pendulum.getData("sys_body"), new Vector2f(0f, 0.5f), new Vector2f(0, -1f), false);
		this.getPhysicsManager().createJoint(revoluteDef);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
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
