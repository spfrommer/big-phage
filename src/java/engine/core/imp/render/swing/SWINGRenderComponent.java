package engine.core.imp.render.swing;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import engine.core.framework.Component;
import engine.core.imp.physics.Vector2f;

public class SWINGRenderComponent extends Component {
	private static final Set<String> IDENTIFIERS = new HashSet<String>(Arrays.asList("sys_position"));
	private GraphicsContext m_context;

	public SWINGRenderComponent(GraphicsContext context) {
		m_context = context;
	}

	@Override
	public void update(float time) {
		Vector2f position = (Vector2f) getData("sys_position");
		System.out.println("Rendering: " + position.y);
		m_context.graphics.drawOval((int) (position.x - 5), (int) (-position.y - 5), 10, 10);
	}

	@Override
	public Set<String> getDataIdentifiers() {
		return IDENTIFIERS;
	}
}
