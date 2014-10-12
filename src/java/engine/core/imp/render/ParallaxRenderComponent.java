package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.material.Material;
import glextra.renderer.Renderer2D;

public class ParallaxRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_dimensions",
			"sys_rotation", "sys_material", "sys_parallaxDepth", "sys_repeatMaterial", "sys_repeatCount"));

	public ParallaxRenderComponent() {

	}

	@Override
	public void update(float time, GameState state) {
		Vector2f position = (Vector2f) getData("sys_position");
		Vector2f dimensions = (Vector2f) getData("sys_dimensions");
		float rotation = (Float) getData("sys_rotation");
		Material mat = (Material) getData("sys_material");
		float depth = (Float) getData("sys_parallaxDepth");
		boolean repeat = (Boolean) getData("sys_repeatMaterial");
		float repeatCount = (Float) getData("sys_repeatCount");
		Renderer2D renderer = state.renderer;

		float camX = renderer.getViewTranslation().x;
		float camY = renderer.getViewTranslation().y;
		float transX = 1 / depth * camX;
		float transY = 1 / depth * camY;

		renderer.setMaterial(mat);
		renderer.pushModel();
		renderer.translate(transX, transY);
		renderer.translate(position.x, position.y);
		renderer.scale(1 / depth, 1 / depth);
		renderer.rotate(rotation);
		if (repeat) {
			renderer.fillRect(-(dimensions.x * repeatCount) / 2, -(dimensions.y * repeatCount) / 2, dimensions.x
					* repeatCount, dimensions.y * repeatCount, repeatCount, repeatCount);
		} else {
			renderer.fillRect(-dimensions.x / 2, -dimensions.y / 2, dimensions.x, dimensions.y);
		}
		renderer.popModel();
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
