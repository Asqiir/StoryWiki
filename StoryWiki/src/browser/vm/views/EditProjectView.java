package browser.vm.views;

import javax.swing.*;
import java.awt.event.*;
import browser.vm.*;
import core.*;

public class EditProjectView extends EditView<Project> {
	JTextField input = new JTextField(20);
	
	public EditProjectView(String name, ActionListener saveAndSwapListener, WindowAdapter vcl) {
		super(vcl, saveAndSwapListener);
		
		frame.setSize(200, 50);
		frame.setTitle("Bearbeite Projekt: " + name);
		
		input.setText(name);
		frame.add(input);
		
		input.addActionListener(getSaveAndSwapListener());
		
		frame.setVisible(true);
	}
	
	@Override
	public Project getEdited() {
		return new Project(input.getText());
	}

}
