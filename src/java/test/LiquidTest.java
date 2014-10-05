package test;

import java.awt.geom.Path2D;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.DataManager;
import engine.core.frame.Entity;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.liquid.FountainComponent;
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.render.lwjgl.LiquidRenderComponent;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import gltools.input.Key;
import gltools.input.KeyListener;
import gltools.input.Keyboard;
import gltools.texture.Color;

public class LiquidTest extends PhysicsGame {
	public LiquidTest() {
		super("Liquid Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the walls
		Entity ground = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f), BodyType.KINEMATIC,
				MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(ground);

		Entity wall1 = factory.createTexturedSolid(new Vector2f(1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wall1);

		Entity wall2 = factory.createTexturedSolid(new Vector2f(-1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wall2);

		Entity liquid = new Entity(getWorld());
		liquid.addComponent(new LiquidRenderComponent(getRenderer()));
		Path2D.Float path = new Path2D.Float();
		float y = 0.5f;
		float x = 0.0f;
		path.moveTo(-1.5f + x, 0f + y);
		path.lineTo(-1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 0f + y);
		path.lineTo(-0.3f + x, 0.5f + y);
		getPhysicsManager().createLiquid(liquid, PhysicsFactory.makeLiquidDef(path, 0.4f, 0.1f, 0.02f));
		liquid.setData("sys_material", MaterialFactory.createBasicMaterial(new Color(0f, 0f, 1f)));
		getWorld().addEntity(liquid);

		Entity liquidFountain = new Entity(getWorld());
		liquidFountain.addComponent(new FountainComponent((Liquid) liquid.getData("sys_liquid"), getPhysicsManager()));
		liquidFountain.setManager("sys_position", DataManager.NONE);
		liquidFountain.setData("sys_position", new Vector2f(0f, 7f));
		getWorld().addEntity(liquidFountain);

		getKeyboard().addListener(new KeyListener() {
			@Override
			public void keyPressed(Keyboard k, Key key) {
				Entity box = getGameFactory().createTexturedSolid(
						new Vector2f((float) (Math.random() - 0.5f) * 2f, 5f), 0f, new Vector2f(0.3f, 0.5f),
						BodyType.DYNAMIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
				getWorld().addEntity(box);
			}

			@Override
			public void keyReleased(Keyboard k, Key key) {

			}
		});
	}

	@Override
	public void preUpdate() {
	}

	@Override
	public void postUpdate() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		LiquidTest liquid = new LiquidTest();
		liquid.start();
	}
}
