package exe;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import model.Welt;

public class WC_MainView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Welt w;

	public WC_MainView(Welt w) {

		this.w = w;

		setVisible(true);
		setDoubleBuffered(true);

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.translate(getWidth() / 2, getHeight() / 2);
		w.draw(g2d);
		g2d.translate(-getWidth() / 2, -getHeight() / 2);

	}

}
