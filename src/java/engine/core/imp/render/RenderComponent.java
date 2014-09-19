package engine.core.imp.render;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.Component;
import engine.core.imp.physics.Vector;

public class RenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("position"));
	private GraphicsContext m_context;

	public RenderComponent(GraphicsContext context) {
		m_context = context;
	}

	@Override
	public void update(float time) {
		Vector position = (Vector) getData("position");
		System.out.println("Rendering: " + position.y);
		m_context.graphics.drawOval((int) (position.x - 5), (int) (-position.y - 5), 10, 10);
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
