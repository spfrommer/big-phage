package test.robot;

/**
 * A 2d Vector adapted from another project of mine.
 */
public class MVector extends Matrix {
	public MVector(float x, float y) {
		super(2, 1, new float[] { x, y });
	}

	/**
	 * @return the x value of the Vector
	 */
	public float getX() {
		return this.getVal(0, 0);
	}

	/**
	 * @return the y value of the Vector
	 */
	public float getY() {
		return this.getVal(1, 0);
	}

	@Override
	public String toString() {
		return "[" + getX() + ", " + getY() + "]";
	}
}