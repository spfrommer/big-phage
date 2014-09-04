package engine.core.imp.render;

import java.awt.geom.Point2D;

import engine.core.framework.Component;

public class RenderComponent extends Component {
	private static String[] required = new String[] { "position" };

	@Override
	public void update(float time) {
		System.out.println("Updating renderer with pos: " + getData("position"));
	}

	@Override
	public String[] getRequiredData() {
		return required;
	}

	@Override
	public Object createObjectFor(String identifier) {
		return new Point2D.Double(0, 0);
	}
}
