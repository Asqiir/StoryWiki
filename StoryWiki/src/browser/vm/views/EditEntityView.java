package browser.vm.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import core.*;
import core.Entity.Types;

public class EditEntityView extends EditView<Entity> {
	private JTextField nameField = new JTextField();
	private JTextArea descArea = new JTextArea();
	private JComboBox<String> typeDropDown = new JComboBox<String>();
	
	public EditEntityView(WindowAdapter vcl, ActionListener sasl, Entity entity) {
		super(vcl, sasl);
		
		frame.setTitle(entity.get().getName());
		frame.setSize(300, 500);
		
		layer.setLayout(new BorderLayout(10,10));
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//first line: name
		nameField.setText(entity.get().getName());
		nameField.setFont(new Font("Ubuntu", Font.BOLD, 16));
		nameField.setMaximumSize(new Dimension(1000000,200));
		
		//dropdown
		for(Entity.Types element:Entity.Types.values()) {
			typeDropDown.addItem(element.showName());
		}
		
		typeDropDown.setSelectedItem(entity.getType());
		
		//northern grid
		JPanel northernGrid = new JPanel();
		northernGrid.setLayout(new GridLayout(2,1,10,10));
		northernGrid.add(nameField);
		northernGrid.add(typeDropDown);
		
		
		//second line: text
		JScrollPane scroller = new JScrollPane();
		scroller.setViewportView(descArea);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		descArea.setEditable(true);
		descArea.setText(entity.getDescription());
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		
		//last line: finish edit button
		JButton finishedEdit = new JButton("Fertig");
		finishedEdit.addActionListener(sasl);
		
		layer.add(northernGrid, BorderLayout.NORTH);
		layer.add(scroller, BorderLayout.CENTER);
		layer.add(finishedEdit, BorderLayout.SOUTH);
		
		frame.setVisible(true);
	}
	
	@Override
	public Entity getEdited() {
		Entity e = new Entity(nameField.getText(), Entity.Types.getByValue((String) typeDropDown.getSelectedItem())); //TODO: make type changeable
		e.setDescription(descArea.getText());
		return e;
	}

}
