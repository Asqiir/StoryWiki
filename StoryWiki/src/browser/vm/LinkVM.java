package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.views.*;
import core.*;

class LinkVM extends ViewModel<Link> {

	public LinkVM(ActionListener cvl, Link data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old
	protected EditView<Link> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditLinkView(vcl, new SwapAndEditListener(), getData());
	}

	protected View<Link> getInstanceOfShowView(ViewClosedListener vcl) {
		return new ShowView<Link>(vcl,getData(), new SwapAndEditListener(), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", getData().getEntity());
				getOpenViewListener().actionPerformed(ove);
			}
		});
	}


	//new
	protected View<Link> createInitView(ViewClosedListener vcl) {
		return createNextView(vcl);
	}
	
	protected View<Link> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof ShowView) {
			return getInstanceOfEditView(vcl);
		} else {
			return getInstanceOfShowView(vcl);
		}
	}
	
	/* =====================
	 * 		edit stuff
	 * ===================== */
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
