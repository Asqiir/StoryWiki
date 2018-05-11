package core;

import java.io.Serializable;
import java.time.*;

import core.Entity.Types;

public class Group extends SearchContainer<Link> implements Searchable<Group>, Serializable {
	String name;
	
	public Group(String name) {
		super();
		this.name = name;
	}
	
	public void rename(String newName) {
		name = newName;
	}
	
	@Override
	public Boolean match(String search) {
		return name.contains(search);
	}

	@Override
	public String getIdentifier() {
		return name;
	}

	@Override
	public boolean unContain(String identifier) {
		if(contains(identifier)) {
			 remove(identifier);
			 return true;
		 }
		 return false;
	}

	@Override
	public LocalDate getValidFrom() {
		return null;
	}

	@Override
	public Period getValidTime() {
		return null;
	}

	@Override
	public Group get() {
		return this;
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public String getTitle() {
		return name;
	}
	
	@Override
	public Entity.Types getType() {
		return null;
	}
}
