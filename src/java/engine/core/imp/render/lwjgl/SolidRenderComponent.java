package engine.core.imp.render.lwjgl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.frame.Component;
import glextra.material.Material;
import glextra.renderer.LWJGLRenderer2D;

public class SolidRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material"));

	private LWJGLRenderer2D m_renderer;

	public SolidRenderComponent(LWJGLRenderer2D renderer) {
		m_renderer = renderer;
	}

	@Override
	public void update(float time) {
		Vector2f position = (Vector2f) getData("sys_position");
		Vector2f dimensions = (Vector2f) getData("sys_dimensions");
		float rotation = (Float) getData("sys_rotation");
		Material mat = (Material) getData("sys_material");

		m_renderer.setMaterial(mat);
		m_renderer.pushModel();
		m_renderer.translate(position.x, position.y);
		m_renderer.rotate(rotation);
		m_renderer.fillRect(-dimensions.x / 2, -dimensions.y / 2, dimensions.x, dimensions.y);
		m_renderer.popModel();
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
