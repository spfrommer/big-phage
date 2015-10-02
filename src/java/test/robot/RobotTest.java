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
import org.jbox2d.dynamics.joints.WheelJoint;
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
import glextra.material.Material;
import glextra.renderer.Light.PointLight;
import gltools.input.Key;
import gltools.input.KeyListener;
import gltools.input.Keyboard;

public class RobotTest extends SimplePhysicsGame {
	private Object m_waitObject = new Object();
	
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
				synchronized(m_waitObject) {
					m_waitObject.notifyAll();
				}
			}
		});
		frame.setLocation(0, 0);
		frame.add(button);
		frame.setSize(300, 100);
		frame.setVisible(true);
	}

	@Override
	public void createMaterials(MaterialFactory factory) {
		MaterialPool.materials.put("metalplate", factory.createBasicMaterial("Textures/metalplate.png"));
		MaterialPool.materials.put("metalplatetriangle",
				factory.createBasicMaterial("Textures/metalplatetriangle.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();
		ManagerFilter filter = this.getPhysicsManager().getCollisionFilter();

		float headLength = 0.2f;
		float halfHeadLength = 0.5f * headLength;
		float headAngle = 0f;
		float headWidth = 0.1f;
		float connectorWidth = 0.5f;
		float halfCWidth = 0.5f * connectorWidth;
		float connectorHeight = 0.1f;
		float legLength = 1f;
		float halfLegLength = 0.5f * legLength;
		float legWidth = 0.1f;
		float jointFrequency = 10f;
		float jointDampening = 0f;
		float connectorDensity = 1.5f;

		// make the robot
		Entity leg1 = factory.createTriangularTexturedSolid(new Vector2f(-halfCWidth, halfLegLength), (float) Math.PI,
				new Vector2f(legWidth, legLength), BodyType.DYNAMIC, MaterialPool.materials.get("metalplatetriangle"));
		leg1.setData("sys_groups", new TagList("robot"));
		filter.addFilter(leg1, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(leg1);

		Entity leg2 = factory.createTriangularTexturedSolid(new Vector2f(halfCWidth, halfLegLength), (float) Math.PI,
				new Vector2f(legWidth, legLength), BodyType.DYNAMIC, MaterialPool.materials.get("metalplatetriangle"));
		leg2.setData("sys_groups", new TagList("robot"));
		filter.addFilter(leg2, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(leg2);

		Entity connector = factory.createTexturedSolid(new Vector2f(0f, legLength), 0, new Vector2f(connectorWidth,
				connectorHeight), connectorDensity, BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		connector.setData("sys_groups", new TagList("robot"));
		filter.addFilter(connector, new ExclusiveGroupFilter(new TagList("robot")));
		connector.addComponent(new KeyboardImpulseComponent());
		// ((Body) connector.getData("sys_body")).applyLinearImpulse(new Vec2(0.03f, 0.03f), new Vec2(0f, legLength));

		this.getWorld().addEntity(connector);

		Entity head = factory.createTexturedSolid(new Vector2f((float) (Math.sin(headAngle) * halfHeadLength),
				legLength + (float) (Math.cos(headAngle) * halfHeadLength)), -headAngle, new Vector2f(headWidth,
				headLength), BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		head.setData("sys_groups", new TagList("robot"));
		filter.addFilter(head, new ExclusiveGroupFilter(new TagList("robot")));
		this.getWorld().addEntity(head);

		// create the joints
		JointDef legJoint1 = PhysicsFactory.makeWheelDef((Body) leg1.getData("sys_body"),
				(Body) connector.getData("sys_body"), new Vector2f(0f, -halfLegLength), new Vector2f(-halfCWidth, 0f),
				new Vector2f(0f, 1f), jointFrequency, jointDampening, false);
		WheelJoint joint1 = (WheelJoint) this.getPhysicsManager().createJoint(legJoint1);
		joint1.enableMotor(true);

		JointDef legJoint2 = PhysicsFactory.makeWheelDef((Body) leg2.getData("sys_body"),
				(Body) connector.getData("sys_body"), new Vector2f(0f, -halfLegLength), new Vector2f(halfCWidth, 0f),
				new Vector2f(0f, 1f), jointFrequency, jointDampening, false);
		WheelJoint joint2 = (WheelJoint) this.getPhysicsManager().createJoint(legJoint2);
		joint2.enableMotor(true);

		JointDef headJoint = PhysicsFactory.makeWeldDef((Body) connector.getData("sys_body"),
				(Body) head.getData("sys_body"), new Vector2f(0f, 0f), new Vector2f(0f, -halfHeadLength), false,
				-headAngle);
		Joint joint3 = this.getPhysicsManager().createJoint(headJoint);

		// create the controller
		RobotController controller = new RobotController(this.getWorld(), leg1, leg2, connector, head, joint1, joint2,
				joint3);
		this.getPhysicsManager().getCollisionHandler().addCompleteListener(controller.getControllerComponent());
		this.getWorld().addEntity(controller);

		// make the ground
		Entity platform = factory.createTexturedSolid(new Vector2f(0f, -0.5f), 0f, new Vector2f(20f, 1f),
				BodyType.KINEMATIC, MaterialPool.materials.get("metalplate"));
		platform.addComponent(new KinematicRotateComponent());
		this.getWorld().addEntity(platform);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		getWorld().addEntity(light);

		getGameState().keyboard.addListener(new KeyListener() {
			@Override
			public void keyPressed(Keyboard k, Key key) {
				if (k.getKey("B") == key) {
					Material material = MaterialPool.materials.get("metalplate");
					Entity box = getGameFactory().createTexturedSolid(
							new Vector2f((float) (Math.random() - 0.5f) * 1f, 5f),
							(float) (Math.random() * Math.PI * 2), new Vector2f(0.3f, 0.5f), 0.5f, BodyType.DYNAMIC,
							material);
					getWorld().addEntity(box);
				}
			}

			@Override
			public void keyReleased(Keyboard k, Key key) {

			}
		});
		
		while(!getGameState().keyboard.isKeyPressed(getGameState().keyboard.getKey("ESCAPE"))
				&& !getGameState().display.closeRequested()) {
			try {
				synchronized(m_waitObject) {
					m_waitObject.wait();
				}
			} catch (InterruptedException e) {}
			for (int i = 0; i < 3600; i++) {
				System.out.println("Stepping!");
				doStep();
			}
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
