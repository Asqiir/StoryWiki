package browser.vm;

import java.awt.event.*;

import browser.vm.ProjectController.OpenViewListener;
import browser.vm.views.*;

public abstract class ViewModel<MODEL> {
	private ActionListener cvl;
	private ActionListener ovl;
	private MODEL data;
	private View<MODEL> view;
	
	public ViewModel(ActionListener cvl, MODEL data, OpenViewListener ovl) {
		this.cvl = cvl;
		this.data = data;
		this.ovl = ovl;
		
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
	}
	protected ActionListener getOpenViewListener() {
		return ovl;
	}
	
	
	/*============================
	 * Abstract – Let's override!
	 *============================*/
	public abstract void initView(ViewClosedListener vcl);

	public abstract void reload();

	
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
}
