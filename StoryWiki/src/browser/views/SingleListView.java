package browser.views;

import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

import core.SearchContainer;
import core.Searchable;

public class SingleListView<INNER extends Searchable<INNER>, MODEL extends SearchContainer<INNER>> extends ListView<MODEL> {
	public SingleListView(WindowAdapter vcl, MODEL model, ListManager list, java.util.List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String,Buffer> buffers, String[] order) {
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

	private ListManager getListManager() {
		return getListManagers()[0];
	}

	@Override
	protected JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String,Buffer> buffers, String[] order) {
		//last button goes to south. anything else to a grid within the center.
		
//		List<JPanel> rows = new ArrayList<JPanel>();
//		List<JComponent> current = new ArrayList<JComponent>();
//		
//		for(String key:order) {
//			//if there is more than one element in the current row, finish the row and start a new one
//			if(current.size()>1) {
//				int border = BIG_BORDER;
//				
//				JPanel row = new JPanel();
//
//				
//				for(JComponent element:current) {
//					row.add(element);
//					if(!(element instanceof JButton)) {
//						border = 0; //no border between a textfield/buffer and his buttons
//					}
//				}
//				rows.add(row);
//				
//				row.setLayout(new GridLayout(1,current.size(),border,0));
//				
//				current = new ArrayList<JComponent>();
//			}
//			
//			
//			/* Start with the new row here */
//			//JTextField first
//			if(inputs.contains(key)) {
//				JTextField in = new JTextField();
//				
//				if(actions.containsKey(key)) {
//					in.addActionListener(actions.get(key));
//				}
//				inputFields.put(key,in);
//				current.add(in);
//			}
//			
//			if(actions.containsKey(key)) {
//				String inscription;
//				//name for the JButton
//				if(optNames.containsKey(key)) {
//					inscription = optNames.get(key);
//				} else {
//					inscription = key;
//				}
//				
//				JButton act = new JButton(inscription);
//				act.addActionListener(actions.get(key));
//				current.add(act);
//			}
//			
//			for(String bufferKey:buffers.keySet()) {
//				if(bufferKey.contains(key)) {
//					current.add(buffers.get(bufferKey).getComboBox());
//				}
//			}
//		}
//		
//		
//		//add all rows to the last JPanel
//		JPanel ownOptions = new JPanel();
//		ownOptions.setLayout(new BoxLayout(ownOptions, BoxLayout.Y_AXIS));
//		
//		for(JPanel row:rows) {
//			row.setBorder(BorderFactory.createEmptyBorder(0, 0, BIG_BORDER, 0));
//			ownOptions.add(row);
//		}
//		
//		JPanel lastLine = new JPanel();
//		lastLine.setLayout(new GridLayout(current.size(),1,0,SMALL_BORDER));
//		///lastLine.setBorder(BorderFactory.createEmptyBorder(BIG_BORDER,0,0,0));
//		
//		for(JComponent element:current) {
//			lastLine.add(element);
//		}
//		ownOptions.add(lastLine);
		return createGroupedOptionsPanel("", order, actions, inputs, optNames, buffers, 2);
	}
	
	public void emptyField(String key) {
		inputFields.get(key).setText("");
	}

	protected String getTitle() {
		return getListManager().getTitle();
	}
	
}
