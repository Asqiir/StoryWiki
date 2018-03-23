package browser.vm.views;

import java.awt.event.*;
import javax.swing.JButton;
import browser.vm.OpenViewEvent;
import browser.vm.ProjectController.OpenViewListener;

public class OpenButton<INNER> extends JButton {
	private INNER open;
	
	public OpenButton(String title, INNER open, ActionListener listener) {
		super(title);
		this.open = open;
		addActionListener(listener);
	}
	
	public INNER getOpen() {
		return open;
	}
	
	
}
