package browser.vm.views;

import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public abstract class ListView<MODEL> extends View<MODEL> {
	private ListManager[] listManagers;
	private JPanel ownOptions;

	public ListView(WindowAdapter vcl, MODEL model, ListManager[] lists, List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames) {
		super(vcl, model);
		
		ownOptions = initOwnOptions(inputs, actions, optNames);
		
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
	}
	
	protected abstract JPanel initOwnOptions(List<String> inputs, Map<String, ActionListener> actions, Map<String, String> optNames);
	
	protected JPanel getOwnOptions() {
		return ownOptions;
	}
}
