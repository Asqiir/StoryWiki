package browser.vm.views;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import core.*;

public class ShowEntityView extends ShowView<Entity> {
	JLabel title = new JLabel();
	JTextArea descArea = new JTextArea();
	
	JLabel list_header_links = new JLabel();
	JLabel list_header_groups = new JLabel();
	
	JList<String> links = new JList<String>();
	JList<String> groups = new JList<String>();
	
	JTextField groupField = new JTextField();
	
	/* JFrame: BoxLayout
	 * 	 _____________________________________			POSITION java (jpanels name)
	 *  /_____________________________________\
	 * | JLabel TITLE			button edit	  |		|header
	 * | ____________________________________ |		
	 * ||									|||		|
	 * ||		descArea					|||		|main
	 * ||___________________________________|||		|
	 * |									  |
	 * |	 openLinks			openGroups	  |		|list_header link&group
	 * | ________________	 ________________ |		
	 * ||	links		||  |  groups		|||		|list link&group
	 * ||_______________||	|_______________|||		|
	 * | ________________	 ________________ |		
	 * ||___open_________|	|___open_________||		|open_button link&group
	 * | 									  |		
	 * |----MasterGroups----------------------|		|
	 * || __________________________________ ||		|			
	 * |||______groupField__________________|||		|GroupBox
	 * || ________________	 _______________ ||		|
	 * |||_createGroup___|  |__removeGroup__|||		|
	 * ||____________________________________||		|
	 * |______________________________________|		
	 */
	
