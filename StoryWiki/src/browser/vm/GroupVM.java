package browser.vm;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import browser.ViewModel;
import browser.ProjectController.*;
import browser.vm.views.*;
import core.*;

public class GroupVM extends ViewModel<Group> {

	public GroupVM(ActionListener cvl, Group data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	@Override
	protected EditView<Group> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditGroupView(vcl, new SwapAndEditListener(), getData().getIdentifier());
	}

	@Override
	protected ShowView<Group> getInstanceOfShowView(ViewClosedListener vcl) {
		ActionListener openLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selected = ((ShowGroupView) getView()).getSelected();
				
				if(!(selected == null || selected.equals(""))) {
					String[] parts = selected.split("–");
					
					String id = parts[0].trim();
					Link link = (Link) getData().get(id);
					
					OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", link);
					getOpenViewListener().actionPerformed(ove);
				}
			}
		};
		
		ActionListener removeLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String rem = ((ShowGroupView) getView()).getSelected();
				
				if(!(rem == null || rem.equals(""))) {
					String remByID = rem.split("–")[0].trim();
					
					getData().unContain(remByID);
					commitEdit();
				}
			}
		};
		
		return new ShowGroupView(vcl, new SwapListener(), openLinkListener, removeLinkListener, getData());
	}

	protected ListView<Group> getInstanceOfListView(ViewClosedListener vcl) { return null; }
	
	
	@Override
	protected void writeEditToModel(Group group) {
		getData().rename(group.getIdentifier());
	}

	@Override
	protected Group createEdited(Map<String, String> input) {
		return new Group(input.get("name"));
	}

	@Override
	protected Map<String, Boolean> isSingleValid(Map<String, String> input) {
		Map<String, Boolean> valids = new HashMap<String, Boolean>();
		valids.put("name", (input.get("name") != null && !input.get("name").equals("")));
		
		return valids;
	}


}
