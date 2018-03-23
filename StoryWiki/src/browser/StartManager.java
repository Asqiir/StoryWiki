package browser;

import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import browser.vm.ProjectController;
import core.*;

public class StartManager {
	StartView view;
	ProjectController projectC;
	
	private void activate() throws IOException {
		if(projectC != null) {
			projectC.close();
			projectC = null;
		}
		view = new StartView(new StartListener()); //when an event is sent, sm is deactivated
	}
	
	private void deActivate(Project project, String dir) {
		view.close(); //while view is active, there is an EXIT_ON_CLOSE
		view = null;
		
		projectC = new ProjectController(project, dir, new RunningListener()); //when an event is sent, sm is activated
	}

	
	public static void main(String[] args) throws IOException {
		new StartManager().activate();
	}
	
	
	class StartListener implements EventListener {
		public void actionPerformed(StartEvent se) throws IOException { //we need exactly to know, what to do: new or open; and which directory
			deActivate(getProjectFrom(se), getDirFrom(se)); //StartManager deactivated; Project views activated
		}
		
		//reorder this stuff!
		public Project getProjectFrom(final StartEvent se) throws IOException {
			if(se.newProject()) {
				Project p = new Project("untitled project");
				p.save(getDirFrom(se));
				return p;
			}
			return Project.load(getDirFrom(se));
		}
		
		public String getDirFrom(final StartEvent se) {
			if(se.newProject()) {
				//FIXME! WARNING: path not ideally created
				return se.getDirectory() + "/" + "untitled.swp";
			} else {
				return se.getDirectory();
			}
		}
	}
	
	class RunningListener implements ActionListener { //we need only to know, that running project should be closed
		public void actionPerformed(ActionEvent e) {
			try {
				activate();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
