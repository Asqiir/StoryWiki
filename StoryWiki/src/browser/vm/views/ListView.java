package browser.vm.views;

import java.awt.event.WindowAdapter;
import java.util.Map;

public class ListView<MODEL> extends View<MODEL> {

	public ListView(WindowAdapter vcl, MODEL model) {
		super(vcl, model);
	}

	@Override
	public Map<String, String> getInput() {
		// change when input fields added
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
}
