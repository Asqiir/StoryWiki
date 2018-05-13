package core;

import java.io.Serializable;
import java.time.*;

public class Link implements Serializable, Searchable<Link> {
	private static final long serialVersionUID = 1L;
	private Entity linkedTo;
	private String linkDesc;
	
	public Link(Entity entity, String desc) {
		linkedTo = entity;
		linkDesc = desc;
	}
	
	public Entity getEntity() {
		return linkedTo;
	}
	
	public String getDescription() {
		return linkDesc;
	}
	
	public void setDescription(String d) {
		linkDesc = d;
	}

	@Override
	public Boolean match(String search) {
		if(getEntity().getName().contains(search)) {
			return true;
		}
		if(getDescription().contains(search)) {
			return null;
		}
		return false;
	}

	@Override
	public String getIdentifier() {
		return getEntity().getName();
	}

	@Override
	public LocalDate getValidFrom() {
		return linkedTo.getValidFrom();
	}

	@Override
	public Period getValidTime() {
		return linkedTo.getValidTime();
	}

	@Override
	public Link get() {
		return this;
	}
	
	@Override
	public Entity.Types getType() {
		return linkedTo.getType();
	}

}
