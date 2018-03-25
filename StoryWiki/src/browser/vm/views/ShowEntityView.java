package browser.vm.views;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import core.Entity;
import core.Group;
import core.Link;

public class ShowEntityView extends ShowView<Entity> {
	JLabel title = new JLabel();
	JTextArea descArea = new JTextArea();
	
	JLabel openLinks = new JLabel();
	JLabel openGroups = new JLabel();
	
	JList<String> links = new JList<String>();
	JList<String> groups = new JList<String>();
	
	public ShowEntityView(ActionListener swapListener, ActionListener openLinkListener, WindowAdapter vcl, Entity entity) {
		super(vcl);
		 
		frame.setSize(400, 500);
		frame.setLayout(new BorderLayout(10,10));
		
		//NORTH
		JPanel header = new JPanel();
		header.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
		
		title.setFont(new Font("Ubuntu", Font.BOLD, 26));
		
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
		scroller.setViewportView(descArea);
		
		//SOUTH
		JPanel linksAndGroups = new JPanel();
		linksAndGroups.setLayout(new BorderLayout(10,10));
		linksAndGroups.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		
			//the two labels "x Links" and "x Groups" TODO: replace them with buttons
		JPanel headerButtons = new JPanel();
		headerButtons.setLayout(new GridLayout(1,2,10,10));
		openLinks.setFont(new Font("Ubuntu", Font.BOLD, 20));
		openGroups.setFont(new Font("Ubuntu", Font.BOLD, 20));
		openLinks.setHorizontalAlignment(JLabel.CENTER);
		openGroups.setHorizontalAlignment(JLabel.CENTER);
		headerButtons.add(openLinks);
		headerButtons.add(openGroups);
		
			//two JLists that show the links and groups
		JPanel lists = new JPanel();
		lists.setLayout(new GridLayout(1,2,10,10));
		JScrollPane scrollLinks = new JScrollPane();
		JScrollPane scrollGroups = new JScrollPane();
		
		scrollLinks.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollGroups.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollLinks.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollGroups.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		links.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scrollLinks.setViewportView(links);
		scrollGroups.setViewportView(groups);		
		
		lists.add(scrollLinks);
		lists.add(scrollGroups);
		
			//4 buttons: open and close each
		JPanel masterLinksAndGroups = new JPanel();
		masterLinksAndGroups.setLayout(new GridLayout(1,2,10,10));

		JButton openLink = new JButton("Öffnen");
		JButton openGroup = new JButton("Öffnen");
		
		openLink.addActionListener(openLinkListener);
		//TODO: opengrouplistener
		
		masterLinksAndGroups.add(openLink);
		masterLinksAndGroups.add(openGroup);
		
			//add stuff tow the southern panel
		linksAndGroups.add(headerButtons, BorderLayout.NORTH);
		linksAndGroups.add(lists, BorderLayout.CENTER);
		linksAndGroups.add(masterLinksAndGroups, BorderLayout.SOUTH);
		
		//ADD ALL
		frame.add(header, BorderLayout.NORTH);
		frame.add(scroller, BorderLayout.CENTER);
		frame.add(linksAndGroups, BorderLayout.SOUTH);
		
		set(entity);
		frame.setVisible(true);
	}

	@Override
	public void set(Entity entity) {
		frame.setTitle(entity.getName());
		title.setText(entity.getName());
		descArea.setText(entity.getDescription());
		openLinks.setText(entity.getLinks().size() + " Links");
		openGroups.setText(entity.getGroups().size() + " Gruppen");
		
		ArrayList<String> linkNames = new ArrayList<String>();
		for(Link element:entity.getLinks()) {
			linkNames.add(element.getIdentifier());
		}
		String[] linkNameArray = linkNames.toArray(new String[linkNames.size()]);
		
		ArrayList<String> groupNames = new ArrayList<String>();
		for(Group element:entity.getGroups()) {
			groupNames.add(element.getIdentifier());
		}
		String[] groupNameArray = groupNames.toArray(new String[groupNames.size()]);
		
		links.setListData(linkNameArray);
		groups.setListData(groupNameArray);
	}

	public String getSelectedLink() {
		return (String) links.getSelectedValue();
	}

}
