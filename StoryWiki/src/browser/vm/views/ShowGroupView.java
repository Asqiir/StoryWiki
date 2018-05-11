package browser.vm.views;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import core.*;

public class ShowGroupView extends ShowView<Group> {
	private JLabel title = new JLabel();
	private JList<String> links = new JList<String>();
	
	public ShowGroupView(WindowAdapter vcl, ActionListener swapListener, ActionListener openLinkListener, ActionListener removeLinkListener, Group group) {
		super(vcl, group);
		
		//header
		title.setFont(new Font("Ubuntu", Font.BOLD, 26));
		JButton edit = new JButton("Umbenennen");
		edit.addActionListener(swapListener);
		
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
		header.add(title);
		header.add(Box.createHorizontalGlue());
		header.add(edit);
		
		//list
		JScrollPane scroller = new JScrollPane();
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setViewportView(links);
		
		//grid
		JButton open = new JButton("Öffnen");
		JButton remove = new JButton("Entfernen");
		
		open.addActionListener(openLinkListener);
		remove.addActionListener(removeLinkListener);
		
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(1,2,10,10));
		grid.add(open);
		grid.add(remove);
		
		//Layer
		layer.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		layer.setLayout(new BorderLayout(10,10));
		
		layer.add(header, BorderLayout.NORTH);
		layer.add(scroller, BorderLayout.CENTER);
		layer.add(grid, BorderLayout.SOUTH);
		
		set(group);
		
		frame.setSize(300, 400);
		frame.setVisible(true);
	}
	
	public String getSelected() {
		return (String) links.getSelectedValue();
	}
	
	@Override
	public void set(Group group) {
		frame.setTitle("Gruppe: " + group.getIdentifier());
		title.setText(group.getIdentifier());
		
		List<Searchable<Link>> linkList = group.getAll();
		List<String> nameList = new ArrayList<String>();
		
		for(Searchable<Link> l:linkList) {
			String firstPart = l.getDescription().split("[\n|\r]")[0];
			nameList.add(l.getIdentifier() + " – " + firstPart);
		}
		String[] nameArray = nameList.toArray(new  String[nameList.size()]);
		
		links.setListData(nameArray);
	}

}
