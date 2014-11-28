package test.robot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import gltools.input.Keyboard;

public class KeyboardImpulseComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body"));

	public KeyboardImpulseComponent() {
	}

	@Override
	public void update(float time, GameState state) {
		Body body = (Body) getData("sys_body");
		Keyboard keyboard = state.keyboard;

		if (keyboard.isKeyPressed(keyboard.getKey("LEFT"))) {
			body.applyLinearImpulse(new Vec2(-0.01f, 0f), body.getWorldCenter());
		}

		if (keyboard.isKeyPressed(keyboard.getKey("RIGHT"))) {
			body.applyLinearImpulse(new Vec2(0.01f, 0f), body.getWorldCenter());
		}

		if (keyboard.isKeyPressed(keyboard.getKey("UP"))) {
			body.applyLinearImpulse(new Vec2(0f, 0.01f), body.getWorldCenter());
		}

		if (keyboard.isKeyPressed(keyboard.getKey("DOWN"))) {
			body.applyLinearImpulse(new Vec2(0f, -0.01f), body.getWorldCenter());
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
