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
			//open, close (projectcontroller is closed when projects view is closed), edit
			openViews.add(new ProjectVM(runningL, new CommitEditListener(), project, new OpenViewListener()));
		}
		if(arg instanceof Entity) {
			openViews.add(new EntityVM(new CloseViewListener(), new CommitEditListener(), (Entity) arg, new OpenViewListener()));
		}
		if(arg instanceof Link) {
			openViews.add(new LinkVM(new CloseViewListener(), new CommitEditListener(), (Link) arg, new OpenViewListener()));
		}
		if(arg instanceof Group) {
			System.out.println("möchte öffnen");
		}
		if(arg instanceof List && !((List) arg).isEmpty()) {
			Object firstItem = ((List) arg).get(0);
			
			if(firstItem instanceof Entity) {
				//TODO openViews.add(new EntityListVM(new CloseViewListener(), new Project(project), new OpenViewListener()));
			}
			//TODO: add other types
		}
		//TODO: add rest
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
	
}

