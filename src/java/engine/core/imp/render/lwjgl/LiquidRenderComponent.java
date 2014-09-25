package engine.core.imp.render.lwjgl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.core.framework.Component;
import engine.core.imp.physics.Vector2f;
import glextra.material.Material;
import glextra.renderer.LWJGLRenderer2D;

public class LiquidRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_particles",
			"sys_particlesize", "sys_material"));

	private LWJGLRenderer2D m_renderer;

	public LiquidRenderComponent(LWJGLRenderer2D renderer) {
		m_renderer = renderer;
	}

	@Override
	public void update(float time) {
		@SuppressWarnings("unchecked")
		List<Vector2f> particles = (List<Vector2f>) getData("sys_particles");
		float psize = (float) getData("sys_particlesize");
		Material mat = (Material) getData("sys_material");

		m_renderer.setMaterial(mat);

		for (Vector2f v : particles) {
			// System.out.println("Rendering: " + v.x + ", " + v.y);
			m_renderer.pushModel();
			m_renderer.translate(v.x, v.y);
			m_renderer.fillRect(-psize + 0.02f, -psize + 0.02f, psize * 2 - 0.04f, psize * 2 - 0.04f);
			m_renderer.popModel();
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
