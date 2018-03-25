package browser.vm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.Entity;
import core.Link;

public class EntityVM extends SingleVM<Entity> {

	public EntityVM(ActionListener cvl, CommitEditListener cel, Entity data, OpenViewListener ovl) {
		super(cvl, cel, data, ovl);
	}

	@Override
	public void initView(ViewClosedListener vcl) {
		setView(getInstanceOfShowView(vcl));
	}

	@Override
	protected EditView<Entity> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditEntityView(vcl, new SwapAndEditListener(), getData());
	}

	@Override
	protected ShowView<Entity> getInstanceOfShowView(ViewClosedListener vcl) {
		return new ShowEntityView(new SwapListener(), new OpenLinkListener(), vcl, getData());
	}

	@Override
	protected void writeEditToModel(Entity entity) {
		getData().setName(entity.getName());
		getData().setType(entity.get().getType());
		getData().setDescription(entity.get().getDescription());
	}
	
	class OpenLinkListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String id = ((ShowEntityView) getView()).getSelectedLink();
			
			OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", getData().getLink(id));
			
			getOpenViewListener().actionPerformed(ove);
		}
	}
}
