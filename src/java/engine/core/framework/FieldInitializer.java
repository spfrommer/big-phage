package engine.core.framework;

import java.util.HashSet;
import java.util.Set;

/**
 * A handy interface that specifies how fields in an Entity should be initialized.
 */
public interface FieldInitializer {
	public abstract Set<String> getDataIdentifiers();

	public abstract Object createObjectFor(String identifier);

	public static final FieldInitializer NONE = new FieldInitializer() {
		@Override
		public Set<String> getDataIdentifiers() {
			return new HashSet<String>();
		}

		@Override
		public Object createObjectFor(String identifier) {
			throw new RuntimeException("NONE initializer for: " + identifier);
		}
	};
}
