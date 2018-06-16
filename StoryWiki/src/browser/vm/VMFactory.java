package browser.vm;

import java.awt.event.ActionListener;

import core.Entity;
import core.Group;
import core.Link;
import core.Project;

public class VMFactory {
	private VMFactory() {}
	
	public static ViewModel<?> createVM(Object arg, ActionListener runningL, ActionListener close, ActionListener open, ActionListener ctrlQ, ActionListener commitEdit) {
		if(arg instanceof Project) {
			return new ProjectVM(runningL, (Project) arg, open, ctrlQ, commitEdit);
		}
		if(arg instanceof Entity) {
			return new EntityVM(close, (Entity) arg, open, ctrlQ, commitEdit);
		}
		if(arg instanceof Link) {
			return new LinkVM(close, (Link) arg, open, ctrlQ, commitEdit);
		}
		if(arg instanceof Group) {
			return new GroupVM(close, (Group) arg, open, ctrlQ, commitEdit);
		}
		
		return null;
	}
	
	public static boolean isMainVM(ViewModel<?> vm) {
		return vm instanceof ProjectVM;
	}
}
