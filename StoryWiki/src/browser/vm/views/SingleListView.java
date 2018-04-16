package browser.vm.views;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

import core.SearchContainer;
import core.Searchable;

public class SingleListView<INNER extends Searchable<INNER>, MODEL extends SearchContainer<INNER>> extends ListView<MODEL> {

	public SingleListView(WindowAdapter vcl, MODEL model, ListManager list, java.util.List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames) {
		super(vcl, model, new ListManager[] {list}, inputs, actions, optNames);
		
		frame.setSize(400,400);
		layer.setLayout(new GridBagLayout());
		
		JPanel listPanel = getListManager().getListPanel();
		JPanel searchOptionsPanel = getListManager().getSearchOptionsPanel();
		JPanel ownOptions = getOwnOptions();
		
		GridBagConstraints listC = new GridBagConstraints();
		listC.weightx = 1;
		listC.weighty = 1;
		listC.fill = GridBagConstraints.BOTH;
		
		GridBagConstraints soptC = new GridBagConstraints();
		soptC.weightx = 0.5;
		soptC.weighty = 0;
		soptC.fill = GridBagConstraints.HORIZONTAL;
		soptC.gridy = 1;
		
		GridBagConstraints ooptC = new GridBagConstraints();
		ooptC.weightx = 0.5;
		ooptC.weighty = 0;
		ooptC.fill = GridBagConstraints.HORIZONTAL;
		ooptC.gridx = 1;
		ooptC.gridy = 1;
		
		layer.add(listPanel, listC);
		layer.add(searchOptionsPanel, soptC);
		layer.add(ownOptions, ooptC);
		
		frame.setVisible(true);
	}

	private ListManager getListManager() {
		return getListManagers()[0];
	}

	@Override
	protected JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames) {

		//number of el
		int totalNumber = optNames.size();
		int columns;
		
		if(totalNumber > 1) {
			columns = 2;
		} else {
			columns = 1;
		}
		
		int rows = Math.round(totalNumber / columns);
		
		//comps
		List<JComponent> elements = new ArrayList<JComponent>();
		
		for(String key:actions.keySet()) {
			JButton act = new JButton(optNames.get(key));
			act.addActionListener(actions.get(key));
			elements.add(act);
		}
				
		//stick together
		JPanel ownOptions = new JPanel();
		ownOptions.setLayout(new GridLayout(rows, columns, 10, 0));
		
		for(JComponent comp: elements) {
			ownOptions.add(comp);
		}
		return ownOptions;
	}

	
}
