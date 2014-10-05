package test;

import org.jbox2d.dynamics.BodyType;

import engine.commons.utils.Vector2f;
import engine.core.exec.PhysicsGame;
import engine.core.frame.Entity;
import engine.core.imp.render.lwjgl.MaterialFactory;
import engine.core.presets.PhysicsGameFactory;

public class LayerTest extends PhysicsGame {
	public LayerTest() {
		super("Layer Test");
	}

	@Override
	public void onStart() {
		PhysicsGameFactory factory = this.getGameFactory();

		Entity background = factory.createTexturedSolid(new Vector2f(0f, 0f), 0f, new Vector2f(3f, 1f),
				BodyType.STATIC, MaterialFactory.createBasicMaterial("Textures/grassbackground.png"));
		background.setUpdateOrder(1);
		getWorld().addEntity(background);

		Entity wall1 = factory.createTexturedSolid(new Vector2f(1.75f, 1.5f), 0f, new Vector2f(0.5f, 4f),
				BodyType.KINEMATIC, MaterialFactory.createBasicMaterial("Textures/metalplate.jpg"));
		wall1.setUpdateOrder(0);
		getWorld().addEntity(wall1);
	}

	@Override
	public void preUpdate() {

	}

	@Override
	public void postUpdate() {

	}

	public static void main(String[] args) {
		LayerTest test = new LayerTest();
		test.start();
	}
}
