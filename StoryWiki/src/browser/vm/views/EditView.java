package browser.vm.views;

import java.awt.event.*;
import java.util.*;

import core.Entity;

public abstract class EditView<MODEL> extends View<MODEL> {
	ActionListener sasl;

	public EditView(WindowAdapter vcl, ActionListener saveAndSwapListener) {
		super(vcl);
		sasl = saveAndSwapListener;
	}
	
	protected ActionListener getSaveAndSwapListener() {
		return sasl;
	}

	@Override
	public void set(MODEL data) {}
	
	public abstract Map<String, String> getInput();
	public abstract void mark(List<String> inValidKeys);
}
