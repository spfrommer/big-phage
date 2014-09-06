package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.Component;
import engine.core.imp.physics.Vector;

public class RenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("position"));

	@Override
	public void update(float time) {
		// System.out.println("Updating renderer with pos: " + getData("position"));
	}

	@Override
	public Set<String> getRequiredIdentifiers() {
		return IDENTIFIERS;
	}

	@Override
	public Object createObjectFor(String identifier) {
		if (identifier.equals("position"))
			return new Vector(0, 0);

		return null;
	}
}
