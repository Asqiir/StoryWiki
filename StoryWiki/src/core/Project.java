package core;

import java.io.*;
import java.util.*;

import core.Entity.Types;

public class Project extends SearchContainer<Entity> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String UNKNOWN_TEXT = "unknown";
	private String name;
	
	//TODO: 3 Constructors:
	// 1. create an empty Project – DONE
	// 2. load from serialized project – DONE
	// 3. import from xml-file
	
	//all kinds of entities
	
	public Project(String name) {
		super();
		this.name = name;
	}
	
	public Project(Project project) {
		super(project);
		project.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void rename(String newName) {
		name = newName;
	}
	
	@Override
	public String getTitle() {
		return getName();
	}
	
	//==========================
	//       SAVE & LOAD
	//==========================
	public void save(String directory) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(directory));
		
		os.writeObject(this);
		os.close();
	}
	
	public static Project load(String directory) throws IOException {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(directory));
		Project p = null;
		try {
			p = (Project) is.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("class Project not found");
			e.printStackTrace();
		}
		is.close();
		
		return p;
	}
	
	
	//==========================
	//   ADD AND REMOVE
	//==========================
 	
 	@Override
	public boolean unContain(String id) { //returns true if something is deleted
		if(!contains(id)) {
			return false;
		}
		
		((Entity) get(id)).unLinkAny();
		remove(id);

		return true;
	}
	
	
	//=========================
	// SOME GETTERS
	//=========================
	private List<Entity> convertToEntity(List<Searchable<Entity>> list) {
		List<Entity> entities = new ArrayList<Entity>();
		
		for(Searchable<Entity> se:list) {
			entities.add((Entity) se);
		}
		return entities;
	}
	
	public List<Entity> getEntities() {
		return convertToEntity(getAll());
	}
	
	public List<Entity> getAll(Types t) {
		List<Entity> ret = new ArrayList<Entity>();
		
		for(Entity e:getEntities()) {
			if(e.getType() == t) {
				ret.add(e);
			}
		}
		
		return ret;
	}	
}
