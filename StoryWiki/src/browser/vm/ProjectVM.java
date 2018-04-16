package browser.vm;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import browser.OpenViewEvent;
import browser.ViewModel;
import browser.vm.views.*;
import core.*;
import core.Entity.*;

public class ProjectVM extends ViewModel<Project> {

	public ProjectVM(ActionListener cvl, Project data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old
	protected ShowView<Project> getInstanceOfShowView(ViewClosedListener vcl) {
		
		/* The newEntityListener waits for the "new entities" text field,
		 * it creats the new Entities, and opens the view */
		ActionListener newEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//1. add the entity
				
				ShowProjectView spView = (ShowProjectView) getView();
				String id = spView.sendCreateAndShowEntityInput();
				
				if(id == null) {
					return;
				}
				
				id = id.replaceAll("–", "");
				
				getData().add(new Entity(id, Types.NOTE));
				
				//2. reload existing views
				commitEdit();
				
				//3. open view of the new entity
				getOpenViewListener().actionPerformed(new OpenViewEvent(this, ActionEvent.ACTION_PERFORMED, "", (Entity) getData().get(id)));
			}
		};
		
		ActionListener deleteEntityListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String id = ((ShowProjectView) getView()).getSelected();
				
				if(id == null) {
					return;
				}
				
				getData().unContain(id);
				
				commitEdit();
			}
		};
		
		ActionListener linkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entity e1 = (Entity) getData().get(((ShowProjectView) getView()).getSelected());
				Entity e2 = (Entity) getData().get(((ShowProjectView) getView()).getLinkTo());
				
				if(e1 == null || e2 == null) {
					return;
				}
				
				//if not linked yet, link
				if(!e1.isLinkedTo(e2)) {
					e1.link(e2);
					commitEdit();
				}
			}
		};
		
		ActionListener unlinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Entity e1 = (Entity) getData().get(((ShowProjectView) getView()).getSelected());
				Entity e2 = (Entity) getData().get(((ShowProjectView) getView()).getUnlink());
				
				if(e1 == null || e2 == null) {
					return;
				}
				
				e1.unLink(e2);
				commitEdit();
			}
		};
		
		return new ShowProjectView(getData(), new SwapAndEditListener(), vcl, newEntityListener, deleteEntityListener, linkListener, unlinkListener);
	}

	protected EditView<Project> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditProjectView(getData().getName(), new SwapAndEditListener(), vcl);	
	}
	
	
	protected SingleListView<Entity, Project> getInstanceOfListView(ViewClosedListener vcl) {
		ListManager list = new ListManager(getData());
		
		List<String> inputs = new ArrayList<String>();
		Map<String,ActionListener> actions = new HashMap<String, ActionListener>();
		Map<String,String> optNames = new HashMap<String, String>();
		
		actions.put("swap", new SwapAndEditListener());
		optNames.put("swap", "Weiter");
		
		return new SingleListView<Entity, Project>(vcl, getData(), list, inputs, actions, optNames);
	}

	//new
	protected View<Project> createInitView(ViewClosedListener vcl) {
		return getInstanceOfShowView(vcl);
	}
	
	protected View<Project> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof ShowView) {
			return getInstanceOfEditView(vcl);
		} else {
			if(getView() instanceof EditView) {
				return getInstanceOfListView(vcl);
			} else {
				return getInstanceOfShowView(vcl);
			}
			
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
