package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.OpenViewEvent;
import browser.ViewModel;
import browser.vm.views.*;
import core.*;

public class GroupVM extends ViewModel<Group> {

	public GroupVM(ActionListener cvl, Group data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old
	protected EditView<Group> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditGroupView(vcl, new SwapAndEditListener(), getData().getIdentifier());
	}

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
		
		return new ShowGroupView(vcl, new SwapAndEditListener(), openLinkListener, removeLinkListener, getData());
	}

	protected SingleListView<Link, Group> getInstanceOfListView(ViewClosedListener vcl) {
		//columns
		String[] columnNames = { "id", "description", "type", "from", "until" };
		ListManager listM = new ListManager(getData(), columnNames,new Buffer[] {});
		
		List<String> input = new ArrayList<String>();
		Map<String, ActionListener> actions = new HashMap<String, ActionListener>();
		Map<String, String> optNames = new HashMap<String, String>();
		
		actions.put("open", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Searchable<?> link = listM.getSelected();
				OpenViewEvent ove = new OpenViewEvent(this, ActionEvent.ACTION_PERFORMED, "open link", link);
				
				getOpenViewListener().actionPerformed(ove);
			}
		});
		optNames.put("open", "Öffnen");
		
		actions.put("delete", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Searchable<?> link = listM.getSelected();
				if(link != null) {
					getData().unContain(link.getIdentifier());
					commitEdit();
				}
			}
		});
		optNames.put("delete", "Entfernen");
		
		actions.put("swap", new SwapAndEditListener());
		optNames.put("swap", "Weiter");
		
		return new SingleListView<Link, Group>(vcl, getData(), listM, input, actions, optNames, new HashMap<String,Buffer>(), new String[] {"open", "delete", "swap"});
	}
	
	//new
	protected View<Group> createInitView(ViewClosedListener vcl) {
		return createNextView(vcl);
	}
	
	protected View<Group> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof ListView) {
			return getInstanceOfEditView(vcl);
		} else {
			return getInstanceOfListView(vcl);
		}
	}

	
	/* ====================
	 * 		edit stuff
	 * ==================== */
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
