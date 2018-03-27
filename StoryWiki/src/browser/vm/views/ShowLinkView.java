package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import core.*;

public class ShowLinkView extends ShowView<Link> {
	private JPanel layer = new JPanel();
	private JTextArea description = new JTextArea();

	public ShowLinkView(ActionListener swapListener, ActionListener openEntityListener, WindowAdapter vcl, Link link) {
		super(vcl);

		frame.setSize(300, 400);
		layer.setLayout(new BorderLayout(10,10));
		
		JPanel header = new JPanel();
		header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		header.setLayout(new GridLayout(2,1,10,10));
		
		JButton title = new JButton("Link zu " + link.getIdentifier());
		title.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		JButton edit = new JButton("Bearbeiten");
		
		title.addActionListener(openEntityListener);
		edit.addActionListener(swapListener);
		
		header.add(title);
		header.add(edit);
		
		JScrollPane scroller = new JScrollPane();
		scroller.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		description.setEditable(false);
		scroller.setViewportView(description);
		
		layer.add(header, BorderLayout.NORTH);
		layer.add(scroller, BorderLayout.CENTER);
		
		frame.add(layer);
		
		set(link);
		frame.setVisible(true);
	}

	protected JPanel getLayer() {
		return layer;
	}
	
	@Override
	public void set(Link edit) {
		frame.setTitle("Link zu: " + edit.getIdentifier());
		description.setText(edit.getDescription());
	}

}
