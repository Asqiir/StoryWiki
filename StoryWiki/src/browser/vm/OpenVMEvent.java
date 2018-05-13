package browser.vm;

import java.awt.event.ActionEvent;

public class OpenVMEvent extends ActionEvent {
	private Object arg;
	
	public OpenVMEvent(Object arg0, int arg1, String arg2, Object transporting) {
		super(arg0, arg1, arg2);
		this.arg = transporting;
	}
	
	public Object getArg() {
		return arg;
	}

}
