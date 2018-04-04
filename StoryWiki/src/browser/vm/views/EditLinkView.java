package browser.vm.views;

import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import core.*;

public class EditLinkView extends EditView<Link> {
	private JTextArea input = new JTextArea();

	public EditLinkView(WindowAdapter vcl, ActionListener saveAndSwapListener, Link link) {
		super(vcl, saveAndSwapListener);

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
		
		frame.setVisible(true);
	}

	@Override
	public Map<String, String> getInput() {
		Map<String, String> input = new HashMap<String, String>();
		input.put("description", this.input.getText());
		
		return input;
	}

	@Override
	public void mark(List<String> inValidKeys) {
		//nothing to mark over here; because there are no conditions for description which is the only changeable thing here
	}
}
