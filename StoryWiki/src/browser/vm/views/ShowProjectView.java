package browser.vm.views;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.html.parser.Entity;

import core.Project;

public class ShowProjectView extends ShowView<Project> {	
	protected JLabel nameLabel;
	protected JLabel numberLabel;
	protected final JPanel showAll = new JPanel();
	
	public ShowProjectView(String name, int number, ActionListener changeViewListener, WindowAdapter vcl, ActionListener createAndShowEntityListener, ActionListener deleteEntityListener, ActionListener allEntitiesListener) {
		super(vcl);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setTitle(name);
		
		//First row; contains name and button to edit it
		JPanel nameRow = new JPanel();
		nameRow.setLayout(new BorderLayout());
		nameLabel = new JLabel(name);
		nameRow.add(BorderLayout.WEST, nameLabel);
		JButton editNameButton = new JButton("edit");
		editNameButton.addActionListener(changeViewListener);
		nameRow.add(BorderLayout.EAST, editNameButton);
		
		//second row; contains number of entities by now
		JPanel numberRow = new JPanel();
		numberLabel = new JLabel(Integer.toString(number));
		numberRow.add(numberLabel);
		numberRow.add(new JLabel("Entities"));
		
		//third row; seeing all entities TODO: make this a scrollpane
		showAll.setLayout(new BoxLayout(showAll, BoxLayout.Y_AXIS));
		
		//forth row; creating new entities
		JPanel newE = new JPanel();
		JLabel tNew = new JLabel("Zeige:");
		JTextField newInput = new JTextField(10);
		newInput.addActionListener(createAndShowEntityListener);
		newE.add(tNew);
		newE.add(newInput);
		
		//5 row to delete an entity
		JPanel deleteRow = new JPanel();
		deleteRow.add(new JLabel("Lösche:"));
		JTextField deleteEntity = new JTextField(10);
		deleteRow.add(deleteEntity);
		deleteEntity.addActionListener(deleteEntityListener);
		
		// 6 row to get all entities
		JButton allE = new JButton("Alle Entities");
		allE.addActionListener(allEntitiesListener);
		
		//TODO: 7 row to close
		
		frame.add(nameRow);
		frame.add(numberRow);
		frame.add(showAll);
		frame.add(newE);
		frame.add(deleteRow);
		frame.add(allE);
		
		frame.setSize(200, 400);
		frame.setVisible(true);
	}

	@Override
	public void set(Project projShot) {
		nameLabel.setText(projShot.getName());
		numberLabel.setText(Integer.toString(projShot.getAll().size()));
		frame.setTitle(projShot.getName());
		
		showAll.removeAll();
		
		for(core.Entity e:projShot.getEntities()) {
			showAll.add(new JLabel(e.getName()));
		}

	}
}
