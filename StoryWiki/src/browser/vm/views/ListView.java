package browser.vm.views;

import java.awt.event.WindowAdapter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import core.*;

public abstract class ListView<INNER> extends View<SearchContainer<INNER>> {
	private final JLabel headerLabel = new JLabel();
	protected final TableModel tableModel = new DefaultTableModel();

	public ListView(WindowAdapter vcl, List<Searchable<INNER>> listContent, String header) {
		super(vcl);
		
		frame.setSize(400, 500);
		
		JTable table = new JTable();
		table.setModel(tableModel);
		//set column names (by abstract method)
		//set not editable
		
		//init components
		redrawHeader(header);
		redrawList(listContent);
		//redrawFilters
		//ActionListener für den Button
		
		//set borders and layout
		layer.setLayout(new BoxLayout(layer, BoxLayout.PAGE_AXIS));
		layer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		
		JScrollPane scrollTable = new JScrollPane();
		scrollTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollTable.setViewportView(table);
		
		//add stuff
		layer.add(headerLabel);
		layer.add(scrollTable);
		//filters
		//startButton
	}
	
	public void redrawHeader(String header) {
		headerLabel.setText(header);
	}
	
	public abstract void redrawList(List<Searchable<INNER>> listContent);

//	public abstract INNER getSelectedItem();
//
//	public abstract void setSelected(INNER item);
}
