package browser.vm.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import core.*;

public class EditEntityView extends EditView<Entity> {
	private JTextField nameField = new JTextField();
	private JTextArea descArea = new JTextArea();
	private JComboBox<String> typeDropDown = new JComboBox<String>();
	
	public EditEntityView(WindowAdapter vcl, ActionListener sasl, Entity entity) {
		super(vcl, sasl);
		
		//first line: name
		nameField.setText(entity.get().getName());
		nameField.setFont(new Font("Ubuntu", Font.BOLD, 16));
		nameField.setMaximumSize(new Dimension(1000000,200));
		
		//dropdown
		typeDropDown.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		for(Entity.Types element:Entity.Types.values()) {
			typeDropDown.addItem(element.showName());
		}
		typeDropDown.setSelectedItem(entity.getType());
		
		//datepicker
		
		
		
		//second line: text
		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
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
		
		//frame
		frame.setTitle(entity.get().getName());
		frame.setSize(300, 500);
		
		layer.setLayout(new GridBagLayout());
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		GridBagConstraints cNameField = new GridBagConstraints();
		cNameField.gridy = 0;
		cNameField.weightx = 1;
		cNameField.weighty = 0;
		cNameField.fill = GridBagConstraints.HORIZONTAL;
		
		GridBagConstraints cDropDown = new GridBagConstraints();
		cDropDown.gridy = 1;
		cDropDown.weightx = 1;
		cDropDown.weighty = 0;
		cDropDown.fill = GridBagConstraints.HORIZONTAL;
		
		//cDatePicker here
		
		GridBagConstraints cScroller = new GridBagConstraints();
		cScroller.gridy = 2;
		cScroller.weightx = 1;
		cScroller.weighty = 1;
		cScroller.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints cFinished = new GridBagConstraints();
		cFinished.gridy = 3;
		cFinished.weightx = 1;
		cFinished.weighty = 0;
		cFinished.fill = GridBagConstraints.HORIZONTAL;
		
		layer.add(nameField, cNameField);
		layer.add(typeDropDown, cDropDown);
		layer.add(scroller, cScroller);
		layer.add(finishedEdit, cFinished);
		
		
		frame.setVisible(true);
	}
	
	@Override
	public Entity getEdited() {
		Entity e = new Entity(nameField.getText(), Entity.Types.getByValue((String) typeDropDown.getSelectedItem()));
		e.setDescription(descArea.getText());
		return e;
	}

}
