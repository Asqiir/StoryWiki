package browser.vm.views;

import javax.swing.*;
import java.awt.event.*;
import core.*;
import core.Entity.Types;

public class EditEntityView extends EditView<Entity> {
	JLabel nameField;
	JTextArea descArea;
	
	public EditEntityView(WindowAdapter vcl, ActionListener sasl, Entity entitySnapShot) {
		super(vcl, sasl);
		
		frame.setTitle(entitySnapShot.get().getName());
		frame.setSize(200, 400);
		
		//first line: name //FIXME: Type editing
		nameField = new JLabel(entitySnapShot.get().getName());
		//second line: desc
		descArea = new JTextArea(entitySnapShot.get().getDescription());
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		//TODO: JScrollPane instead of TextArea
		
		JButton commit = new JButton("Fertig");
		
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.add(nameField);
		frame.add(descArea);
		frame.add(commit);
		
		commit.addActionListener(getSaveAndSwapListener());
		
		frame.setVisible(true);
		
	}

	@Override
	public Entity getEdited() {
		Entity e = new Entity(nameField.getText(), Types.NOTE);
		e.setDescription(descArea.getText());
		return e;
	}

}
