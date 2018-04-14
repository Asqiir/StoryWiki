package browser;

import java.awt.event.ActionEvent;

public class OpenViewEvent extends ActionEvent {
	private Object arg;
	
	public OpenViewEvent(Object arg0, int arg1, String arg2, Object transporting) {
		super(arg0, arg1, arg2);
		this.arg = transporting;
	}
	
	public Object getArg() {
		return arg;
	}

}
