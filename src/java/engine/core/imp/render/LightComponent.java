package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.renderer.Light;

public class LightComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_lights"));

	public LightComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		@SuppressWarnings("unchecked")
		List<Light> lights = (List<Light>) getData("sys_lights");

		for (Light light : lights)
			state.renderer.renderLight(light);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
