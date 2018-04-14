package browser.vm.views;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import core.Entity;
import core.Project;

public class ShowProjectView extends ShowView<Project> {
	protected JLabel header;
	protected JLabel showNumber;
	
	protected JList<String> showAllEntities;
	
	protected JTextField renameField = new JTextField();
	protected JTextField openAndShowField = new JTextField();
	
	protected JTextField linkToField = new JTextField();
	protected JTextField unlinkField = new JTextField();
	
	public ShowProjectView(String name, int number, String[] allEntityOptions, ActionListener changeViewListener, WindowAdapter vcl, ActionListener createAndShowEntityListener,
			ActionListener deleteEntityListener, ActionListener linkListener, ActionListener unlinkListener) {
		
		super(vcl);

		frame.setTitle(name);
		layer.setLayout(new BorderLayout(10,10));
		
		//NORTH
		JPanel northern = new JPanel();
		northern.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		northern.setLayout(new BorderLayout(10,10));

		JPanel headLine = new JPanel();
		headLine.setLayout(new BoxLayout(headLine, BoxLayout.LINE_AXIS));
		
		header = new JLabel(name);
		header.setFont(new Font("Ubuntu", Font.BOLD, 26));
		
		JButton edit = new JButton("Bearbeiten");
		edit.addActionListener(changeViewListener);
		
		headLine.add(header);
		headLine.add(Box.createHorizontalGlue());
		headLine.add(edit);
		
		showNumber = new JLabel(number + " Entities");
		showNumber.setFont(new Font("Ubuntu", Font.PLAIN, 20));
		showNumber.setHorizontalAlignment(JLabel.CENTER);

		northern.setMinimumSize(new Dimension(header.getPreferredSize().width + edit.getPreferredSize().width, northern.getPreferredSize().height));
		northern.add(headLine, BorderLayout.NORTH);
		northern.add(showNumber, BorderLayout.SOUTH);		

		//CENTER
		JScrollPane listScroll = new JScrollPane();
		listScroll.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		showAllEntities = new JList(allEntityOptions);
		showAllEntities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listScroll.setViewportView(showAllEntities);
		listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		showAllEntities.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()>1) {
					openAndShowField.setText(getSelected());
					linkToField.setText(getSelected());
					unlinkField.setText(getSelected());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {	}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		//SOUTH
		JPanel gridLayer = new JPanel();
		gridLayer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		gridLayer.setLayout(new GridLayout(4,2,10,10));
		
		JButton open = new JButton("Öffnen");
		open.addActionListener(createAndShowEntityListener);
		JButton delete = new JButton("Löschen");
		delete.addActionListener(deleteEntityListener);
		gridLayer.add(open);
		gridLayer.add(delete);
				
		JLabel newE = new JLabel("(Erstelle &) Zeige:");
		openAndShowField.addActionListener(createAndShowEntityListener);
		gridLayer.add(newE);
		gridLayer.add(openAndShowField);
		
		JButton linkTo = new JButton("Verlinke mit:");
		linkTo.addActionListener(linkListener);
		linkToField.addActionListener(linkListener);
		
		gridLayer.add(linkTo);
		gridLayer.add(linkToField);
		
		JButton unlink = new JButton("Trennen");
		unlink.addActionListener(unlinkListener);
		unlinkField.addActionListener(unlinkListener);
		
		gridLayer.add(unlink);
		gridLayer.add(unlinkField);
		
		
		//ADD ALL
		layer.add(northern, BorderLayout.NORTH);
		layer.add(listScroll, BorderLayout.CENTER);
		layer.add(gridLayer, BorderLayout.SOUTH);
		
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

	public String getLinkTo() {
		String content = linkToField.getText();
		linkToField.setText("");
		
		return content;
	}

	public String getUnlink() {
		String unlink = unlinkField.getText();
		unlinkField.setText("");
		return unlink;
	}
}
