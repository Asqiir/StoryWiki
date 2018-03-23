package browser.vm.views;

import java.awt.event.*;

public abstract class EditView<MODEL> extends View<MODEL> {
	ActionListener sasl;

	public EditView(WindowAdapter vcl, ActionListener saveAndSwapListener) {
		super(vcl);
		sasl = saveAndSwapListener;
	}
	
	protected ActionListener getSaveAndSwapListener() {
		return sasl;
	}
	
	public abstract MODEL getEdited();
}
