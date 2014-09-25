package engine.core.imp.physics;

import org.jbox2d.common.Vec2;

public class Vector2f {
	public float x;
	public float y;

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(Vec2 v) {
		this.x = v.x;
		this.y = v.y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
