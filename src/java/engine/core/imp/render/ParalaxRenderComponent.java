package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.material.Material;
import glextra.renderer.Renderer2D;

public class ParalaxRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_dimensions",
			"sys_rotation", "sys_material", "sys_paralaxLayer"));

	public ParalaxRenderComponent() {

	}

	@Override
	public void update(float time, GameState state) {
		Vector2f position = (Vector2f) getData("sys_position");
		Vector2f dimensions = (Vector2f) getData("sys_dimensions");
		float rotation = (Float) getData("sys_rotation");
		Material mat = (Material) getData("sys_material");
		float layer = (float) ((Integer) getData("sys_paralaxLayer") + 1);
		Renderer2D renderer = state.renderer;

		float camX = renderer.getViewTranslation().x;
		float camY = renderer.getViewTranslation().y;
		float transX = layer * camX * 0.1f;
		float transY = layer * camY * 0.1f;

		renderer.setMaterial(mat);
		renderer.pushModel();
		System.out.println("Scaling: " + layer);
		renderer.scale(layer, layer);
		renderer.translate(transX, transY);
		renderer.translate(position.x, position.y);
		renderer.rotate(rotation);
		renderer.fillRect(-dimensions.x / 2, -dimensions.y / 2, dimensions.x, dimensions.y);
		System.out.println("Initial: " + renderer.getModelScale());
		renderer.popModel();
		System.out.println("After: " + renderer.getModelScale());
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
