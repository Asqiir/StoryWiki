package browser.vm.views;

import core.*;

public abstract class ViewFactory {

	public static View initView(Object data, WindowAdapter wa) {
		if(data instanceof Project) {
			return new ShowProjectView();
		}
	}
}
