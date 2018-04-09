package browser;

import java.awt.event.ActionListener;

import browser.vm.EntityVM;
import browser.vm.GroupVM;
import browser.vm.LinkVM;
import browser.vm.ProjectVM;
import core.Entity;
import core.Group;
import core.Link;
import core.Project;

public class VMFactory {
	public static ViewModel<?> createVM(Object arg, ActionListener runningL, Project project, ActionListener close, ActionListener open, ActionListener ctrlQ, ActionListener commitEdit) {
		if(arg instanceof Project) {
			return new ProjectVM(runningL, project, open, ctrlQ, commitEdit);
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
}
