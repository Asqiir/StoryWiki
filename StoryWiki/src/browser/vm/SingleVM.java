package browser.vm;

import java.awt.event.*;
import java.util.*;

import browser.vm.ProjectController.*;
import browser.vm.views.*;

public abstract class SingleVM<MODEL> extends ViewModel<MODEL> {
	private CommitEditListener cel;
	
	public SingleVM(ActionListener cvl, CommitEditListener cel,  MODEL data, OpenViewListener ovl, ActionListener ctrlQListener) {
		super(cvl, data, ovl, ctrlQListener);
		this.cel = cel;
	}
	
	@Override
	public void initView(ViewClosedListener vcl) {
		setView(getInstanceOfShowView(vcl));
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
			Map<String,String> input = ((EditView) getView()).getInput();
			Map<String,Boolean> valid = isSingleValid(input);
			
			if(isInputValid(valid)) {
				MODEL edited = createEdited(input);
				writeEditToModel(edited);
				commitEdit();
				swapView();
			} else {
				markInvalid(valid);
			}

		}
	}
	
	protected abstract MODEL createEdited(Map<String,String> input);
	
	protected abstract Map<String,Boolean> isSingleValid(Map<String,String> input);
	
	protected void markInvalid(Map<String,Boolean> valids) {
		List<String> invalidKeys = new ArrayList();
		
		for(String key:valids.keySet()) {
			if(!valids.get(key)) {
				invalidKeys.add(key);
			}
		}
		((EditView) getView()).mark(invalidKeys);
	}
	
	protected boolean isInputValid(Map<String,Boolean> valids) {
		boolean isValid = true;
		
		//test, wether any value equals true
		for(String key:valids.keySet()) {
			isValid = isValid && valids.get(key);
		}
		return isValid;
	}
}
