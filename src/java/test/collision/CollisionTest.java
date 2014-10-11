package test.collision;

import java.util.Arrays;
import java.util.List;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.MaterialPool;
import engine.core.exec.SimplePhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.group.TagList;
import engine.core.imp.physics.collision.InclusiveGroupFilter;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class CollisionTest extends SimplePhysicsGame {

	public CollisionTest() {
		super("Collision Test");
	}

	@Override
	public void createMaterials() {
		MaterialPool.materials.put("metalplate", MaterialFactory.createBasicMaterial("Textures/metalplate.png"));
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the ground
		Entity ground = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(2f, 1f), BodyType.KINEMATIC,
				MaterialPool.materials.get("metalplate"));
		ground.setData("sys_groups", new TagList("testgroup1"));
		getWorld().addEntity(ground);

		// make the brick
		Entity brick = factory.createTexturedSolid(new Vector2f(1f, 3f), (float) Math.PI / 4 + 0.1f, new Vector2f(1f,
				1f), BodyType.DYNAMIC, MaterialPool.materials.get("metalplate"));
		this.getPhysicsManager().getCollisionHandler().addEvent(brick, new PrintEvent());
		this.getPhysicsManager().getCollisionFilter()
				.addFilter(brick, new InclusiveGroupFilter(new TagList("testgroup2", "testgroup1")));
		brick.setData("sys_groups", new TagList("testgroup1", "testgroup2"));
		getWorld().addEntity(brick);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 1f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		getWorld().addEntity(light);

		List<Entity> group1 = getWorld().getGroupManager().getEntities("testgroup1");
		System.out.println(group1.size());

		List<Entity> group2 = getWorld().getGroupManager().getEntities("testgroup2");
		System.out.println(group2.size());
	}

	@Override
	protected void preUpdate() {

	}

	@Override
	protected void postUpdate() {

	}

	public static void main(String[] args) {
		CollisionTest test = new CollisionTest();
		test.start();
	}
}
