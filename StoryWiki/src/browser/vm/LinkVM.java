package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.ViewModel;
import browser.ProjectController.*;
import browser.vm.views.*;
import core.*;

public class LinkVM extends ViewModel<Link> {

	public LinkVM(ActionListener cvl, Link data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	@Override
	protected EditView<Link> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditLinkView(vcl, new SwapAndEditListener(), getData());
	}

	@Override
	protected ShowView<Link> getInstanceOfShowView(ViewClosedListener vcl) {
		ActionListener openEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", getData().getEntity());
				getOpenViewListener().actionPerformed(ove);
			}
		};
		
		return new ShowLinkView(new SwapAndEditListener(), openEntityListener, vcl, getData());
	}

	@Override
	protected ListView<Link> getInstanceOfListView(ViewClosedListener vcl) { return null; }
	
	
	@Override
	protected void writeEditToModel(Link edit) {
		getData().setDescription(edit.getDescription());
	}

	@Override
	protected Link createEdited(Map<String, String> input) {
		return new Link(getData().getEntity(), input.get("description"));
	}

	@Override
	protected Map<String, Boolean> isSingleValid(Map<String, String> input) {
		Map<String, Boolean> valids = new HashMap<String, Boolean>();
		valids.put("description", true);
		//desc is nothing but an additional string
		
		return valids;
	}
}
