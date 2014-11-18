package test.robot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import gltools.input.Keyboard;

public class KeyboardRotateComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body"));

	public KeyboardRotateComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Body body = (Body) getData("sys_body");
		Keyboard keyboard = state.keyboard;

		if (keyboard.isKeyPressed(keyboard.getKey("LEFT"))) {
			body.applyAngularImpulse(0.01f);
		}

		if (keyboard.isKeyPressed(keyboard.getKey("RIGHT"))) {
			body.applyAngularImpulse(-0.01f);
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

}
