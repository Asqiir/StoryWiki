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
				
				//open both
				OpenViewEvent ove1 = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", e1.getLink(e2));
				OpenViewEvent ove2 = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", e2.getLink(e1));
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
		
		return new ShowProjectView(getData().getName(), getData().getAll().size(), createAllEntityOptions(), new SwapAndEditListener(), vcl, newEntityListener, deleteEntityListener, linkListener, unlinkListener);
	}

	protected EditView<Project> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditProjectView(getData().getName(), new SwapAndEditListener(), vcl);	
	}
	

	//new
	protected View<Project> createInitView(ViewClosedListener vcl) {
		return getInstanceOfShowView(vcl);
	}
	
	protected View<Project> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof EditView) {
			return getInstanceOfShowView(vcl);
		} else {
			return getInstanceOfEditView(vcl);
		}
	}
	
	//???
	protected String[] createAllEntityOptions() {
		List<Entity> eList = ((Project) getData()).getEntities();
		List<String> nameList = new ArrayList<String>();
		
		for(Entity element:eList) {
			nameList.add(element.getIdentifier());
		}
		
		return nameList.toArray(new String[nameList.size()]);
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
