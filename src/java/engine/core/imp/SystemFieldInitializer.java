package engine.core.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.commons.utils.Vector2f;
import engine.core.frame.FieldInitializer;
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.render.lwjgl.MaterialFactory;

public class SystemFieldInitializer implements FieldInitializer {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material", "sys_dimensions", "sys_body", "sys_liquid"));

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object createObjectFor(String identifier) {
		if (identifier.equals("sys_position"))
			return new Vector2f(0, 0);
		if (identifier.equals("sys_rotation"))
			return 0f;
		if (identifier.equals("sys_material"))
			return MaterialFactory.createBasicMaterial();
		if (identifier.equals("sys_dimensions"))
			return new Vector2f(1, 1);
		if (identifier.equals("sys_body"))
			return null;
		if (identifier.equals("sys_liquid"))
			return new Liquid(new ArrayList<Body>(), 0.1f, 0.03f);

		return null;
	}
}
