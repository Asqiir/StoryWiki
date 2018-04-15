package browser.vm.views;

import java.awt.event.WindowAdapter;

import core.SearchContainer;
import core.Searchable;

public class SingleListView<MODEL extends SearchContainer> extends ListView<MODEL> {

	public SingleListView(WindowAdapter vcl, MODEL model) {
		super(vcl, model);
		// TODO Auto-generated constructor stub
	}

}
