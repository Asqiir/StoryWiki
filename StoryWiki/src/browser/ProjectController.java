package browser;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import core.Project;

public class ProjectController {
	private Project project; //shared model
	private String directory;
	
	private ActionListener runningL; //tell here, when projects "closed"-button pressed
	
	private List<ViewModel<?>> openViews = new ArrayList<ViewModel<?>>(); //tell them about any change within model, close them, when the main view is closed
	
	public ProjectController(Project p, String projectDirectory, ActionListener rl) {
		project = p;
		runningL = rl;
		directory = projectDirectory;
		
		openView(project);
	}
	
	public void close() throws IOException {
		while(!openViews.isEmpty()) {
			if(!VMFactory.isMainVM(openViews.get(0))) { //the project vm is already closing.
				//in order to the close pattern, it will remove itself in the last step from the list
				openViews.get(0).closeView();
			} else {
				openViews.remove(openViews.get(0));
			}
		}
		project.save(directory);
	}
	
	private void openView(Object arg) {
		openViews.add(VMFactory.createVM(arg, runningL, project, new CloseViewListener(), new OpenViewListener(), new CtrlQListener(), new CommitEditListener()));
	}
	
	public class OpenViewListener implements ActionListener {
		public void actionPerformed(ActionEvent ove) {
			openView(((OpenViewEvent) ove).getArg());
		}
	}
	
	class CloseViewListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//when event arrives here, the view is already closed. This event is thrown by vm.discharge()
			openViews.remove((ViewModel<?>) e.getSource());
		}
	}
	
	class CommitEditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for(ViewModel<?> vm:openViews) {
				vm.reload();
			}
		}
	}
	
	class CtrlQListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for(ViewModel element:openViews) {
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

