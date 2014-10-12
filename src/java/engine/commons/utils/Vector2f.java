package engine.commons.utils;

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

	public Vector2f add(Vector2f vec) {
		return new Vector2f(x + vec.x, y + vec.y);
	}

	public Vector2f sub(Vector2f vec) {
		return new Vector2f(x - vec.x, y - vec.y);
	}

	public Vector2f mul(float f) {
		return new Vector2f(x * f, y * f);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float angle() {
		return (float) Math.atan2(y, x);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
