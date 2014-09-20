package engine.core.imp.render.lwjgl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.Component;
import engine.core.imp.physics.Vector2f;
import glextra.material.Material;
import glextra.renderer.LWJGLRenderer2D;

public class RenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material"));

	private LWJGLRenderer2D m_renderer;

	public RenderComponent(LWJGLRenderer2D renderer) {
		m_renderer = renderer;
	}

	@Override
	public void update(float time) {
		Vector2f position = (Vector2f) getData("sys_position");
		// double rotation = (Double) getData("sys_rotation");
		Material mat = (Material) getData("sys_material");

		m_renderer.setMaterial(mat);
		m_renderer.pushModel();
		m_renderer.translate((int) position.x, (int) position.y);
		// m_renderer.translate(50, 50);
		// m_renderer.rotate((float) (Math.PI * 0.25));
		// m_renderer.translate(-50, -50);
		m_renderer.fillRect(0, 0, 10, 10);
		m_renderer.popModel();
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
