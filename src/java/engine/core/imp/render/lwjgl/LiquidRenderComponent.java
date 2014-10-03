package engine.core.imp.render.lwjgl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.core.frame.Component;
import engine.core.imp.physics.liquid.Liquid;
import glextra.material.Material;
import glextra.renderer.LWJGLRenderer2D;

public class LiquidRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_liquid", "sys_material"));

	private LWJGLRenderer2D m_renderer;

	public LiquidRenderComponent(LWJGLRenderer2D renderer) {
		m_renderer = renderer;
	}

	@Override
	public void update(float time) {
		Liquid liquid = (Liquid) getData("sys_liquid");
		Material mat = (Material) getData("sys_material");

		float psize = liquid.getParticleSize();

		m_renderer.setMaterial(mat);

		for (Body b : liquid.getParticles()) {
			m_renderer.pushModel();
			m_renderer.translate(b.getPosition().x, b.getPosition().y);
			m_renderer.fillRect(-psize + 0.02f, -psize + 0.02f, psize * 2 - 0.04f, psize * 2 - 0.04f);
			m_renderer.popModel();
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
