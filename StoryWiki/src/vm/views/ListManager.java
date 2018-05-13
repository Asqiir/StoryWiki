package vm.views;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;

import core.Entity;
import core.SearchContainer;
import core.Searchable;

public class ListManager {
	final JTable table = new JTable();
	
	private JPanel searchPanel;
	private final JScrollPane listComp = new JScrollPane(table);
	
	private String[] columnNames;
	private Buffer[] buffers;
	private Map<String, JComponent> filterInput = new HashMap<String, JComponent>();
	private Map<String, String> selectedFilterOptions = new HashMap<String, String>();
	
	private SearchContainer model;
	private List<Searchable> active;
	
	private static final int SMALL_BORDER = 2;
	private static final int BIG_BORDER = 10;

	public ListManager(SearchContainer m, String[] columns, Buffer[] buffers) {
		model = m;
		columnNames = columns;
		this.buffers = buffers;
		
		initListPanel();
		searchPanel = initSearchPanel();
		
		update();		
	}
	
	public ListManager(SearchContainer m, String[] columns, Buffer[] buffers, String searchOptionsHeader) {
		model = m;
		columnNames = columns;
		this.buffers = buffers;
		
		initListPanel();
		
		searchPanel = initSearchPanel();		
		searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1),searchOptionsHeader, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, View.SUBTITLE_FONT, Color.BLACK));
		
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

	
	/* ======================
	 * 		init the panels
	 * ====================== */
	private void initListPanel() {
		//selection mode
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFont(View.DEFAULT_FONT);
		
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2) {
					fowardBuffers();
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
	
		
		table.setModel(new DefaultTableModel(columnNames, 0) { //no rows by now
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		});
		
		//scrolling stuff
		listComp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listComp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	
	private JPanel initSearchPanel() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
		
		if(Arrays.asList(columnNames).contains("id")) {
			JPanel search = new JPanel();
			search.setLayout(new BorderLayout());
			search.setBorder(BorderFactory.createEmptyBorder(0, 0, SMALL_BORDER, 0));
			
			JTextField inField =  new JTextField();
			inField.setFont(View.DEFAULT_FONT);
			filterInput.put("search",inField);
			
			JLabel inLabel = new JLabel("Suche: ");
			inLabel.setFont(View.DEFAULT_FONT);
			
			search.add(inLabel, BorderLayout.WEST);
			search.add(inField);
			
			searchPanel.add(search);
		}
		
		if(Arrays.asList(columnNames).contains("from") && Arrays.asList(columnNames).contains("until")) {
			JLabel fromLabel = new JLabel("Von: ");
			JLabel untilLabel = new JLabel("Bis: ");
			
			fromLabel.setFont(View.DEFAULT_FONT);
			untilLabel.setFont(View.DEFAULT_FONT);

			JPanel dateLabelPanel = new JPanel();
			dateLabelPanel.setLayout(new GridLayout(2,1,SMALL_BORDER,SMALL_BORDER));
			dateLabelPanel.add(fromLabel);
			dateLabelPanel.add(untilLabel);
			
			JTextField from = new JTextField();
			JTextField until = new JTextField();
			
			from.setFont(View.DEFAULT_FONT);
			until.setFont(View.DEFAULT_FONT);
			
			filterInput.put("from", from);
			filterInput.put("until", until);
			
			JPanel textFieldPanel = new JPanel();
			textFieldPanel.setLayout(new GridLayout(2,1,SMALL_BORDER,SMALL_BORDER));
			textFieldPanel.add(from);
			textFieldPanel.add(until);
			
			JPanel dateFilterPanel = new JPanel();
			dateFilterPanel.setLayout(new BorderLayout());
			dateFilterPanel.add(dateLabelPanel, BorderLayout.WEST);
			dateFilterPanel.add(textFieldPanel);
			dateFilterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, SMALL_BORDER, 0));
			
			searchPanel.add(dateFilterPanel);
		}
		
		//one line for 2 dropdowns
		JPanel dropDownLine = new JPanel();
		int dropDownLineContentCounter = 0;
		if(Arrays.asList(columnNames).contains("type")) {
			JComboBox<String> typeSelector = new JComboBox<String>();
			
			typeSelector.setFont(View.DEFAULT_FONT);
			typeSelector.addItem("alle");
			typeSelector.setSelectedItem("alle");
			
			for(Entity.Types element:Entity.Types.values()) {
				typeSelector.addItem(element.showName());
			}
			dropDownLine.add(typeSelector);
			filterInput.put("type", typeSelector);
			dropDownLineContentCounter++;
		}
		if(Arrays.asList(columnNames).contains("from") && Arrays.asList(columnNames).contains("until")) {
			JComboBox<String> directorySelector = new JComboBox<String>();
			directorySelector.setFont(View.DEFAULT_FONT);
			
			directorySelector.addItem("nicht nach Datum sortiert");
			directorySelector.addItem("Nach Beginn sortiert");
			directorySelector.addItem("Nach Ende sortiert");
			
			dropDownLine.add(directorySelector);
			filterInput.put("order", directorySelector);
			dropDownLineContentCounter++;
		}
		dropDownLine.setLayout(new GridLayout(dropDownLineContentCounter,1,0,SMALL_BORDER+2));
		searchPanel.add(dropDownLine);
		
		JPanel startPanel = new JPanel();
		startPanel.setBorder(BorderFactory.createEmptyBorder(BIG_BORDER, 0, 0, 0));
		startPanel.setLayout(new BorderLayout());
		JButton startFilter = new JButton("Los!");
		startFilter.setFont(View.DEFAULT_FONT);
		startFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Map<String, Boolean> isValid = isSingleValid(getFilterOptions(filterInput));

				markInvalids(isValid);
				selectedFilterOptions = getValidsOnly(getFilterOptions(filterInput), isValid);
				
				update(); //redraws the tables only
			}
		});
		startPanel.add(startFilter);
		
		searchPanel.add(startPanel);
		return searchPanel;
	}
	
	
	/* =======================
	 * 		updating
	 * ======================= */
	public void update() {
		active = filter(model, selectedFilterOptions);
		redrawTable();
		
		for(Buffer b:buffers) {
			b.update();
		}
		
		listComp.revalidate();
	}

	private void redrawTable() {
		((DefaultTableModel) table.getModel()).setRowCount(0);
		
		for(Searchable<?> s:active) {
			String[] rowData = presentAsStringArray(s);
			((DefaultTableModel) table.getModel()).addRow(rowData);
		}
	}
	
	private Map<String, String> getValidsOnly(Map<String, String> selectedOptions, Map<String, Boolean> validationResult) {
		Map<String, String> validsOnly = new HashMap<String, String>();
		
		for(String element:selectedOptions.keySet()) {
			if(validationResult.get(element)) {
				validsOnly.put(element, selectedOptions.get(element));
			}
		}
		return validsOnly;
	}
	
	private void markInvalids(Map<String, Boolean> isValid) {
		for(String element:isValid.keySet()) {
			//valids are black, invalids are red
			if(!isValid.get(element) && !(filterInput.get(element) instanceof JComboBox)) {
				filterInput.get(element).setForeground(Color.RED);
			} else {
				filterInput.get(element).setForeground(Color.BLACK);
			}
		}
	}
	
	private List<Searchable<?>> filter(SearchContainer<Searchable> con, Map<String, String> selectedFilters) {
		List<Searchable<Searchable>> preFiltered = con.getAll();
		
		List<Searchable<?>> filter = convert(preFiltered);
		
		if(selectedFilters.keySet().contains("search")) {
			filter = SearchContainer.search(filter, selectedFilters.get("search"));
		}
		
		if(selectedFilters.keySet().contains("from") && selectedFilters.keySet().contains("until")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			LocalDate startDateInclusive = LocalDate.parse(selectedFilters.get("from"), formatter);
			LocalDate endDateExclusive = LocalDate.parse(selectedFilters.get("until"), formatter);
			
			filter = SearchContainer.filterForValid(filter, startDateInclusive, Period.between(startDateInclusive, endDateExclusive));
		}
		
		if(selectedFilters.keySet().contains("type")) {
			if(selectedFilters.get("type") != "alle") {
				filter = SearchContainer.filterForType(filter, Entity.Types.getByValue(selectedFilters.get("type")));
			}
		}
		
		if(selectedFilters.keySet().contains("order")) {
			filter = removeNonValidDates(filter);
			filter = SearchContainer.orderByDate(filter, selectedFilters.get("order").contains("eginn"));
		}
		
		
		return filter;
	}
	
	private List<Searchable<?>> removeNonValidDates(final List<Searchable<?>> filter) {
		List<Searchable<?>> f = new ArrayList<Searchable<?>>(filter);
		
		for(Searchable<?> element:filter) {
			if(element.getValidFrom() == null || element.getValidTime() == null) {
				f.remove(element);
			}
		}
		
		return f;
	}

	private List<Searchable<?>> convert(List<Searchable<Searchable>> list) {
		List<Searchable<?>> converted = new ArrayList<Searchable<?>>();
		
		for(Searchable element:list) {
			converted.add(element);
		}
		return converted;
	}

	protected String[] presentAsStringArray(Searchable<?> s) {
		String[] row = new String[columnNames.length];
		
		int index = 0;
		for(String key:columnNames) {
			row[index] = getValueFromKey(s, key);
			index++;
		}
		return row;
	}
	
	private String getValueFromKey(Searchable<?> s, String key) {
		switch(key) {
			case("id"): {
				return s.getIdentifier();
			} case("description"): {
				return s.getDescription().split("\n\n")[0];
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
			} case("type"): {
				if(s.getType() != null) {
					return s.getType().showName();
				}
			} default: {
				return "";
			}
		}
	}
	
	public Map<String, String> getFilterOptions(Map<String, JComponent> filterInputs) {
		Map<String, String> filter = new HashMap<String, String>();
		
		for(String key:filterInputs.keySet()) {
			filter.put(key, getInputOf(filterInputs.get(key)));
		}
		
		return filter;
	}
	
	private Map<String, Boolean> isSingleValid(Map<String, String> filterOptions) {
		Map<String, Boolean> valids = new HashMap<String, Boolean>();
		
		valids.put("search", true);
		
		if(filterOptions.containsKey("from") && filterOptions.containsKey("until")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
			try { //if "date from" is a valid date…
				LocalDate startDateInclusive = LocalDate.parse(filterOptions.get("from"), formatter);
				valids.put("from", true);
				
				try {//if "date until" is a valid date…
					LocalDate endDateExclusive = LocalDate.parse(filterOptions.get("until"), formatter);
					//and after "date from"
					valids.put("until", endDateExclusive.isAfter(startDateInclusive));
					
				} catch (DateTimeParseException e) {
					valids.put("until", false);
				}
				
			} catch (DateTimeParseException e) {
				valids.put("from", false);
				valids.put("until", false);
			}
		}
		
		if(filterOptions.containsKey("type")) {
			valids.put("type", Entity.Types.getByValue(filterOptions.get("type")) != null);
		}
		if(filterOptions.containsKey("order")) {
			valids.put("order", !filterOptions.get("order").contains("nicht"));
		}
		return valids;
	}
	
	public String getTitle() {
		return model.getTitle();
	}
	
	private String getInputOf(JComponent inputComp) {
		if(inputComp instanceof JTextField) {
			return ((JTextField) inputComp).getText();
		}
		if(inputComp instanceof JComboBox) {
			return (String) ((JComboBox<?>) inputComp).getSelectedItem();
		}
		return "";
	}

	public Searchable getSelected() {
		if(table.getSelectedColumn() != -1 && table.getSelectedRow() != -1 && !active.isEmpty()) {
			return active.get(table.getSelectedRow());
		} else {
			return null;
		}
	}
	 
	private void fowardBuffers() {
		Searchable selected = getSelected();
		
		for(Buffer b:buffers) {
			Searchable s = b.getSelected(); //the buffer selects the new searchable
			b.select(selected);
			selected = s; //and remembers the old one, for the next buffer
		}
	}
}
