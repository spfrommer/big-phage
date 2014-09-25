package engine.core.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.FieldInitializer;
import engine.core.imp.physics.Vector2f;
import engine.core.imp.render.lwjgl.MaterialFactory;

public class SystemFieldInitializer implements FieldInitializer {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material", "sys_dimensions", "sys_particles", "sys_particlesize"));

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
		if (identifier.equals("sys_dimensions"))
			return new Vector2f(1, 1);
		if (identifier.equals("sys_particles"))
			return new ArrayList<Vector2f>();
		if (identifier.equals("sys_particlesize"))
			return 0.1;

		return null;
	}
}
