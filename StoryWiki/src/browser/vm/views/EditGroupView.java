package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import core.*;

public class EditGroupView extends EditView<Group> {
	private JTextField rename = new JTextField();

	public EditGroupView(WindowAdapter vcl, ActionListener saveAndSwapListener, String title) {
		super(vcl, saveAndSwapListener);
		
		frame.setSize(300, 120);
		frame.setTitle("Gruppe " + title + " umbenennen");
		
		JPanel layer = new JPanel();
		layer.setLayout(new GridLayout(2,1,10,10));
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JButton finished = new JButton("Fertig");
		finished.addActionListener(saveAndSwapListener);
		
		rename.setText(title);
		rename.addActionListener(saveAndSwapListener);
		
		layer.add(rename);
		layer.add(finished);
		
		frame.add(layer);
		frame.setVisible(true);
	}

	@Override
	public Group getEdited() {
		return new Group(rename.getText());
	}

}
