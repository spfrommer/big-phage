package test;

import java.awt.geom.Path2D;
import java.util.Arrays;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.physics.PhysicsFactory;
import engine.core.imp.physics.liquid.FountainComponent;
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.render.LightComponent;
import engine.core.imp.render.LiquidRenderComponent;
import engine.core.imp.render.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;
import glextra.renderer.Light.PointLight;
import gltools.input.Key;
import gltools.input.KeyListener;
import gltools.input.Keyboard;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class LiquidTest extends PhysicsGame {
	public LiquidTest() {
		super("Liquid Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		// make the walls
		Entity wallbottom = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(7f, 1f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wallbottom);

		Entity wallright = factory.createTexturedSolid(new Vector2f(3.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wallright);

		Entity wallleft = factory.createTexturedSolid(new Vector2f(-3.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		getWorld().addEntity(wallleft);

		// make the liquid
		Entity liquid = new Entity(getWorld());
		liquid.addComponent(new LiquidRenderComponent());

		Path2D.Float path = new Path2D.Float();
		float y = 0.5f;
		float x = 0.0f;
		path.moveTo(-1.5f + x, 0f + y);
		path.lineTo(-1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 4f + y);
		path.lineTo(1.5f + x, 0f + y);
		path.lineTo(-0.3f + x, 0.5f + y);

		getPhysicsManager().createLiquid(liquid,
				PhysicsFactory.makeLiquidDef(path, 0.4f, 0.1f, 0.03f, new Vector2f(0f, 0f), 5.8f));
		liquid.setData("sys_material", MaterialFactory.createBasicMaterial("Textures/waterdrop.png"));
		getWorld().addEntity(liquid);

		// make the fountain
		Entity liquidFountain = new Entity(getWorld());
		liquidFountain.setData("sys_fountainPosition", new Vector2f(2.5f, 3f));
		liquidFountain.addComponent(new FountainComponent((Liquid) liquid.getData("sys_liquid"), getPhysicsManager()));
		getWorld().addEntity(liquidFountain);

		// add the light
		Entity light = new Entity(getWorld());
		light.setData("sys_lights", Arrays.asList(new PointLight(new Vector3f(0f, 0f, 5f), new Vector3f(0f, 0f, 0.05f),
				new Color(1f, 1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f))));
		light.addComponent(new LightComponent());
		getWorld().addEntity(light);

		getKeyboard().addListener(new KeyListener() {
			@Override
			public void keyPressed(Keyboard k, Key key) {
				Entity box = getGameFactory().createTexturedSolid(
						new Vector2f((float) (Math.random() - 0.5f) * 4f, 5f), (float) (Math.random() * Math.PI * 2),
						new Vector2f(0.3f, 0.5f), BodyType.DYNAMIC,
						MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
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

	}

	public static void main(String[] args) {
		LiquidTest liquid = new LiquidTest();
		liquid.start();
	}
}
