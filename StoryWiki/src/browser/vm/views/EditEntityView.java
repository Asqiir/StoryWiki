package browser.vm.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.*;

public class EditEntityView extends EditView<Entity> {
	private JTextField nameField = new JTextField();
	private JComboBox<String> typeDropDown = new JComboBox<String>();
	private JTextField from = new JTextField();
	private JTextField until = new JTextField();
	private JTextArea descArea = new JTextArea();
	
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
		from.setText(createValidDateStringOf(entity.getValidFrom()));
		
		if(entity.getValidFrom() != null) {
			until.setText(createValidDateStringOf(Searchable.getValidUntil(entity)));			
		} else {
			until.setText("02.01.2000");
		}
		
		
		JPanel datePanel = new JPanel();
		
		datePanel.setBorder(new TitledBorder("Von - bis [tt.mm.jjjj]"));
		datePanel.setLayout(new GridLayout(2,1,10,10));
		
		datePanel.add(from);
		datePanel.add(until);
		
		//second line: text
		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
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
		
		GridBagConstraints cDatePanel = new GridBagConstraints();
		cDatePanel.gridy = 2;
		cDatePanel.weightx = 1;
		cDatePanel.weighty = 0;
		cDatePanel.fill = GridBagConstraints.HORIZONTAL;
		
		GridBagConstraints cScroller = new GridBagConstraints();
		cScroller.gridy = 3;
		cScroller.weightx = 1;
		cScroller.weighty = 1;
		cScroller.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints cFinished = new GridBagConstraints();
		cFinished.gridy = 4;
		cFinished.weightx = 1;
		cFinished.weighty = 0;
		cFinished.fill = GridBagConstraints.HORIZONTAL;
		
		layer.add(nameField, cNameField);
		layer.add(typeDropDown, cDropDown);
		layer.add(datePanel, cDatePanel);
		layer.add(scroller, cScroller);
		layer.add(finishedEdit, cFinished);
		
		frame.setVisible(true);
	}
	
	private String createValidDateStringOf(LocalDate date) {
		if(date != null) {
			return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		}
		return "01.01.2000";
	}

	@Override
	public Map<String, String> getInput() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("id", nameField.getText());
		input.put("description", descArea.getText());
		input.put("type", (String) typeDropDown.getSelectedItem());
		input.put("date from", from.getText());
		input.put("date until", until.getText());
		
		return input;
	}

	@Override
	public void mark(List<String> inValidKeys) {
		//remove any mark before
		nameField.setForeground(Color.BLACK);
		descArea.setForeground(Color.BLACK);
		typeDropDown.setForeground(Color.BLACK);
		from.setForeground(Color.BLACK);
		until.setForeground(Color.BLACK);
		
		if(inValidKeys.contains("id")) {
			nameField.setForeground(Color.RED);
		}
		if(inValidKeys.contains("description")) {
			descArea.setForeground(Color.RED);
		}
		if(inValidKeys.contains("type")) {
			typeDropDown.setForeground(Color.RED);
		}
		if(inValidKeys.contains("date from")) {
			from.setForeground(Color.RED);
		}
		if(inValidKeys.contains("date until")) {
			until.setForeground(Color.RED);
		}
	}
}
