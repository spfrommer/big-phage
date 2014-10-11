package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;

public class StationaryCameraControllerComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_camPosition", "sys_camScale",
			"sys_camRotation"));

	@Override
	public void update(float time, GameState state) {
		Vector2f position = (Vector2f) getData("sys_camPosition");
		Vector2f camScale = (Vector2f) getData("sys_camScale");

		state.renderer.viewTrans(-position.x, -position.y);
		state.renderer.viewScale(camScale.x, camScale.y);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
