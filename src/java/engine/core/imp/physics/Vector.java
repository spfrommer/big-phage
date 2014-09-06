package engine.core.imp.physics;

public class Vector {
	public double x;
	public double y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
