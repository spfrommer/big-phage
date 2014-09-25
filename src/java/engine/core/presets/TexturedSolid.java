package engine.core.presets;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import engine.core.framework.Entity;
import engine.core.framework.World;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.PhysicsManager;
import engine.core.imp.physics.Vector2f;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.imp.render.lwjgl.SolidRenderComponent;
import glextra.renderer.LWJGLRenderer2D;

public class TexturedSolid extends Entity {
	private Body m_body;

	public TexturedSolid(World world, PhysicsManager physics, LWJGLRenderer2D renderer, Vector2f position,
			float rotation, Vector2f dimensions, BodyType type, String texture) {
		super(world);
		addComponent(new SolidRenderComponent(renderer));
		setData("sys_dimensions", dimensions);
		setData("sys_material", MaterialFactory.createBasicMaterial(texture));

		m_body = physics.createBody(this, PhysicsFactory.makeBodyDef(position, type, rotation, 0f));
		m_body.createFixture(PhysicsFactory.makeRectangularFixtureDef(dimensions, 0, 10, 0.3f, 0.1f));
	}
}
