package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.material.Material;
import glextra.renderer.Renderer2D;

public class SolidRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_dimensions",
			"sys_rotation", "sys_material", "sys_repeatMaterial", "sys_repeatCount"));

	public SolidRenderComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Vector2f position = (Vector2f) getData("sys_position");
		Vector2f dimensions = (Vector2f) getData("sys_dimensions");
		float rotation = (Float) getData("sys_rotation");
		Material mat = (Material) getData("sys_material");
		boolean repeat = (Boolean) getData("sys_repeatMaterial");
		float repeatCount = (Float) getData("sys_repeatCount");
		Renderer2D renderer = state.renderer;

		renderer.setMaterial(mat);
		renderer.pushModel();
		renderer.translate(position.x, position.y);
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
