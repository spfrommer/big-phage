package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.imp.physics.liquid.Liquid;
import glextra.material.Material;
import glextra.renderer.Renderer2D;

public class LiquidRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_liquid", "sys_material"));

	public LiquidRenderComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Liquid liquid = (Liquid) getData("sys_liquid");
		Material mat = (Material) getData("sys_material");
		float psize = liquid.getParticleRadius();
		Renderer2D renderer = state.renderer;

		renderer.setMaterial(mat);

		for (Body b : liquid.getParticles()) {
			renderer.pushModel();
			renderer.translate(b.getPosition().x, b.getPosition().y);
			renderer.fillRect(-psize, -psize, psize * 2, psize * 2);
			renderer.popModel();
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
