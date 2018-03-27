package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public abstract class View<MODEL> {
	protected final JFrame frame = new JFrame();
	protected final JPanel layer = new JPanel();
	private WindowListener vcl;
	private List<ActionListener> ctrlQListener = new ArrayList<ActionListener>();
	
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
		
		Action closeApplication = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for(ActionListener cql:ctrlQListener) {
					cql.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ctrl + Q pressed. closing windows now."));
				}
			}
		};
		
		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Q"), "close all frames");
		layer.getActionMap().put("close all frames", closeApplication);
	}
	
	public void addCtrlQListener(ActionListener ctrlQListener) {
		if(!this.ctrlQListener.contains(ctrlQListener)) {
			this.ctrlQListener.add(ctrlQListener);
		}
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
