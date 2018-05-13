package browser.views;

import javax.swing.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.*;

import core.*;

public class EditProjectView extends EditView<Project> {
	private JTextField input = new JTextField(20);
	
	public EditProjectView(String name, ActionListener saveAndSwapListener, WindowAdapter vcl) {
		super(vcl, saveAndSwapListener);
		
		frame.setSize(300, 120);
		frame.setTitle("Bearbeite Projekt: " + name);
		
		//create an inner layer with borders
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		layer.setLayout(new GridLayout(2,1,10,10));
		
		input.setText(name);
		input.addActionListener(saveAndSwapListener);
		input.setFont(DEFAULT_FONT);
		
		JButton swap = new JButton("Weiter");
		swap.addActionListener(saveAndSwapListener);
		swap.setFont(DEFAULT_FONT);
		
		layer.add(input);
		layer.add(swap);
		//==================
		
		frame.setVisible(true);
	}

	@Override
	public Map<String, String> getInput() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("name", this.input.getText());
		return input;
	}

	@Override
	public void mark(List<String> inValidKeys) {
		if(inValidKeys.contains("name")) {
			input.setForeground(Color.RED);
		} else {
			input.setForeground(Color.BLACK);
		}
	}
}
