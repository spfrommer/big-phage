package engine.core.imp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jbox2d.dynamics.Body;

import engine.commons.utils.Vector2f;
import engine.core.frame.FieldInitializer;
import engine.core.imp.group.TagList;
import engine.core.imp.physics.liquid.Liquid;
import engine.core.imp.render.MaterialFactory;
import glextra.material.Material;
import glextra.renderer.Light.PointLight;
import gltools.texture.Color;
import gltools.vector.Vector3f;

public class SystemFieldInitializer implements FieldInitializer {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position", "sys_rotation",
			"sys_material", "sys_dimensions", "sys_body", "sys_liquid", "sys_fountainPosition", "sys_lights",
			"sys_frames", "sys_timePerFrame", "sys_repeatAnimation", "sys_groups", "sys_camPosition", "sys_camScale",
			"sys_camRotation", "sys_paralaxDepth", "sys_repeatMaterial", "sys_repeatCount"));

	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}

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
			return new Liquid(new ArrayList<Body>(), 0.1f, 0.03f, new Vector2f(0f, 0f), 10f);
		if (identifier.equals("sys_fountainPosition"))
			return new Vector2f(0f, 0f);
		if (identifier.equals("sys_lights"))
			return Arrays.asList(new PointLight(new Vector3f(0f, 0f, 3f), new Vector3f(0f, 0f, 0.05f), new Color(1f,
					1f, 1f), new Color(0.1f, 0.1f, 0.1f, 0.1f)));
		if (identifier.equals("sys_frames"))
			return new ArrayList<Material>();
		if (identifier.equals("sys_timePerFrame"))
			return 1f / 60f;
		if (identifier.equals("sys_repeatAnimation"))
			return false;
		if (identifier.equals("sys_groups"))
			return new TagList();
		if (identifier.equals("sys_camPosition"))
			return new Vector2f(0f, 0f);
		if (identifier.equals("sys_camScale"))
			return new Vector2f(1f, 1f);
		if (identifier.equals("sys_camRotation"))
			return 0f;
		if (identifier.equals("sys_paralaxDepth"))
			return 0f;
		if (identifier.equals("sys_repeatMaterial"))
			return false;
		if (identifier.equals("sys_repeatCount"))
			return 1f;

		return null;
	}
}
