package browser.vm;

import java.awt.event.*;

import browser.vm.ProjectController.CommitEditListener;
import browser.vm.ProjectController.OpenViewListener;
import browser.vm.views.EditView;
import browser.vm.views.ShowView;

public abstract class SingleVM<MODEL> extends ViewModel<MODEL> {
	private CommitEditListener cel;
	
	public SingleVM(ActionListener cvl, CommitEditListener cel,  MODEL data, OpenViewListener ovl) {
		super(cvl, data, ovl);
		this.cel = cel;
	}
	
	protected void swapView() {
		//removing the listener first, so the vm will continue existing
		getView().removeCloseListener();
		closeView();
		
		//set new view; closeviewlistener is added
		if(getView() instanceof ShowView) {
			//if show-only-mod by now:
			setView(getInstanceOfEditView(new ViewClosedListener()));
		} else {
			setView(getInstanceOfShowView(new ViewClosedListener()));
		}
	}
	
	protected final void edit() {
		/* 1. get edit
		 * 2. write write it
		 * 3. commit edit*/
		
		EditView view = (EditView) getView();

		MODEL edited = (MODEL) view.getEdited();
		//TODO: cast entfernen
		writeEditToModel(edited);
		commitEdit();
	}
	
	protected void commitEdit() {
		cel.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "commit edit"));
	}
	
	protected abstract EditView<MODEL> getInstanceOfEditView(ViewClosedListener vcl);
	protected abstract ShowView<MODEL> getInstanceOfShowView(ViewClosedListener vcl);
	
	protected abstract void writeEditToModel(MODEL dss);
	
	@Override
	public final void reload() {
		if(getView() instanceof ShowView) {
			((ShowView<MODEL>) getView()).set(getData());
			getView().repaint();
		}
	}
	
	protected class SwapListener implements ActionListener {
		@Override //This action listener swap views
		public void actionPerformed(ActionEvent arg0) {
			swapView();
		}
	}
	
	protected class SwapAndEditListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			edit();
			swapView();
		}
	}
}