	public ShowEntityView(ActionListener swapListener, ActionListener openLinkListener, WindowAdapter vcl, ActionListener createGroupListener, ActionListener openGroupListener, ActionListener removeGroupListener, Entity entity) {
		super(vcl);
		 
		frame.setSize(400, 500);
		frame.setLayout(new BorderLayout(10,10));
		
		//========BOX1==================
		
		/* |  JLabel TITLE			button edit	  |		|header
		 * | ____________________________________ |		
		 * ||									|||		|
		 * ||		descArea					|||		|main
		 * ||___________________________________|||		|
		 */
		
		//configure box1_header components
		title.setFont(new Font("Ubuntu", Font.BOLD, 26));
		
		JButton edit = new JButton("Bearbeiten");
		edit.addActionListener(swapListener);
		
		//header
		JPanel header = new JPanel();
		header.setLayout(new BoxLayout(header, BoxLayout.LINE_AXIS));
		
		header.add(title);
		header.add(Box.createHorizontalGlue());
		header.add(edit);
		
		//main
		JScrollPane main = new JScrollPane();
		main.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		main.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		descArea.setLineWrap(true);
		descArea.setWrapStyleWord(true);
		main.setViewportView(descArea);
		
		//===================================
		
		
		//========BOX2=======================
		
		/* | openLinks				openGroups	  |		|list_header link&group
		 * | ________________	 ________________ |		
		 * ||	links		||  |  groups		|||		|list link&group
		 * ||_______________||	|_______________|||		|
		 * | ________________	 ________________ |		
		 * ||___open_________|	|___open_________||		|openbutton link&groups
		*/

		//configure buttons/labels ist_headers
		list_header_links.setFont(new Font("Ubuntu", Font.BOLD, 20));
		list_header_groups.setFont(new Font("Ubuntu", Font.BOLD, 20));
		
		list_header_links.setHorizontalAlignment(JLabel.CENTER);
		list_header_groups.setHorizontalAlignment(JLabel.CENTER);
				
		//configure JScrollPanes for lists
		JScrollPane list_links = new JScrollPane();
		JScrollPane list_groups = new JScrollPane();
		
		list_links.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		list_groups.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		list_links.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		list_groups.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		links.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		groups.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() > 1) {
					groupField.setText(getSelectedGroup());
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		list_links.setViewportView(links);
		list_groups.setViewportView(groups);

		//configure openbuttons
		JButton open_links = new JButton("Öffnen");
		JButton open_groups = new JButton("Öffnen");
		
		open_links.addActionListener(openLinkListener);
		open_groups.addActionListener(openGroupListener);
		
		//=======BOX3====================
		JButton create = new JButton("Gruppe erstellen");
		JButton remove = new JButton ("Gruppe löschen");
		
		create.addActionListener(createGroupListener);
		remove.addActionListener(removeGroupListener);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1,2,10,10));
		buttons.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttons.add(create);
		buttons.add(remove);
		
		TitledBorder border = BorderFactory.createTitledBorder("Gruppen administrieren");
		border.setTitleFont(new Font("Ubuntu", Font.BOLD, 16));
		
		JPanel styleDiv = new JPanel();
		styleDiv.setLayout(new BorderLayout());
		styleDiv.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		styleDiv.add(groupField);
		
		JPanel adminGroups = new JPanel();
		adminGroups.setBorder(border);
		adminGroups.setLayout(new GridLayout(2,1,10,10));
		adminGroups.add(styleDiv);
		adminGroups.add(buttons);
		
		//===============================
		
		GridBagConstraints cHeader = new GridBagConstraints();
		cHeader.gridx = 0;
		cHeader.gridy = 0;
		cHeader.gridwidth = 2;
		cHeader.gridheight = 1;
		cHeader.fill = GridBagConstraints.HORIZONTAL;
		cHeader.weightx = 0.5;
		cHeader.weighty = 0;
		cHeader.ipadx = 10;
		cHeader.ipady = 10;
		
		GridBagConstraints cMain = new GridBagConstraints();
		cMain.gridx = 0;
		cMain.gridy = 1;
		cMain.gridheight = 1;
		cMain.gridwidth = 2;
		cMain.fill = GridBagConstraints.BOTH;
		cMain.weightx = 0.5;
		cMain.weighty = 0.8;
		cMain.ipadx = 10;
		cMain.ipady = 10;
		
		GridBagConstraints cListHeaderLinks = new GridBagConstraints();
		cListHeaderLinks.gridx = 0;
		cListHeaderLinks.gridy = 2;
		cListHeaderLinks.fill = GridBagConstraints.HORIZONTAL;
		cListHeaderLinks.weightx = 0.5;
		cListHeaderLinks.weighty = 0;
		cListHeaderLinks.ipadx = 10;
		cListHeaderLinks.ipady = 10;
		
		GridBagConstraints cListHeaderGroups = new GridBagConstraints();
		cListHeaderGroups.gridx = 1;
		cListHeaderGroups.gridy = 2;
		cListHeaderGroups.fill = GridBagConstraints.HORIZONTAL;
		cListHeaderGroups.weightx = 0.5;
		cListHeaderGroups.weighty = 0;
		cListHeaderGroups.ipadx = 10;
		cListHeaderGroups.ipady = 10;
		
		GridBagConstraints cListLinks = new GridBagConstraints();
		cListLinks.gridx = 0;
		cListLinks.gridy = 3;
		cListLinks.fill = GridBagConstraints.BOTH;
		cListLinks.weightx = 0.5;
		cListLinks.weighty = 0.2;
		
		GridBagConstraints cListGroups = new GridBagConstraints();
		cListGroups.gridx = 1;
		cListGroups.gridy = 3;
		cListGroups.fill = GridBagConstraints.BOTH;
		cListGroups.weightx = 0.5;
		cListGroups.weighty = 0.2;
		
		GridBagConstraints cOpenbuttonLinks = new GridBagConstraints();
		cOpenbuttonLinks.gridx = 0;
		cOpenbuttonLinks.gridy = 4;
		cOpenbuttonLinks.fill = GridBagConstraints.HORIZONTAL;
		cOpenbuttonLinks.weightx = 0.5;
		cOpenbuttonLinks.weighty = 0;
		
		GridBagConstraints cOpenbuttonGroups = new GridBagConstraints();
		cOpenbuttonGroups.gridx = 1;
		cOpenbuttonGroups.gridy = 4;
		cOpenbuttonGroups.fill = GridBagConstraints.HORIZONTAL;
		cOpenbuttonGroups.gridheight = 1;
		cOpenbuttonGroups.gridwidth = 1;
		cOpenbuttonGroups.weightx = 0.5;
		cOpenbuttonGroups.weighty = 0;
		
		GridBagConstraints cEmpty = new GridBagConstraints();
		cEmpty.gridx = 0;
		cEmpty.gridy = 5;
		cEmpty.fill = GridBagConstraints.HORIZONTAL;
		cEmpty.weightx = 0;
		cEmpty.weighty = 0;
		cEmpty.gridheight = 1;
		cEmpty.gridwidth = 2;
		
		GridBagConstraints cAdminGroups = new GridBagConstraints();
		cAdminGroups.gridx = 0;
		cAdminGroups.gridy = 6;
		cAdminGroups.fill = GridBagConstraints.HORIZONTAL;
		cAdminGroups.weightx = 0;
		cAdminGroups.weighty = 0;
		cAdminGroups.ipadx = 10;
		cAdminGroups.ipady = 10;
		cAdminGroups.gridheight = 1;
		cAdminGroups.gridwidth = 2;


		JPanel emptyPanel = new JPanel();
		emptyPanel.setMinimumSize(new Dimension(10,10));
		
		//ADD All to frame
		JPanel layer = new JPanel();
		layer.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		layer.setLayout(new GridBagLayout());
		
		layer.add(header, cHeader);
		
		layer.add(main, cMain);
		
		layer.add(list_header_links, cListHeaderLinks);
		layer.add(list_header_groups, cListHeaderGroups);
		
		layer.add(list_links, cListLinks);
		layer.add(list_groups, cListGroups);
		
		layer.add(open_links, cOpenbuttonLinks);
		layer.add(open_groups, cOpenbuttonGroups);
		
		layer.add(emptyPanel, cEmpty);
		layer.add(adminGroups, cAdminGroups);


		frame.add(layer);
		
		set(entity);
		frame.setVisible(true);
	}

	@Override
	public void set(Entity entity) {
		frame.setTitle(entity.getName());
		title.setText(entity.getName());
		descArea.setText(entity.getDescription());
		list_header_links.setText(entity.getLinks().size() + " Links");
		list_header_groups.setText(entity.getGroups().size() + " Gruppen");
		
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
	
	public String getSelectedGroup() {
		return (String) groups.getSelectedValue();
	}
	
	public String sendInputGroup() {
		String input = groupField.getText();
		groupField.setText("");
		return input;
	}

}
