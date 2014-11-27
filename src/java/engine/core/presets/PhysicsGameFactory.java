package engine.core.presets;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.frame.Component;
import engine.core.frame.Entity;
import engine.core.frame.World;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.render.SolidRenderComponent;
import glextra.material.Material;

public class PhysicsGameFactory {
	private World m_world;
	private PhysicsManager m_physics;

	private static final float DEFAULT_DENSITY = 1.5f;
	private static final float DEFAULT_FRICTION = 1f;
	private static final float DEFAULT_RESTITUTION = 0.1f;

	public PhysicsGameFactory(World world, PhysicsManager physics) {
		m_world = world;
		m_physics = physics;
	}

	public Entity createTriangularTexturedSolid(Vector2f position, float rotation, Vector2f dimensions, BodyType type,
			Material material) {
		Entity entity = new Entity(m_world);
		entity.addComponent(new SolidRenderComponent());
		entity.setData("sys_dimensions", dimensions);
		entity.setData("sys_material", material);

		m_physics.createSolid(entity, PhysicsFactory.makeBodyDef(position, type, rotation, 0f));
		((Body) entity.getData("sys_body")).createFixture(PhysicsFactory.makeTriangularFixtureDef(dimensions, 0,
				DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_RESTITUTION));
		return entity;
	}

	public Entity createTexturedSolid(Vector2f position, float rotation, Vector2f dimensions, BodyType type,
			Material material) {
		Entity entity = new Entity(m_world);
		entity.addComponent(new SolidRenderComponent());
		entity.setData("sys_dimensions", dimensions);
		entity.setData("sys_material", material);

		m_physics.createSolid(entity, PhysicsFactory.makeBodyDef(position, type, rotation, 0f));
		((Body) entity.getData("sys_body")).createFixture(PhysicsFactory.makeRectangularFixtureDef(dimensions, 0,
				DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_RESTITUTION));
		return entity;
	}

	public Entity createTexturedSolid(Vector2f position, float rotation, Vector2f dimensions, float density,
			BodyType type, Material material) {
		Entity entity = new Entity(m_world);
		entity.addComponent(new SolidRenderComponent());
		entity.setData("sys_dimensions", dimensions);
		entity.setData("sys_material", material);

		m_physics.createSolid(entity, PhysicsFactory.makeBodyDef(position, type, rotation, 0f));
		((Body) entity.getData("sys_body")).createFixture(PhysicsFactory.makeRectangularFixtureDef(dimensions, 0,
				density, DEFAULT_FRICTION, DEFAULT_RESTITUTION));
		return entity;
	}

	public Entity createTexturedSolid(Vector2f position, float rotation, Vector2f dimensions, BodyType type,
			Material material, Component render) {
		Entity entity = new Entity(m_world);
		entity.addComponent(render);
		entity.setData("sys_dimensions", dimensions);
		entity.setData("sys_material", material);

		m_physics.createSolid(entity, PhysicsFactory.makeBodyDef(position, type, rotation, 0f));
		((Body) entity.getData("sys_body")).createFixture(PhysicsFactory.makeRectangularFixtureDef(dimensions, 0,
				DEFAULT_DENSITY, DEFAULT_FRICTION, DEFAULT_RESTITUTION));
		return entity;
	}
}
