package test.pendulum;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.core.frame.Component;
import gltools.input.Keyboard;

public class KeyboardRotateComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body"));

	private Keyboard m_keyboard;

	public KeyboardRotateComponent(Keyboard keyboard) {
		m_keyboard = keyboard;
	}

	@Override
	public void update(float time) {
		Body body = (Body) getData("sys_body");

		if (m_keyboard.isKeyPressed(m_keyboard.getKey("LEFT"))) {
			body.applyAngularImpulse(0.01f);
		}

		if (m_keyboard.isKeyPressed(m_keyboard.getKey("RIGHT"))) {
			body.applyAngularImpulse(-0.01f);
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

}
