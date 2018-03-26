package browser.vm;

import java.awt.event.*;
import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.*;

public class GroupVM extends SingleVM<Group> {

	public GroupVM(ActionListener cvl, CommitEditListener cel, Group data, OpenViewListener ovl) {
		super(cvl, cel, data, ovl);
	}

	@Override
	protected EditView<Group> getInstanceOfEditView(ViewClosedListener vcl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ShowView<Group> getInstanceOfShowView(ViewClosedListener vcl) {
		ActionListener openLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = ((ShowGroupView) getView()).getSelected();
				Link link = (Link) getData().get(selected);
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", link);
			}
		};
		
		ActionListener removeLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String remByID = ((ShowGroupView) getView()).getSelected();
				
				getData().unContain(remByID);
				commitEdit();
			}
		};
		
		return new ShowGroupView(vcl, new SwapListener(), openLinkListener, removeLinkListener, getData());
	}

	@Override
	protected void writeEditToModel(Group dss) {
		// TODO Auto-generated method stub
		
	}
}
