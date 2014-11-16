package test.robot;

public class MathUtils {
	private MathUtils() {

	}

	/**
	 * Returns 1 if i is even and -1 otherwise.
	 * 
	 * @param i
	 * @return
	 */
	public static int changeSign(int i) {
		if (i % 2 == 0)
			return 1;
		return -1;
	}
}
