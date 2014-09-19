package engine.core.imp.render.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SWINGDisplay extends JFrame {
	private static final long serialVersionUID = 7172173442931126925L;
	private List<Painter> m_painters = new ArrayList<Painter>();

	public SWINGDisplay() {
		this.add(new PainterPanel());
	}

	public void addPainter(Painter painter) {
		m_painters.add(painter);
	}

	private void callPainters(Graphics2D g2d) {
		for (Painter p : m_painters)
			p.paint(g2d);
	}

	private class PainterPanel extends JPanel {
		private static final long serialVersionUID = -3953445173774325531L;

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.translate(100, 100);
			SWINGDisplay.this.callPainters((Graphics2D) g);
		}

		/*@Override
		public Dimension getPreferredSize() {
			return new Dimension(PREF_W, PREF_H); // appropriate constants
		}*/
	}
}
