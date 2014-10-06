package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import glextra.renderer.Light;
import glextra.renderer.Light.PointLight;

public class LightComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_light"));

	public LightComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Light light = (PointLight) getData("sys_light");

		state.renderer.renderLight(light);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
