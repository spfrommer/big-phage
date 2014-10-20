package engine.core.imp.control;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;

import engine.core.exec.GameState;
import engine.core.frame.Component;
import gltools.input.Keyboard;

public class KeyboardControlComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_body"));

	@Override
	public void update(float time, GameState state) {
		Body body = (Body) getData("sys_body");

		Keyboard keyboard = state.keyboard;
		if (keyboard.isKeyPressed(keyboard.getKey('w'))) {
			Vec2 directionVector = new Vec2((float) Math.cos(body.getAngle() + Math.PI / 2), (float) Math.sin(body
					.getAngle() + Math.PI / 2));

			body.applyForceToCenter(directionVector.mul(200f * body.getMass() * time));
		}
		if (keyboard.isKeyPressed(keyboard.getKey('s'))) {
			Vec2 directionVector = new Vec2((float) Math.cos(body.getAngle() + Math.PI / 2), (float) Math.sin(body
					.getAngle() + Math.PI / 2));

			body.applyForceToCenter(directionVector.mul(-200f * body.getMass() * time));
		}
		if (keyboard.isKeyPressed(keyboard.getKey('a'))) {
			body.applyTorque(100f * body.getMass() * time);
		}
		if (keyboard.isKeyPressed(keyboard.getKey('d'))) {
			body.applyTorque(-100f * body.getMass() * time);
		}
		if (keyboard.isKeyPressed(keyboard.getKey("LSHIFT"))) {
			body.applyForceToCenter(body.getLinearVelocity().mul(-100f * body.getMass() * time));
			body.applyTorque(body.getAngularVelocity() * -100f * body.getMass() * time);
		}
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
