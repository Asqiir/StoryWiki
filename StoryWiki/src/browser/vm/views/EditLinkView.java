package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import core.*;

public class EditLinkView extends EditView<Link> {
	private JPanel layer = new JPanel();
	private JTextArea input = new JTextArea();
	private Entity linkedTo;

	public EditLinkView(WindowAdapter vcl, ActionListener saveAndSwapListener, Link link) {
		super(vcl, saveAndSwapListener);

		linkedTo = link.getEntity();
		
		frame.setSize(300, 400);

		layer.setLayout(new BorderLayout(10,10));
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel name = new JLabel("zu " + link.getIdentifier());
		name.setFont(new Font("Ubuntu", Font.BOLD, 16));
		
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setViewportView(input);
		
		JButton edit = new JButton("Fertig");
		edit.addActionListener(saveAndSwapListener);
		
		layer.add(name, BorderLayout.NORTH);
		layer.add(scroller, BorderLayout.CENTER);
		layer.add(edit, BorderLayout.SOUTH);
		
		frame.add(layer);
		frame.setVisible(true);
	}
	
	protected JPanel getLayer() {
		return layer;
	}

	@Override
	public Link getEdited() {
		return new Link(linkedTo, input.getText());
	}

}
