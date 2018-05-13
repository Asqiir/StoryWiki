package browser.views;

import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import java.awt.*;
import core.*;

public class EditGroupView extends EditView<Group> {
	private JTextField rename = new JTextField();

	public EditGroupView(WindowAdapter vcl, ActionListener saveAndSwapListener, String title) {
		super(vcl, saveAndSwapListener);
		
		frame.setSize(300, 120);
		frame.setTitle("Gruppe " + title + " umbenennen");
		
		layer.setLayout(new GridLayout(2,1,10,10));
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JButton finished = new JButton("Fertig");
		finished.addActionListener(saveAndSwapListener);
		
		rename.setText(title);
		rename.addActionListener(saveAndSwapListener);
		
		layer.add(rename);
		layer.add(finished);
		
		frame.setVisible(true);
	}

	@Override
	public Map<String, String> getInput() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("name", rename.getText());

		return input;
	}

	@Override
	public void mark(List<String> inValidKeys) {
		rename.setForeground(Color.BLACK);
		
		if(inValidKeys.contains("name")) {
			rename.setForeground(Color.RED);
		}
	}
}
