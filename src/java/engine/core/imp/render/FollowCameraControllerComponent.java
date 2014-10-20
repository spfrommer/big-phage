package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.commons.utils.Vector2f;
import engine.core.exec.GameState;
import engine.core.frame.Component;
import engine.core.frame.Entity;

/**
 * Follows an entity assumed to be on
 */
public class FollowCameraControllerComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_camTargetEntity",
			"sys_camScale", "sys_camRotation"));

	@Override
	public void update(float time, GameState state) {
		Entity camTarget = (Entity) getData("sys_camTargetEntity");
		Vector2f camScale = (Vector2f) getData("sys_camScale");
		Vector2f targetPosition = (Vector2f) camTarget.getData("sys_position");

		// float depth = 1f;

		state.renderer.viewTrans(-targetPosition.x / 2, -targetPosition.y / 2);
		state.renderer.viewScale(camScale.x, camScale.y);

	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
