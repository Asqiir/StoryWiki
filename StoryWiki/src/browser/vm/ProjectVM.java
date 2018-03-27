package browser.vm;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import browser.vm.ProjectController.OpenViewListener;
import browser.vm.views.*;
import core.*;
import core.Entity.*;

public class ProjectVM extends SingleVM<Project> {

	public ProjectVM(ActionListener cvl, ProjectController.CommitEditListener cel, Project data, OpenViewListener ovl, ActionListener ctrlQListener) {
		super(cvl, cel, data, ovl, ctrlQListener);
	}

	/*============================
	 * Get the VIEW INSTANCES
	 * ===========================*/
	@Override
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
		
		ActionListener allEntitiesListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				OpenViewEvent ove = new OpenViewEvent(this, ActionEvent.ACTION_PERFORMED, "", getData().getEntities());
				getOpenViewListener().actionPerformed(ove);
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
		
		return new ShowProjectView(getData().getName(), getData().getAll().size(), createAllEntityOptions(), new SwapListener(), vcl, newEntityListener, deleteEntityListener, allEntitiesListener, linkListener, unlinkListener);
	}

	@Override
	protected EditView<Project> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditProjectView(getData().getName(), new SwapAndEditListener(), vcl);	
	}
	
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
	protected void writeEditToModel(Project dss) {
		getData().rename(dss.getName());
	}
}
