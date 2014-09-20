package engine.core.imp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.FieldInitializer;
import engine.core.imp.physics.Vector2f;
import engine.core.imp.render.lwjgl.MaterialFactory;

public class SystemFieldInitializer implements FieldInitializer {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material"));

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object createObjectFor(String identifier) {
		if (identifier.equals("sys_position"))
			return new Vector2f(0, 0);
		if (identifier.equals("sys_rotation"))
			return 0;
		if (identifier.equals("sys_material"))
			return MaterialFactory.createBasicMaterial();
		return null;
	}
}
