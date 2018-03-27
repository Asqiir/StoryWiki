package browser.vm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.Entity;
import core.Group;
import core.Link;

public class EntityVM extends SingleVM<Entity> {

	public EntityVM(ActionListener cvl, CommitEditListener cel, Entity data, OpenViewListener ovl) {
		super(cvl, cel, data, ovl);
	}

	@Override
	protected EditView<Entity> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditEntityView(vcl, new SwapAndEditListener(), getData());
	}

	@Override
	protected ShowView<Entity> getInstanceOfShowView(ViewClosedListener vcl) {
		ActionListener openLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String id = ((ShowEntityView) getView()).getSelectedLink();
				
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", getData().getLink(id));
				
				getOpenViewListener().actionPerformed(ove);
			}
		};
		
		ActionListener createGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = ((ShowEntityView) getView()).sendInputGroup();
				
				if(!(name == null || name.equals(""))) {
					getData().createGroup(name);
					
					commitEdit();
				}
			}
		};
		
		ActionListener openGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Group group = getData().getGroup(((ShowEntityView) getView()).getSelectedGroup());
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", group);
				
				getOpenViewListener().actionPerformed(ove);
			}
		};
		
		ActionListener deleteGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getData().removeGroup(((ShowEntityView) getView()).sendInputGroup());
				commitEdit();
			}
		};
		
		ActionListener addLinkToGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String linkID = ((ShowEntityView) getView()).getSelectedLink();
				String groupID = ((ShowEntityView) getView()).getSelectedGroup();
				
				if(!(linkID == null || linkID.equals("") || groupID == null || groupID.equals(""))) {
					Link link = getData().getLink(linkID);
					Group group = getData().getGroup(groupID);
					
					group.add(link);
					commitEdit();
				}
			}
		};
		
		return new ShowEntityView(new SwapListener(), openLinkListener, vcl, createGroupListener, openGroupListener, deleteGroupListener, addLinkToGroupListener, getData());
	}

	@Override
	protected void writeEditToModel(Entity entity) {
		getData().setName(entity.getName());
		getData().setType(entity.get().getType());
		getData().setDescription(entity.get().getDescription());
	}
}
