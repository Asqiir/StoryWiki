package browser.views;

import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public abstract class ListView<MODEL> extends View<MODEL> {
	private ListManager[] listManagers;
	protected final Map<String,JTextField>  inputFields = new HashMap<String,JTextField>();
	private JPanel ownOptions;
	
	public ListView(WindowAdapter vcl, MODEL model, ListManager[] lists, List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String, Buffer> buffers, String[] order) {
		super(vcl, model);
		
		ownOptions = initOwnOptions(inputs, actions, optNames, buffers, order);
		
		listManagers = lists;
	}
	
	protected ListManager[] getListManagers() {
		return listManagers;
	}
	
	@Override
	public void update() {
		for(ListManager element:listManagers) {
			element.update();
		}
		frame.setTitle(getTitle());
	}
	
	protected abstract String getTitle();
	
	protected abstract JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames, Map<String, Buffer> buffers, String[] order);
	
	protected JPanel getOwnOptions() {
		return ownOptions;
	}
	
	public String getInput(String key) {
		return inputFields.get(key).getText();
	}
	
	protected final JPanel createGroupedOptionsPanel(String keyMustContain, String[] order, Map<String, ActionListener> actions, List<String> inputs, Map<String,String> optNames, Map<String,Buffer> buffers, int defaultGroupSize) {
		List<JPanel> allGroups = new ArrayList<JPanel>();
		List<JComponent> currentGroup = new ArrayList<JComponent>();
		
		for(String key:order) {
			//if there is more than default elements in the current row, finish the row and start a new one
			if(currentGroup.size()>=defaultGroupSize) {
				allGroups.add(createGroupPanel(currentGroup, defaultGroupSize));
				currentGroup = new ArrayList<JComponent>();
			}

			/* Start with the new row here */
			if(key.contains(keyMustContain)) {
				//JTextField first
				if(inputs.contains(key)) {
					JTextField in = new JTextField();
					in.setFont(DEFAULT_FONT);
					
					if(actions.containsKey(key)) {
						in.addActionListener(actions.get(key));
					}
					inputFields.put(key,in);
					currentGroup.add(in);
				}
				
				if(actions.containsKey(key)) {
					String inscription;
					//name for the JButton
					if(optNames.containsKey(key)) {
						inscription = optNames.get(key);
					} else {
						inscription = key;
					}
					
					JButton act = new JButton(inscription);
					act.setFont(DEFAULT_FONT);
					act.addActionListener(actions.get(key));
					currentGroup.add(act);
				}
				
				for(String bufferKey:buffers.keySet()) {
					if(bufferKey.contains(key)) {
						currentGroup.add(buffers.get(bufferKey).getComboBox());
					}
				}
			}
		}
		
		//add all rows to the last JPanel
		JPanel ownOptions = new JPanel();
		ownOptions.setLayout(new BoxLayout(ownOptions, BoxLayout.Y_AXIS));
		
		for(JPanel group:allGroups) {
			group.setBorder(BorderFactory.createEmptyBorder(0, 0, BIG_BORDER, 0));
			ownOptions.add(group);
		}
		
		//last line
		ownOptions.add(createGroupPanel(currentGroup, defaultGroupSize));
		
		return ownOptions;
	}

	private JPanel createGroupPanel(List<JComponent> currentGroup, int defaultGroupSize) {
		int border = BIG_BORDER;
		
		JPanel groupPanel = new JPanel();
		
		for(JComponent element:currentGroup) {
			groupPanel.add(element);
			if(!(element instanceof JButton)) {
				border = 0; //no border between a textfield/buffer and his buttons
			}
		}
		
		int rowNumber = 1;
		int columnNumber = currentGroup.size();
		
		if(currentGroup.size()%defaultGroupSize==0) {
			rowNumber = currentGroup.size() / defaultGroupSize;
			columnNumber = defaultGroupSize;
		}
		
		groupPanel.setLayout(new GridLayout(rowNumber,columnNumber,border,border));
		return groupPanel;
	}

	public void clearField(String key) {
		inputFields.get(key).setText("");
	}
}
