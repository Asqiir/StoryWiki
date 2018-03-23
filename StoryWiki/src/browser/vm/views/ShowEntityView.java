package browser.vm.views;

import javax.swing.*;
import java.awt.event.*;
import core.Entity;

public class ShowEntityView extends ShowView<Entity> {
	JLabel nameLabel;
	JTextArea descArea;
	
	public ShowEntityView(ActionListener swapListener, WindowAdapter vcl, Entity ess) { //TODO: newLinkListener
		super(vcl);
		
		frame.setTitle(ess.get().getName());
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setSize(200, 400);
		
		//first row
		nameLabel = new JLabel(ess.get().getName());
		//2 row
		descArea = new JTextArea(ess.get().getDescription());
		descArea.setEditable(false);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);

		//12 row
		JButton edit = new JButton("Ändern");
		edit.addActionListener(swapListener);
		
		/* 3-5 row: new, alter, delete link
		 * 6 row: all links
		 * 7-9 row: new, alter, delte group
		 * 10 row: all groups
		 */
		
		frame.add(nameLabel);
		frame.add(descArea);
		frame.add(edit);
		
		frame.setVisible(true);
	}

	@Override
	public void set(Entity ess) {
		nameLabel.setText(ess.getName());
		descArea.setText(ess.getDescription());
	}

}
