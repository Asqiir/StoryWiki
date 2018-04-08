package browser.vm;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import browser.vm.ProjectController.CommitEditListener;
import browser.vm.ProjectController.OpenViewListener;
import browser.vm.ViewModel.ViewClosedListener;
import browser.vm.views.*;

public abstract class ViewModel<MODEL> {
	private ActionListener cvl; //close view listener
	private ActionListener ovl; //open view listener
	private ActionListener ctrlQListener;
	private CommitEditListener cel;
	
	
	private MODEL data;
	private View<MODEL> view;
	
	public ViewModel(ActionListener cvl, MODEL data, OpenViewListener ovl, ActionListener ctrlQListener, CommitEditListener cel) {
		this.cvl = cvl;
		this.data = data;
		this.ovl = ovl;
		this.ctrlQListener = ctrlQListener;
		this.cel = cel;
		
		initView(new ViewClosedListener());
	}
	
	/*============================
	 * 	   Getter and Setter
	 *============================*/
	protected ActionListener getCloseListener() {
		return cvl;
	}
	protected void setData(MODEL d) {
		data = d;
	}
	protected MODEL getData() {
		return data;
	}
	protected View<MODEL> getView() {
		return view;
	}
	protected void setView(View<MODEL> v) {
		view = v;
		view.addCtrlQListener(ctrlQListener);
	}
	protected ActionListener getOpenViewListener() {
		return ovl;
	}
	
	/*============================
	 * Abstract – Let's override!
	 *============================*/
	protected abstract void writeEditToModel(MODEL data);
	
	protected abstract EditView<MODEL> getInstanceOfEditView(ViewClosedListener vcl);
	protected abstract ShowView<MODEL> getInstanceOfShowView(ViewClosedListener vcl);
	protected abstract ListView<MODEL> getInstanceOfListView(ViewClosedListener vcl);
	
	/*===========================
	 * 	Concrete
	 *===========================*/
	private void initView(ViewClosedListener vcl) {
		View<MODEL> v = getInstanceOfShowView(vcl);
		if(v != null) {
			setView(v);
		} else {
			setView(getInstanceOfListView(vcl));
		}
	}
	
	private View getNextView(final View oldView, ViewClosedListener vcl) {
		View nextView = oldView;
		
		if(oldView instanceof ShowView) {
			nextView = getInstanceOfEditView(vcl);
		}
		if(oldView instanceof EditView || nextView == null) {
			nextView = getInstanceOfListView(vcl);
		}
		if(oldView instanceof ListView || nextView == null) {
			nextView = getInstanceOfShowView(vcl);
		}
		if(nextView == null) {
			nextView = getInstanceOfEditView(vcl);
		}
		return nextView;
	}
	
	private void swapView() {
		getView().removeCloseListener();
		closeView();
		
		setView(getNextView(getView(), new ViewClosedListener()));
	}
	
	public final void reload() {
		if(getView() instanceof ShowView) {
			((ShowView<MODEL>) getView()).set(getData());
			getView().repaint();
		}

//		if(getView() instanceof ListView) {
//			((ListView<MODEL>) getView()).set(getData());
//			getView().repaint();
//		}
	}
	
	protected void commitEdit() {
		cel.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "commit edit"));
	}
	
	
	/*============================
	 * 	   Close pattern stuff
	 *  1. vm closes view (optional step)
	 *  2. view closes. sends event to viewclosedlistener (start here, if closed by user)
	 *  3. vm discharges from projectcontrollers list
	 * ===========================*/
	protected void closeView() {
		view.close();
	}
	private void discharge() {
		//this will remove the vm from the list of active vms
		getCloseListener().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
	}
	
	class ViewClosedListener extends WindowAdapter {
		public void windowClosing(WindowEvent e)
	    {
			discharge();
	    }
	}
	
	/*============================
	 * 	   Edit validation
	 *  1. get the stuff to change
	 *  2. check if anything ist valid
	 *  3. if true: save and swap then
	 *  4. if false: mark invalid
	 * ===========================*/
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
	
	
	/*===========================
	 * 	swapping classes
	 * ==========================*/
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
}
