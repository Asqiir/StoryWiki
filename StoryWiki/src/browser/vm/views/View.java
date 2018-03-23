package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;

public class View<MODEL> {
	protected final JFrame frame = new JFrame();
	private WindowListener vcl;
	
	public View(WindowAdapter vcl) {
		frame.addWindowListener(vcl);
		this.vcl = vcl; //remember, to delete later
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
