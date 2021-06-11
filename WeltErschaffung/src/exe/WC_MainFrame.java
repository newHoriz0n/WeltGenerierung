package exe;

import javax.swing.JFrame;

import model.Welt;

public class WC_MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WC_MainFrame() {
		
		Welt w = new Welt();
				
		WC_MainView v = new WC_MainView(w);
		add(v);
		
		w.setView(v);
		
		setVisible(true);
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public static void main(String [] args) {
		WC_MainFrame wcf = new WC_MainFrame();
		wcf.requestFocus();
	}
	
}
