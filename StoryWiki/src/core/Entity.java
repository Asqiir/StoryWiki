package core;

import java.util.*;
import java.io.*;
import java.time.*;

@SuppressWarnings("serial")
public class Entity implements Serializable, Searchable<Entity> {
	public enum Types {
		NOTE("Notitz"),
		ERA("Zeitspanne"),
		EVENT("Ereignis"),
		PERSON("Person"),
		PLACE("Ort");
		
		String shownAs;
		Types(String s) {
			shownAs = s;
		}
		
		public String showName() {
			return shownAs;
		}
		
		public static Types getByValue(String s) {
			for(Types element:values()) {
				if(element.showName().equals(s)) {
					return element;
				}
			}
			return null;
		}
	};
	public Types type;
	
	private SearchContainer<Link> linkContainer = new SearchContainer<Link>() {
		 @Override
		 public boolean unContain(String identifier) {
			 for(Searchable<Group> g:groupContainer.getAll()) {
				 g.get().unContain(identifier);
			 }
			 
			 if(contains(identifier)) {
				 remove(identifier);
				 return true;
			 }
			 return false;
		 }
		 
		 @Override
		 public String getTitle() {
			 return name;
		 }
	};
	
	private SearchContainer<Group> groupContainer = new SearchContainer<Group>() {
		@Override
		public boolean unContain(String identifier) {
			if(contains(identifier)) {
				 remove(identifier);
				 return true;
			 }
			 return false;
		}
		
		@Override
		public String getTitle() {
			return name;
		}
	};
		
	private String name;
	private String description = "";
	
	private LocalDate validFrom;
	private Period validTime;
	
	public Entity(String name, Types type) {
		this.name = name;
		this.type = type;
	}
	
	public Entity(String name, Types type, LocalDate d) {
		this.name = name;
		this.type = type;
		this.validFrom = d;
		this.validTime = Period.ofDays(1);
	}
	
	public Entity(String name, Types type, LocalDate from, LocalDate until) {
		this.name = name;
		this.type = type;
		this.validFrom = from;
		this.validTime = Period.between(from, until);
	}

	//============================
	// SETTER & GETTER
	//============================
	public void setType(Types t) {
		type = t;
	}
	
	public void setDescription(String d) {
		description = d;
	}
	
	public void setNoDate() {
		validFrom = null;
		validTime = null;
	}
	
	public void setDate(LocalDate d) {
		validFrom = d;
		validTime = Period.ofDays(1);
	}
	
	public void setDuration(LocalDate from, LocalDate until) {
		validFrom = from;
		validTime = Period.between(from, until);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Types getType() {
		return type;
	}
	
	public List<Link> getLinks() {
		return convertToLink(linkContainer.getAll());
	}
	
	public Link getLink(Entity e) {
		return (Link) linkContainer.get(e.getName());
	}
	
	public Link getLink(String id) {
		return (Link) linkContainer.get(id);
	}
	
	public LocalDate getValidFrom() {
		return validFrom;
	}
	
	public Period getValidTime() {
		return validTime;
	}
	
	
	//===========================
	//  LINK STUFF
	//===========================

	public void link(Entity e) {
		link(e, Project.UNKNOWN_TEXT);
	}
	
	public void link(Entity e, String d)  {
		if(e.equals(this)) {
			return;
		}
		
		Link l = new Link(e, d);
		
		linkContainer.add(l);
		
		if(!e.isLinkedTo(this)) {
			e.link(this);
		}
	}
		
	public void unLink(Entity e) {	//returns true if there was a link
		linkContainer.unContain(e.getName());
		
		if(e.isLinkedTo(this)) {
			e.unLink(this);
		}
	}
	
	//FIXME! der ganze generic-stuff ist absolut scheiße!
	public List<Link> searchLink(String search) {
		return convertToLinkPlain(SearchContainer.search(linkContainer.getAll(), search));
	}
	
	public void unLinkAny() {
		for(Link l:new HashSet<Link>(getLinks())) {
			unLink(l.getEntity());
		}
	}
	
	public boolean isLinkedTo(Entity e) {
		return linkContainer.contains(e.getName()); //name of element is identifier of the link
	}

	static List<Link> convertToLink(List<Searchable<Link>> list) {
		List<Link> links = new ArrayList<Link>();
		
		for(Searchable<Link> se:list) {
			links.add(se.get());
		}
		return links;
	}
	
	static List<Link> convertToLinkPlain(List<Searchable<?>> list) {
		List<Link> links = new ArrayList<Link>();
		
		for(Searchable<?> se:list) {
			links.add((Link) se.get());
		}
		return links;
	}
	
	static List<Link> convertToLink3(List<Searchable<?>> list) {
		List<Link> links = new ArrayList<Link>();
		
		for(Searchable<?> se:list) {
			links.add((Link) se);
		}
		return links;
	}
	
	//=================================
	//		GROUP STUFF
	//=================================
	public List<Group> getGroups() {
		return convertToGroup(groupContainer.getAll());
	}
	
	public void createGroup(String name) {
		groupContainer.add(new Group(name));
	}
	
	public void removeGroup(String name) {
		groupContainer.unContain(name);
	}
	
	public void renameGroup(String oldName, String newName) throws Exception {
		if(!groupContainer.contains(newName)) {
			Group g = (Group) groupContainer.get(oldName);
			g.rename(newName);
			groupContainer.remove(oldName);
			groupContainer.add(g);
		} else {
			throw new Exception("name already exists");
		}
	}
	
	public void addLinkToGroup(String entityID, String groupName) {
		((Group) groupContainer.get(groupName)).add(linkContainer.get(entityID));
	}
	
	public void removeLinkFromGroup(String entityID, String groupName) {
		((Group) groupContainer.get(groupName)).remove(entityID);
	}
	
	static List<Group> convertToGroup(List<Searchable<Group>> list) {
		List<Group> groups = new ArrayList<Group>();
		
		for(Searchable<Group> se:list) {
			groups.add(se.get());
		}
		return groups;
	}
	
	public Group getGroup(String id) {
		return (Group) groupContainer.get(id);
	}

	@Override
	public Boolean match(String search) {
		if(getName().contains(search)) {
			return true;
		}
		if(getDescription().contains(search)) {
			return null;
		}
		return false;
	}

	@Override
	public String getIdentifier() {
		return getName();
	}

	@Override
	public Entity get() {
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public SearchContainer<Link> getLinkContainer() {
		return linkContainer;
	}
	
	public SearchContainer<Group> getGroupContainer() {
		return groupContainer;
	}
}