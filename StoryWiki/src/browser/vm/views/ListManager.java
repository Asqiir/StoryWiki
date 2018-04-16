package browser.vm.views;

import java.util.*;
import javax.swing.*;
import core.SearchContainer;
import core.Searchable;

public class ListManager {
	final JTable list = new JTable();
	final JPanel searchPanel = new JPanel();
	final JPanel listPanel = new JPanel();
	
	SearchContainer model;
	List<Searchable> active;

	public ListManager(SearchContainer m) {
		model = m;
		
		initListPanel();
		//initSearchPanel();
		
		update();		
	}
	
	public JPanel getListPanel() {
		return listPanel;
	}

	public void update() {
		active = filter(model);
		redrawTable();
		list.revalidate();
	}

	private void initListPanel() {
		//XXX
		JLabel testL = new JLabel("test");
		listPanel.add(testL);
	}
	
	private void redrawTable() {}
	
	private List<Searchable<Searchable>> filter(SearchContainer<Searchable> con) {
		return con.getAll();
	}

	public JPanel getSearchOptionsPanel() {
		return searchPanel;
	}
}
