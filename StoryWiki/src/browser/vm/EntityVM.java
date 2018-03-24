package browser.vm;

import java.awt.event.ActionListener;

import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.Entity;

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
		return new ShowEntityView(new SwapListener(), vcl, /*TODO: newLinkListener, openLinkListener, deleteLinkListener, */ getData());
	}

	@Override
	protected void writeEditToModel(Entity ess) {
		getData().setName(ess.getName());
		getData().setType(ess.get().getType());
		getData().setDescription(ess.get().getDescription());
	}
}
