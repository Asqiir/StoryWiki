package browser;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class StartEvent extends ActionEvent {
	private String directory;
	private boolean isNew;
	
	
	public StartEvent(Object arg0, int arg1, String arg2) {
		super(arg0, arg1, arg2);
	}
	
	
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}

	
	public String getDirectory() {
		return directory;
	}
	
	public boolean newProject() {
		return isNew;
	}
}
