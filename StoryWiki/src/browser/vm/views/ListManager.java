package browser.vm.views;

import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

import core.SearchContainer;
import core.Searchable;

public class ListManager {
	final JTable table = new JTable();
	
	private final JPanel searchPanel = new JPanel();
	private final JScrollPane listComp = new JScrollPane(table);
	
	private String[] columnNames;
	
	SearchContainer model;
	List<Searchable> active;

	public ListManager(SearchContainer m, String[] columns) {
		model = m;
		columnNames = columns;
		
		initListPanel();
		//initSearchPanel();
		
		update();		
	}
	
	/* ======================
	 * 		get the panels
	 * ====================== */
	public JComponent getListComp() {
		return listComp;
	}
	
	public JPanel getSearchOptionsPanel() {
		return searchPanel;
	}

	
	
	public void update() {
		active = filter(model);
		redrawTable();
		listComp.revalidate();
	}

	private void initListPanel() {
		//selection mode
		table.setRowSelectionAllowed(true);
		table.setCellSelectionEnabled(false);
		
		table.setModel(new DefaultTableModel(columnNames, 0) { //no rows by now
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		});
		
		//scrolling stuff
		listComp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listComp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	
	
	private void redrawTable() {
		((DefaultTableModel) table.getModel()).setRowCount(0);
		
		int index = 0;
		for(Searchable s:active) {
			String[] rowData = presentAsStringArray(s);
			((DefaultTableModel) table.getModel()).addRow(rowData);
			
			index++;
		}
	}
	
	private List<Searchable<Searchable>> filter(SearchContainer<Searchable> con) {
		return con.getAll();
	}

	protected String[] presentAsStringArray(Searchable s) {
		String[] row = new String[columnNames.length];
		
		int index = 0;
		for(String key:columnNames) {
			row[index] = getValueFromKey(s, key);
			index++;
		}
		return row;
	}
	
	private String getValueFromKey(Searchable s, String key) {
		switch(key) {
			case("id"): {
				return s.getIdentifier();
			} case("from"): {
				try {
					return s.getValidFrom().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
				} catch(NullPointerException npe) {
					//no date set:
					return "";
				}
			} case("until"): {
				try {
					return Searchable.getValidUntil(s).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));					
				} catch(NullPointerException npe) {
					//no date set:
					return "";
				}
			} default: {
				return "";
			}
		}
	}
}
