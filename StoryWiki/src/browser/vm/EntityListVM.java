package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.vm.ProjectController.OpenViewListener;
import browser.vm.views.EntityListView;
import core.*;

public class EntityListVM extends ListVM<Entity> {

	public EntityListVM(ActionListener cvl, SearchContainer<Entity> data, OpenViewListener ovl, ActionListener ctrlQListener) {
		super(cvl, data, ovl, ctrlQListener);
	}

	@Override
	public void initView(ViewClosedListener vcl) {
		setView(new EntityListView(vcl, getData().getAll(), getHeader()));
	}

	@Override
	protected String getHeader() {
		return ((Project) getData()).getName();
	}

}
