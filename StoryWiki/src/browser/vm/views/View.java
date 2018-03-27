package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;

public abstract class View<MODEL> {
	protected final JFrame frame = new JFrame();
	protected final JPanel layer = new JPanel();
	private WindowListener vcl;
	
	public View(WindowAdapter vcl) {
		frame.addWindowListener(vcl);
		this.vcl = vcl; //remember, to delete later
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Action closeFrame = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		};
		
		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control W"), "close frame");
		layer.getActionMap().put("close frame", closeFrame);
	}
	
	public void close() {
		//commits event that says window is going to be closed.
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public void removeCloseListener() { //removing is only meant to use when window is going to be closed; when vm shouldn't be stopped.
		frame.removeWindowListener(vcl);
		vcl = null;
	}

	public void repaint() {
		frame.validate();
		frame.repaint();
	}
}
