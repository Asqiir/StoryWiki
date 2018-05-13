package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.detailIO.Buffer;
import browser.detailIO.ListManager;
import browser.views.*;
import core.*;

class GroupVM extends ViewModel<Group> {

	public GroupVM(ActionListener cvl, Group data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old
	protected EditView<Group> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditGroupView(vcl, new SwapAndEditListener(), getData().getIdentifier());
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
				OpenVMEvent ove = new OpenVMEvent(this, ActionEvent.ACTION_PERFORMED, "open link", link);
				
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
