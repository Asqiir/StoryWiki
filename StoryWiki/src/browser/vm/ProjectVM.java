package browser.vm;

import java.awt.event.*;
import javax.swing.JTextField; //not ideal!

import browser.vm.ProjectController.OpenViewListener;
import browser.vm.views.*;
import core.*;
import core.Entity.*;

public class ProjectVM extends SingleVM<Project> {

	public ProjectVM(ActionListener cvl, ProjectController.CommitEditListener cel, Project data, OpenViewListener ovl) {
		super(cvl, cel, data, ovl);
	}

	@Override
	public void initView(ViewClosedListener vcl) {
		setView(getInstanceOfShowView(vcl));
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
				String id = ((JTextField) arg0.getSource()).getText();
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
				JTextField textField = (JTextField) arg0.getSource();
				String id = textField.getText();
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
		
		return new ShowProjectView(getData().getName(), getData().getAll().size(), new SwapListener(), vcl, newEntityListener, deleteEntityListener, allEntitiesListener);
	}

	@Override
	protected EditView<Project> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditProjectView(getData().getName(), new SwapAndEditListener(), vcl);	
	}


	/*============================
	 * write data to model
	 * ===========================*/
	@Override
	protected void writeEditToModel(Project dss) {
		getData().rename(dss.getName());
	}
}
