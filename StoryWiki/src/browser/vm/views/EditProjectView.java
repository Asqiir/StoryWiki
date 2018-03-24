package browser.vm.views;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.*;
import core.*;

public class EditProjectView extends EditView<Project> {
	JTextField input = new JTextField(20);
	
	public EditProjectView(String name, ActionListener saveAndSwapListener, WindowAdapter vcl) {
		super(vcl, saveAndSwapListener);
		
		frame.setSize(300, 120);
		frame.setTitle("Bearbeite Projekt: " + name);
		
		//create an inner layer with borders
		JPanel layer = new JPanel();
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		layer.setLayout(new GridLayout(2,1,10,10));
		
		input.setText(name);
		input.addActionListener(getSaveAndSwapListener());
		
		JButton ready = new JButton("Fertig");
		ready.addActionListener(saveAndSwapListener);
		
		layer.add(input);
		layer.add(ready);
		//==================
		
		frame.add(layer);
		frame.setVisible(true);
	}
	
	@Override
	public Project getEdited() {
		return new Project(input.getText());
	}

}
