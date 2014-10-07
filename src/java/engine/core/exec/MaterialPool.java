package engine.core.exec;

import glextra.material.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * A class for pooling materials since they're too slow to create in game.
 */
public class MaterialPool {
	public static Map<String, Material> materials = new HashMap<String, Material>();

	private MaterialPool() {
	}
}
