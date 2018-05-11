package browser.vm.views;

import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import core.Link;
import core.Searchable;

public class ShowSView<MODEL extends Searchable<MODEL>> extends View<MODEL> {
	private JComponent titleComp;
	private final JTextArea timeArea = new JTextArea();
	private final JTextArea descArea = new JTextArea();
	
	private Searchable<?> model;
	
	public ShowSView(WindowAdapter vcl, MODEL model, ActionListener swapListener) {
		super(vcl, model);
		this.model = model;
		
		JTextArea jta = new JTextArea();
		jta.setEditable(false);
		
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		jta.setOpaque(false);
		
		drawFrame(swapListener, jta);
	}

	public ShowSView(WindowAdapter vcl, MODEL model, ActionListener swapListener, ActionListener titleListener) {
		super(vcl, model);
		this.model = model;
		
		JButton titleButton = new JButton();
		titleButton.addActionListener(titleListener);
		
		drawFrame(swapListener, titleButton);
	}
	
	private void drawFrame(ActionListener swapListener, JComponent titleComponent) {
		titleComp = titleComponent;
		
		titleComp.setFont(TITLE_FONT);
		timeArea.setFont(DEFAULT_FONT);
		descArea.setFont(DEFAULT_FONT);
		
		timeArea.setEditable(false);
		descArea.setEditable(false);
		
		timeArea.setLineWrap(true);
		timeArea.setWrapStyleWord(true);
		timeArea.setOpaque(false);
		
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		
		update();
		
		JScrollPane center = new JScrollPane();
		center.setViewportView(descArea);
		center.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JButton south = new JButton("Weiter");
		south.setFont(DEFAULT_FONT);
		south.addActionListener(swapListener);
		
		GridBagConstraints titleC = new GridBagConstraints();
		titleC.fill = GridBagConstraints.HORIZONTAL;
		titleC.weightx = 1;
		titleC.weighty = 0;
		
		GridBagConstraints timeC = new GridBagConstraints();
		timeC.gridy = 1;
		timeC.fill = GridBagConstraints.HORIZONTAL;
		timeC.weightx = 1;
		timeC.weighty = 0;
		timeC.insets = new Insets(SMALL_BORDER,0,0,0);
		
		GridBagConstraints centerC = new GridBagConstraints();
		centerC.gridy = 2;
		centerC.fill = GridBagConstraints.BOTH;
		centerC.weightx = 1;
		centerC.weighty = 1;
		centerC.insets = new Insets(BIG_BORDER,0,BIG_BORDER,0);
		
		GridBagConstraints southC = new GridBagConstraints();
		southC.gridy = 3;
		southC.fill = GridBagConstraints.HORIZONTAL;
		southC.weightx = 1;
		southC.weighty = 0;
		
		layer.setLayout(new GridBagLayout());
		layer.add(titleComponent, titleC);
		layer.add(timeArea, timeC);
		layer.add(center, centerC);
		layer.add(south, southC);
		
		frame.setSize(300, 400);
		frame.setVisible(true);
	}
	
	@Override
	public void update() {
		setHeader(getPrefix() + " :: " + model.getIdentifier());
		frame.setTitle(getPrefix() + " :: " + model.getIdentifier());
		
		if(model.getValidFrom() != null) {
			String dateText = model.getValidFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " - " + Searchable.getValidUntil(model).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			timeArea.setText(dateText);
		} else {
			timeArea.setText("");
		}
		
		descArea.setText(model.getDescription());
	}

	private void setHeader(String header) {
		if(titleComp instanceof JButton) {
			((JButton) titleComp).setText(header);
		}
		if(titleComp instanceof JTextArea) {
			((JTextArea) titleComp).setText(header);
		}
	}
	
	private String getPrefix() {
		if(model instanceof Link) {
			return "Link zu";
		} else {
			return model.getType().showName();
		}
	}
}
