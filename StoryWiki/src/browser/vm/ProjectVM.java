package browser.vm;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import browser.detailIO.Buffer;
import browser.detailIO.ListManager;
import browser.views.*;
import core.*;
import core.Entity.*;

class ProjectVM extends ViewModel<Project> {

	public ProjectVM(ActionListener cvl, Project data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old
	protected EditView<Project> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditProjectView(getData().getName(), new SwapAndEditListener(), vcl);	
	}
		
	protected SingleListView<Entity, Project> getInstanceOfListView(ViewClosedListener vcl) {
		Buffer b1 = new Buffer(getData());
		Buffer b2 = new Buffer(getData());
		
		String[] columnNames = {"id", "description", "type", "from", "until"};
		ListManager list = new ListManager(getData(), columnNames, new Buffer[] {b1,b2});
		
		List<String> inputs = new ArrayList<String>();
		Map<String,ActionListener> actions = new HashMap<String, ActionListener>();
		Map<String,String> optNames = new HashMap<String, String>();
		Map<String,Buffer> buffers = new HashMap<String,Buffer>();
		
		actions.put("open", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				OpenVMEvent ove = new OpenVMEvent(this, ActionEvent.ACTION_PERFORMED, "", list.getSelected());
				getOpenViewListener().actionPerformed(ove);
			}
		});
		optNames.put("open", "Öffnen");
		
		actions.put("delete", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entity e = (Entity) list.getSelected();
				if(e != null && getData().unContain(e.getIdentifier())) {
					commitEdit();
				}
			}
		});
		optNames.put("delete", "Löschen");
		
		actions.put("create", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = ((SingleListView) getView()).getInput("create");
				
				if(input != null && !input.equals("")) {
					((SingleListView) getView()).emptyField("create");
					getData().add(new Entity(input, Types.NOTE));
					commitEdit();
				}
			}
		});
		inputs.add("create");
		optNames.put("create", "Notitz erstellen");
		
		actions.put("link", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entity e1 = (Entity) b1.getSelected();
				Entity e2 = (Entity) b2.getSelected();
				
				if(e1 != null && e2 != null && !e1.equals(e2) && !e1.getIdentifier().equals(e2.getIdentifier())) {
					e1.link(e2);
					commitEdit();
				}
			}
		});
		optNames.put("link", "Verlinke");
		buffers.put("link 1", b1);
		buffers.put("link 2", b2);
		
		
		actions.put("swap", new SwapAndEditListener());
		optNames.put("swap", "Weiter");
		
		String[] order = {"open", "delete", "create", "link", "swap"};
		
		return new SingleListView<Entity, Project>(vcl, getData(), list, inputs, actions, optNames, buffers, order);
	}

	//new
	protected View<Project> createInitView(ViewClosedListener vcl) {
		return createNextView(vcl);
	}
	
	protected View<Project> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof ListView) {
			return getInstanceOfEditView(vcl);
		} else {
			return getInstanceOfListView(vcl);
		}
	}


	/*============================
	 * write data to model
	 * ===========================*/
	@Override
	protected void writeEditToModel(Project project) {
		getData().rename(project.getName());
	}

	@Override
	protected Project createEdited(Map<String, String> input) {
		return new Project(input.get("name"));
	}

	@Override
	protected Map<String, Boolean> isSingleValid(Map<String, String> input) {
		Map<String, Boolean> valid = new HashMap<String, Boolean>();
		valid.put("name", input.get("name") != null && !input.get("name").equals(""));
		
		return valid;
	}
}
