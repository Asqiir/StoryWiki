package browser;

import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

import browser.views.View;

public class StartView {
	JFrame frame;
	JFileChooser chooser;
	ActionListener sl;
	
	public StartView(ActionListener sl) {
		this.sl = sl;
		frame = createNewOrLoadFrame(new StartButtonListener(false), new StartButtonListener(true));
		
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}
		};
		
		frame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("ctrl W"), "close");
		frame.getRootPane().getInputMap().put(KeyStroke.getKeyStroke("ctrl Q"), "close");
		frame.getRootPane().getActionMap().put("close", action);
	}
	
	void close() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(false);
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		//window closed
	}
	
	
	class StartButtonListener implements ActionListener { //listens for new/ load button
		boolean isNew;
		
		public StartButtonListener(boolean isn) {
			isNew = isn;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//ask user for file / directory
			String dir = askUser(isNew);
			//create event to tell it the manager
			StartEvent se = new StartEvent(this, ActionEvent.ACTION_PERFORMED, "");
			se.setDirectory(dir);
			se.setIsNew(isNew);
			
			//send
			sl.actionPerformed(se);
		}
		
	}
	
	private String askUser(boolean isNew) {
		if(isNew) {
			return askUserForDirectory();
		}
		return askUserForFile();
	}
	
	private JFrame createNewOrLoadFrame(StartButtonListener loadPButtonListener, StartButtonListener newPButtonListener) {
		JFrame f = new JFrame("StoryWikiBrowser");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300,200);
		
		JButton newPButton = new JButton("Neues Projekt erstellen");
		JButton loadPButton = new JButton("Projekt laden");
		
		newPButton.setFont(View.DEFAULT_FONT);
		loadPButton.setFont(View.DEFAULT_FONT);
		
		newPButton.addActionListener(newPButtonListener);
		loadPButton.addActionListener(loadPButtonListener);
		
		JPanel layer = new JPanel();
		layer.setBorder(BorderFactory.createEmptyBorder(View.BIG_BORDER,View.BIG_BORDER,View.BIG_BORDER,View.BIG_BORDER));
		layer.setLayout(new GridLayout(2,1,View.BIG_BORDER,View.BIG_BORDER));
		
		layer.add(newPButton);
		layer.add(loadPButton);
		
		f.add(layer);
		f.setVisible(true);
		
		return f;
	}

	
	private String askUserForDirectory() {
		JFileChooser fc = new JFileChooser();
		fc.setFont(View.DEFAULT_FONT);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		//now, ask!
		int returnval = fc.showSaveDialog(frame);
		
		if(returnval == JFileChooser.APPROVE_OPTION && fc.getSelectedFile().isDirectory()) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		System.out.println("error in getDirectory");
		return "";
	}
	
	private String askUserForFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		//asking the user
		int returnval = fc.showOpenDialog(frame);
		
		if(returnval == JFileChooser.APPROVE_OPTION && fc.getSelectedFile().isFile()) {
			return fc.getSelectedFile().getAbsolutePath();
		}
		System.out.println("error in getDirectory");
		return "";
	}
}
