package browser;

import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class StartView {
	JFrame frame;
	JFileChooser chooser;
	StartManager.StartListener sl;
	
	public StartView(StartManager.StartListener sl) {
		this.sl = sl;
		frame = createNewOrLoadFrame(new StartButtonListener(false), new StartButtonListener(true));
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
			try {
				sl.actionPerformed(se);
			} catch (IOException ioe) {
				System.out.println("error while sending startevent to startlistener");
				ioe.printStackTrace();
			}
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
		
		newPButton.addActionListener(newPButtonListener);
		loadPButton.addActionListener(loadPButtonListener);
		
		f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
		
		f.getContentPane().add(newPButton);
		f.getContentPane().add(loadPButton);
		
		f.setVisible(true);
		
		return f;
	}

	
	private String askUserForDirectory() {
		JFileChooser fc = new JFileChooser();
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
