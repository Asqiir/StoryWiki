package browser.views;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import core.*;

public class GroupsAndLinksListView extends ListView<Entity> {
	
	JComponent linkListComp;
	JComponent groupListComp;
	JPanel linkSearch;
	JPanel groupSearch;
	JPanel linkOptions;
	JPanel groupOptions;
	JPanel proceed;
	
	public GroupsAndLinksListView(WindowAdapter vcl, Entity model, ListManager linkList, ListManager groupList, List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames,
			Map<String,Buffer> buffers, String[] order, String title) {
		super(vcl, model, new ListManager[] {linkList, groupList}, inputs, actions, optNames, buffers, order);
		
		linkListComp = linkList.getListComp();
		groupListComp = groupList.getListComp();
		
		linkSearch = linkList.getSearchOptionsPanel(); //TODO: add decision in ListManagers constructor for 1 or 2 rows
		groupSearch = groupList.getSearchOptionsPanel();
		
		//now draw these
		drawLayer(linkListComp,groupListComp,linkSearch,groupSearch,linkOptions,groupOptions,proceed, title);
		
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	private void drawLayer(JComponent linkListComp, JComponent groupListComp, JPanel linkSearch, JPanel groupSearch,
			JPanel linkOptions, JPanel groupOptions, JComponent proceed, String title) {
	
		layer.setLayout(new GridBagLayout());
		
		GridBagConstraints tC = new GridBagConstraints();
		tC.gridx = 0;
		tC.gridy = 0;
		tC.weightx = 1;
		tC.weighty = 0;
		tC.fill = GridBagConstraints.HORIZONTAL;
		tC.insets = new Insets(0,0,BIG_BORDER,0);
		
		JLabel tLabel = new JLabel(title);
		tLabel.setFont(TITLE_FONT);
		layer.add(tLabel, tC);
		
		GridBagConstraints lLC = new GridBagConstraints();
		lLC.gridx = 0;
		lLC.gridy = 1;
		lLC.weightx = 0.7;
		lLC.weighty = 1;
		lLC.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints gLC = new GridBagConstraints();
		gLC.gridx = 1;
		gLC.gridy = 1;
		gLC.weightx = 0.3;
		gLC.weighty = 1;
		gLC.fill = GridBagConstraints.BOTH;
		gLC.insets = new Insets(0,BIG_BORDER,0,0);
		
		layer.add(linkListComp, lLC);
		layer.add(groupListComp, gLC);
		
		GridBagConstraints lSC = new GridBagConstraints();
		lSC.gridx = 0;
		lSC.gridy = 2;
		lSC.gridheight = 2;
		lSC.weightx = 0.7;
		lSC.weighty = 0;
		lSC.fill = GridBagConstraints.HORIZONTAL;
		lSC.insets = new Insets(BIG_BORDER,0,BIG_BORDER,0);
		
		GridBagConstraints gSC = new GridBagConstraints();
		gSC.gridx = 1;
		gSC.gridy = 2;
		gSC.gridheight = 1;
		gSC.weightx = 0.3;
		gSC.weighty = 0;
		gSC.fill = GridBagConstraints.HORIZONTAL;
		gSC.insets = new Insets(BIG_BORDER,BIG_BORDER,BIG_BORDER,0);
		
		layer.add(linkSearch, lSC);
		layer.add(groupSearch, gSC);
		
		GridBagConstraints lOC = new GridBagConstraints();
		lOC.gridx = 0;
		lOC.gridy = 4;
		lOC.weightx = 0.7;
		lOC.weighty = 0;
		lOC.fill = GridBagConstraints.HORIZONTAL;
		
		GridBagConstraints gOC = new GridBagConstraints();
		gOC.gridx = 1;
		gOC.gridy = 3;
		gOC.gridheight = 2;
		gOC.weightx = 0.3;
		gOC.weighty = 0;
		gOC.fill = GridBagConstraints.HORIZONTAL;
		gOC.insets = new Insets(0,BIG_BORDER,0,0);
		
		layer.add(linkOptions, lOC);
		layer.add(groupOptions, gOC);
		
		GridBagConstraints pC = new GridBagConstraints();
		pC.gridx = 0;
		pC.gridy = 5;
		pC.gridheight = 1;
		pC.weightx = 1;
		pC.weighty = 0;
		pC.fill = GridBagConstraints.HORIZONTAL;
		pC.gridwidth = 2;
		pC.insets = new Insets(BIG_BORDER,0,0,0);
		
		layer.add(proceed, pC);
	}

	@Override
	protected String getTitle() {
		return getModel().getIdentifier();
	}

	@Override
	protected JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String,Buffer> buffers, String[] order) { //TODO: buffers
		linkOptions = createGroupedOptionsPanel("link", order, actions, inputs, optNames, buffers, 1); //at least one element per row
		groupOptions = createGroupedOptionsPanel("group", order, actions, inputs, optNames, buffers, 1); //at least one element per row
		proceed = createGroupedOptionsPanel("gen", order, actions, inputs, optNames, buffers, 2); //at least 3 elements per row
		
		linkOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Links bearbeiten", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUBTITLE_FONT, Color.BLACK));
		groupOptions.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),"Gruppen bearbeiten", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, SUBTITLE_FONT, Color.BLACK));
		
		return new JPanel(); //this doesn't matter
	}
}
