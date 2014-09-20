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
	public TexturedSolid(World world, PhysicsManager physics, LWJGLRenderer2D renderer, Vector2f position,
			float rotation, Vector2f dimensions, BodyType type, String texture) {
		super(world);
		addComponent(new SolidRenderComponent(renderer));
		setData("sys_dimensions", dimensions);
		setData("sys_material", MaterialFactory.createBasicMaterial(texture));

		Body fbody = physics.createBody(this, PhysicsFactory.makeBodyDef(position, type, rotation));
		fbody.createFixture(PhysicsFactory.makeRectangularFixtureDef(new Vector2f(0, 0), dimensions, 0));
	}
}
