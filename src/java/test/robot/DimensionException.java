package test.robot;

public class DimensionException extends RuntimeException {
	private static final long serialVersionUID = 3978034240388639304L;

	public DimensionException() {
		super("Dimensions do not match!");
	}
}
