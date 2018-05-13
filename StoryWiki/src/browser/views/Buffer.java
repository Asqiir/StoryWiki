package browser.views;

import javax.swing.*;
import core.SearchContainer;
import core.Searchable;

public class Buffer {
	private SearchContainer<?> con;
	private JComboBox<String> cb = new JComboBox<String>();
	
	public Buffer(SearchContainer sc) {
		con = sc;
		cb.setFont(View.DEFAULT_FONT);
		fillComboBox();
		//TODO defaultselection
	}
	
	private void fillComboBox() {
		for(Searchable<?> s:con.getAll()) {
			cb.addItem(s.getIdentifier());
		}
	}
	
	public Searchable getSelected() {
		String selected = (String) cb.getSelectedItem();
		
		if(selected != null && !selected.equals("")) {
			return con.get(selected);
		} else {
			return null;
		}
	}
	
	public void select(Searchable s) {
		cb.setSelectedItem(s.getIdentifier());
	}
	
	public void update() {
		String selected = (String) cb.getSelectedItem();
		
		cb.removeAllItems();
		fillComboBox();
		
		try {
			Searchable s = con.get(selected);
			select(s);
		} catch (NullPointerException e) {
		}
	}
	
	public JComponent getComboBox() {
		return cb;
	}
}
