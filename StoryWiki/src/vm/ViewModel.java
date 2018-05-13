package vm;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vm.views.*;

public abstract class ViewModel<MODEL> {
	private ActionListener cvl; //close view listener
	private ActionListener ovl; //open view listener
	private ActionListener ctrlQListener;
	private ActionListener cel; //commitEditListener
	
	
	private MODEL data;
	private View<MODEL> view;
	
	public ViewModel(ActionListener cvl, MODEL data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		this.cvl = cvl;
		this.data = data;
		this.ovl = ovl;
		this.ctrlQListener = ctrlQListener;
		this.cel = cel;
		
		setView(createInitView(new ViewClosedListener()));
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
		reload();
	}
	protected ActionListener getOpenViewListener() {
		return ovl;
	}
	
	/*============================
	 * Abstract – Let's override!
	 *============================*/
	protected abstract void writeEditToModel(MODEL data);
	
	protected abstract View<MODEL> createInitView(ViewClosedListener vcl);
	protected abstract View<MODEL> createNextView(ViewClosedListener vcl);
	
//	protected abstract ShowView<MODEL> getInstanceOfShowView(ViewClosedListener vcl);
//	protected abstract EditView<MODEL> getInstanceOfEditView(ViewClosedListener vcl);
//	protected abstract ListView<MODEL> getInstanceOfListView(ViewClosedListener vcl);
//	
//	protected View<MODEL> getInitView(ViewClosedListener vcl) {
//		return getInstanceOfShowView(vcl);
//	}
//	
//	protected View<MODEL> createNextView(View<MODEL> oldView, ViewClosedListener vcl) {
//		oldView.removeCloseListener();
//		
//		if(oldView instanceof ShowView) {
//			return getInstanceOfEditView(vcl);
//		} else {
//			return getInstanceOfShowView(vcl);
//		}
//	}
	/*===========================
	 * 	Concrete
	 *===========================*/
	
	private void swapView() {
		getView().removeCloseListener();
		closeView();
		
		setView(createNextView(new ViewClosedListener()));
	}
	
	public final void reload() {
		getView().update();
		getView().repaint();
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
	public void closeView() {
		view.close();
	}
	private void discharge() {
		//this will remove the vm from the list of active vms
		getCloseListener().actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
	}
	
	protected class ViewClosedListener extends WindowAdapter {
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
	 * 	swapping class
	 * ==========================*/
	protected class SwapAndEditListener implements ActionListener {
		public SwapAndEditListener() {}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(!(getView() instanceof EditView)) {
				swapView();
				return;
			}
			
			Map<String,String> input = ((EditView<MODEL>) getView()).getInput();
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
