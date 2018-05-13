package browser.views;

import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public abstract class View<MODEL> {
	//GUI constants
	public static final int BIG_BORDER = 10;
	public static final int SMALL_BORDER = 3;
	
	public static final Font DEFAULT_FONT = new Font("Ubuntu", Font.PLAIN, 12);
	public static final Font TITLE_FONT = new Font("Ubuntu", Font.BOLD, 26);
	public static final Font SUBTITLE_FONT = new Font("Ubuntu", Font.BOLD, 16);
	
	class MyFrame extends JFrame { //needed to make sure getContentPane() returns a JPanel
		public MyFrame(JComponent layer) {
			setContentPane(layer);
		}
	}
	
	protected final JFrame frame = new MyFrame(new JPanel());
	protected final JPanel layer = (JPanel) frame.getContentPane();
	private WindowListener vcl;
	private List<ActionListener> ctrlQListener = new ArrayList<ActionListener>();
	
	private MODEL model;
	
	public View(WindowAdapter vcl, MODEL model) {
		frame.addWindowListener(vcl);
		this.vcl = vcl; //remember, to delete later
		this.model = model;
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setFont(DEFAULT_FONT);
		layer.setBorder(BorderFactory.createEmptyBorder(BIG_BORDER, BIG_BORDER, BIG_BORDER, BIG_BORDER));
		
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
	
	public abstract void update();
	
	public void repaint() {
		frame.validate();
		frame.repaint();
	}

	protected MODEL getModel() {
		return model;
	}
}
