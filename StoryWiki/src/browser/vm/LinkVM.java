package browser.vm;

import java.awt.event.*;
import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.*;

public class LinkVM extends SingleVM<Link> {

	public LinkVM(ActionListener cvl, CommitEditListener cel, Link data, OpenViewListener ovl, ActionListener ctrlQListener) {
		super(cvl, cel, data, ovl, ctrlQListener);
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
		
		return new ShowLinkView(new SwapListener(), openEntityListener, vcl, getData());
	}

	@Override
	protected void writeEditToModel(Link edit) {
		getData().setDescription(edit.getDescription());
	}
}
