package browser.vm;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import core.*;

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
			if(!(openViews.get(0) instanceof ProjectVM)) { //the project vm is already closing.
				//in order to the close pattern, it will remove itself in the last step from the list
				openViews.get(0).closeView();
			} else {
				openViews.remove(openViews.get(0));
			}
		}
		project.save(directory);
	}
	
	private void openView(Object arg) {
		if(arg instanceof Project) {
			openViews.add(new ProjectVM(runningL, project, new OpenViewListener(), new CtrlQListener(), new CommitEditListener()));
		}
		if(arg instanceof Entity) {
			openViews.add(new EntityVM(new CloseViewListener(), (Entity) arg, new OpenViewListener(), new CtrlQListener(), new CommitEditListener()));
		}
		if(arg instanceof Link) {
			openViews.add(new LinkVM(new CloseViewListener(), (Link) arg, new OpenViewListener(), new CtrlQListener(), new CommitEditListener()));
		}
		if(arg instanceof Group) {
			openViews.add(new GroupVM(new CloseViewListener(), (Group) arg, new OpenViewListener(), new CtrlQListener(), new CommitEditListener()));
		}
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
				if(element instanceof ProjectVM) {
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

