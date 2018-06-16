package browser.views;

import java.awt.event.*;
import javax.swing.*;

import browser.detailIO.Buffer;
import browser.detailIO.ListManager;

import java.util.*;
import java.util.List;
import java.awt.*;

import core.SearchContainer;
import core.Searchable;

public class SingleListView<INNER extends Searchable<INNER>, MODEL extends SearchContainer<INNER>> extends ListView<MODEL> {
	public SingleListView(WindowAdapter vcl, MODEL model, ListManager<INNER> list, java.util.List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String,Buffer> buffers, String[] order) {
		super(vcl, model, new ListManager[] {list}, inputs, actions, optNames, buffers, order);
		
		frame.setSize(800,400);
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		layer.setLayout(new GridBagLayout());
		
		JComponent listComp = getListManager().getListComp();
		JPanel searchOptionsPanel = getListManager().getSearchOptionsPanel();
		JPanel ownOptions = getOwnOptions();
		
		GridBagConstraints listC = new GridBagConstraints();
		listC.weightx = 1;
		listC.weighty = 1;
		listC.gridx = 0;
		listC.gridy = 0;
		listC.gridheight = 1;
		listC.gridwidth = 2;
		listC.fill = GridBagConstraints.BOTH;
		listC.insets = new Insets(0,0,10,0);
		
		GridBagConstraints soptC = new GridBagConstraints();
		soptC.weightx = 0.5;
		soptC.weighty = 0;
		soptC.fill = GridBagConstraints.HORIZONTAL;
		soptC.gridy = 1;
		soptC.insets = new Insets(0,0,0,0);
		soptC.ipady = 5;
		
		GridBagConstraints ooptC = new GridBagConstraints();
		ooptC.weightx = 0.5;
		ooptC.weighty = 0;
		ooptC.fill = GridBagConstraints.HORIZONTAL;
		ooptC.gridx = 1;
		ooptC.gridy = 1;
		ooptC.ipady = 0;
		ooptC.insets = new Insets(0,10,0,0);
		
		layer.add(listComp, listC);
		layer.add(searchOptionsPanel, soptC);
		layer.add(ownOptions, ooptC);
		
		frame.setVisible(true);
	}

	private ListManager<INNER> getListManager() {
		return getListManagers()[0];
	}

	@Override
	protected JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String,Buffer> buffers, String[] order) {
		return createGroupedOptionsPanel("", order, actions, inputs, optNames, buffers, 2);
	}
	
	public void emptyField(String key) {
		inputFields.get(key).setText("");
	}

	protected String getTitle() {
		return getListManager().getTitle();
	}
	
}
