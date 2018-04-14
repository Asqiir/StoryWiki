package browser.vm.views;

import java.awt.event.WindowAdapter;
import java.util.Map;

public abstract class ShowView<MODEL> extends View<MODEL> implements ResetContentView<MODEL> {
	/*a view, that:
	 * - can change to an edit view by clicking a button
	 * - is updated when changes come (set method)
	 * - don't edit data (input fields for open views)
	 */

	public ShowView(WindowAdapter viewClosedListener) {
		super(viewClosedListener);
	}
	
	public Map<String, String> getInput() {
		return null;
	}
}
