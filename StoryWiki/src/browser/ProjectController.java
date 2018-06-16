package browser;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import browser.vm.OpenVMEvent;
import browser.vm.VMFactory;
import browser.vm.ViewModel;
import core.Project;

public class ProjectController {
	private Project project; //shared model
	private String directory;
	
	private ActionListener runningL; //tell here, when projects "closed"-button pressed
	
	private List<ViewModel<?>> openVMs = new ArrayList<ViewModel<?>>(); //tell them about any change within model, close them, when the main view is closed
	
	public ProjectController(Project p, String projectDirectory, ActionListener rl) {
		project = p;
		runningL = rl;
		directory = projectDirectory;
		
		openVM(project);
	}
	
	public void close() throws IOException {
		while(!openVMs.isEmpty()) {
			if(!VMFactory.isMainVM(openVMs.get(0))) { //the project vm is already closing.
				//in order to the close pattern, it will remove itself in the last step from the list
				openVMs.get(0).closeView();
			} else {
				openVMs.remove(openVMs.get(0));
			}
		}
		project.save(directory);
	}
	
	private void openVM(Object arg) {
		openVMs.add(VMFactory.createVM(arg, runningL, new CloseViewListener(), new OpenVMListener(), new CtrlQListener(), new CommitEditListener()));
	}
	
	public class OpenVMListener implements ActionListener {
		public void actionPerformed(ActionEvent ove) {
			openVM(((OpenVMEvent) ove).getArg());
		}
	}
	
	class CloseViewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//when event arrives here, the view is already closed. This event is thrown by vm.discharge()
			openVMs.remove((ViewModel<?>) e.getSource());
		}
	}
	
	class CommitEditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(ViewModel<?> vm:openVMs) {
				vm.reload();
			}
		}
	}
	
	class CtrlQListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(ViewModel element:openVMs) {
				if(VMFactory.isMainVM(element)) {
					/* Having pressed CTRL + Q, anything should be closed.
					 * This happens by closing the main frame.
					 * Program will return to start frame.
					 */
					
					element.closeView();
					return;
				}
			}
		}
	}
}

