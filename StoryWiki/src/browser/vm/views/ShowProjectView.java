package browser.vm.views;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import core.Entity;
import core.Project;

public class ShowProjectView extends ShowView<Project> {	
	protected JLabel header;
	protected JLabel showNumber;
	
	protected JList showAllEntities;
	
	protected JTextField renameField = new JTextField();
	protected JTextField openAndShowField = new JTextField();
	
	public ShowProjectView(String name, int number, String[] allEntityOptions, ActionListener changeViewListener, WindowAdapter vcl, ActionListener createAndShowEntityListener, ActionListener deleteEntityListener, ActionListener allEntitiesListener) {
		super(vcl);
		frame.setTitle(name);
		
		JPanel layer = new JPanel();
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		layer.setLayout(new BoxLayout(layer, BoxLayout.PAGE_AXIS));
		
		//first row: name and edit button
		JPanel first = new JPanel();
		first.setLayout(new BoxLayout(first, BoxLayout.LINE_AXIS));
		
		header = new JLabel(name);
		header.setFont(new Font("Ubuntu", Font.BOLD, 26));
		
		JButton edit = new JButton("Editieren");
		edit.addActionListener(changeViewListener);
		
		first.add(header);
		first.add(Box.createHorizontalGlue());
		first.add(edit);
		
		//second row: all entities button, which is header
		showNumber = new JLabel(number + " Entities");
		showNumber.setFont(new Font("Ubuntu", Font.BOLD, 18));
		//TODO: action-listener hinzufügen; in Button umwandeln
		
		//third row: Jlist for all entities
		JScrollPane listScroll = new JScrollPane();
		showAllEntities = new JList(allEntityOptions);
		showAllEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroll.setViewportView(showAllEntities);
		listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		//4. - 6. grid layer
		JPanel gridLayer = new JPanel();
		gridLayer.setLayout(new GridLayout(2,2,10,10));
		
		//fourth row: button open and button delete
		JButton open = new JButton("Öffnen");
		open.addActionListener(createAndShowEntityListener);
		JButton delete = new JButton("Löschen");
		delete.addActionListener(deleteEntityListener);
		gridLayer.add(open);
		gridLayer.add(delete);
				
		//fifth row: "new entity" and textfield
		JLabel newE = new JLabel("(Erstelle &) Zeige:");
		openAndShowField.addActionListener(createAndShowEntityListener);
		gridLayer.add(newE);
		gridLayer.add(openAndShowField);
		
		layer.add(first);
		layer.add(Box.createRigidArea(new Dimension(0, 10)));
		layer.add(showNumber);
		layer.add(Box.createRigidArea(new Dimension(0, 10)));
		layer.add(listScroll);
		layer.add(Box.createRigidArea(new Dimension(0, 10)));
		layer.add(gridLayer);
		
		frame.add(layer);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
	
	protected void setNumber(int number) {
		showNumber.setText(number + " Entities");
	}

	@Override
	public void set(Project projShot) {
		header.setText(projShot.getName());
		setNumber(projShot.getAll().size());
		frame.setTitle(projShot.getName());
		
		//get all names, convert to String[]
		List<Entity> eList = projShot.getEntities();
		List<String> nameList = new ArrayList<String>();
		
		for(Entity element:eList) {
			nameList.add(element.getIdentifier());
		}
		
		String[] entityNames = nameList.toArray(new String[nameList.size()]);
		//================
		
		showAllEntities.setListData(entityNames);
	}

	public String getSelected() {
		return (String) showAllEntities.getSelectedValue();
	}
	
	public String sendCreateAndShowEntityInput() {
		String input = openAndShowField.getText();
		
		if(input.trim().length() > 0) {	
			openAndShowField.setText("");
			return input;
		}
		
		if(!showAllEntities.isSelectionEmpty()) {
			return getSelected();
		}
		
		return null;
	}
}
