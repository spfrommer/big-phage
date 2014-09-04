package test;

import engine.core.framework.Entity;
import engine.core.framework.World;
import engine.core.imp.render.RenderComponent;

public class FrameworkTest {
	public static void main(String[] main) {
		World world = new World();
		Entity entity = new Entity();
		RenderComponent render = new RenderComponent();
		entity.addComponent(render);
		world.addEntity(entity);
		while (true) {
			world.update(1);
		}
	}
}
