package test.robot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Transform;
import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import gltools.input.Keyboard;

public class KinematicRotateComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body"));

	public KinematicRotateComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Body body = (Body) getData("sys_body");
		Keyboard keyboard = state.keyboard;
		Transform bTrans = body.getTransform();
		float rotSpeed = 0.1f;

		if (keyboard.isKeyPressed(keyboard.getKey('J'))) {
			body.setAngularVelocity(rotSpeed);
		} else if (keyboard.isKeyPressed(keyboard.getKey('L'))) {
			body.setAngularVelocity(-rotSpeed);
		} else {
			body.setAngularVelocity(0);
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
