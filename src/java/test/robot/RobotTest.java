package test.robot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.JointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.lwjgl.LWJGLException;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.group.TagList;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.collision.ExclusiveGroupFilter;
import engine.core.imp.physics.collision.ManagerFilter;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glcommon.Color;
import glcommon.vector.Vector3f;
import glextra.renderer.Light.PointLight;

public class RobotTest extends SimplePhysicsGame {
	public RobotTest() {
		super("Robot test");
		this.setAutoStep(false);

		makeStepGui();
	}

	private void makeStepGui() {
		JFrame frame = new JFrame("Controller");
		JButton button = new JButton("Step");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					org.lwjgl.opengl.Display.makeCurrent();
				} catch (LWJGLException e1) {
					e1.printStackTrace();
				}
				for (int i = 0; i < 5; i++)
					doStep();
			}
		});
		frame.setLocation(0, 0);
		frame.add(button);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("metalplate", MaterialFactory.createBasicMaterial("Textures/metalplate.png"));
		MaterialPool.materials.put("metalplatetriangle",
				MaterialFactory.createBasicMaterial("Textures/metalplatetriangle.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();
		ManagerFilter filter = this.getPhysicsManager().getCollisionFilter();

		// make the robot
		Entity leg1 = factory.createTriangularTexturedSolid(new Vector2f(-0.25f, 0.5f), (float) Math.PI, new Vector2f(
				0.1f, 1f), BodyType.DYNAMIC, MaterialPool.materials.get("metalplatetriangle"));
		leg1.setData("sys_groups", new TagList("robot"));
		filter.addFilter(leg1, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(leg1);

		Entity leg2 = factory.createTriangularTexturedSolid(new Vector2f(0.25f, 0.5f), (float) Math.PI, new Vector2f(
				0.1f, 1f), BodyType.DYNAMIC, MaterialPool.materials.get("metalplatetriangle"));
		leg2.setData("sys_groups", new TagList("robot"));
		filter.addFilter(leg2, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(leg2);

		Entity connector = factory.createTexturedSolid(new Vector2f(0f, 1f), 0, new Vector2f(0.5f, 0.1f),
				BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		connector.setData("sys_groups", new TagList("robot"));
		filter.addFilter(connector, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(connector);

		Entity head = factory.createTexturedSolid(new Vector2f(0f, 1.4f), 0, new Vector2f(0.1f, 0.4f),
				BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		head.setData("sys_groups", new TagList("robot"));
		head.addComponent(new KeyboardRotateComponent());
		filter.addFilter(head, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(head);

		// create the joints
		JointDef legJoint1 = PhysicsFactory.makeRevoluteDef((Body) leg1.getData("sys_body"),
				(Body) connector.getData("sys_body"), new Vector2f(0f, -0.5f), new Vector2f(-0.25f, 0f), false);
		RevoluteJoint joint1 = (RevoluteJoint) this.getPhysicsManager().createJoint(legJoint1);
		joint1.enableMotor(true);

		JointDef legJoint2 = PhysicsFactory.makeRevoluteDef((Body) leg2.getData("sys_body"),
				(Body) connector.getData("sys_body"), new Vector2f(0f, -0.5f), new Vector2f(0.25f, 0f), false);
		RevoluteJoint joint2 = (RevoluteJoint) this.getPhysicsManager().createJoint(legJoint2);
		joint2.enableMotor(true);

		JointDef bodyJoint = PhysicsFactory.makeRevoluteDef((Body) connector.getData("sys_body"),
				(Body) head.getData("sys_body"), new Vector2f(0f, 0f), new Vector2f(0f, -0.2f), false, -0f);
		Joint joint3 = this.getPhysicsManager().createJoint(bodyJoint);

		// create the controller
		RobotController controller = new RobotController(this.getWorld(), leg1, leg2, connector, head, joint1, joint2,
				joint3);
		this.getPhysicsManager().getCollisionHandler().addCompleteListener(controller.getControllerComponent());
		this.getWorld().addEntity(controller);

		// make the ground
		Entity platform = factory.createTexturedSolid(new Vector2f(0f, -0.5f), 0f, new Vector2f(3f, 1f),
				BodyType.STATIC, MaterialPool.materials.get("metalplate"));
		this.getWorld().addEntity(platform);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		getWorld().addEntity(light);

		try {
			org.lwjgl.opengl.Display.releaseContext();
		} catch (LWJGLException e2) {
			e2.printStackTrace();
		}
	}

	@Override
	protected void preUpdate() {

	}

	@Override
	protected void postUpdate() {

	}

	public static void main(String[] args) {
		RobotTest test = new RobotTest();
		test.start();
	}
}
