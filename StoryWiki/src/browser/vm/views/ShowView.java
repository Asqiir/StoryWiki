package browser.vm.views;

import java.awt.event.WindowAdapter;
import java.util.Map;

public abstract class ShowView<MODEL> extends View<MODEL> {
	/*a view, that:
	 * - can change to an edit view by clicking a button
	 * - is updated when changes come (set method)
	 * - don't edit data (input fields for open views)
	 */

	public ShowView(WindowAdapter viewClosedListener, MODEL model) {
		super(viewClosedListener, model);
	}
	
	public void update() {
		set(getModel());
	}
	
	protected abstract void set(MODEL model);
}
