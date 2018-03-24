package browser.vm.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import core.Entity;

public class ShowEntityView extends ShowView<Entity> {
	JLabel title = new JLabel();
	JTextArea descArea = new JTextArea();
	
	public ShowEntityView(ActionListener swapListener, WindowAdapter vcl, Entity entity) {
		super(vcl);
		
		frame.setTitle(entity.get().getName());
		frame.setSize(400, 400);
		frame.setLayout(new BorderLayout(10,10));
		
		//NORTH
		JPanel header = new JPanel();
		header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
		
		title.setFont(new Font("Ubuntu", Font.BOLD, 26));
		title.setText(entity.getName());
		
		JButton edit = new JButton("Bearbeiten");
		edit.addActionListener(swapListener);
		
		header.add(title);
		header.add(Box.createHorizontalGlue());
		header.add(edit);
		
		//CENTER
		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		descArea.setText(entity.getDescription());
		scroller.setViewportView(descArea);
		
		//SOUTH
		//TODO: Links
		//TODO: Groups
		
		//ADD ALL
		frame.add(header, BorderLayout.NORTH);
		frame.add(scroller, BorderLayout.CENTER);
		
		frame.setVisible(true);
	}

	@Override
	public void set(Entity ess) {
		title.setText(ess.getName());
		descArea.setText(ess.getDescription());
	}

}
