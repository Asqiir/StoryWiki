package browser.vm;

import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import browser.vm.ProjectController.*;
import browser.vm.views.*;
import core.*;
import core.Entity.Types;

public class EntityVM extends ViewModel<Entity> {
	public EntityVM(ActionListener cvl, Entity data, OpenViewListener ovl, ActionListener ctrlQListener, CommitEditListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	@Override
	protected EditView<Entity> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditEntityView(vcl, new SwapAndEditListener(), getData());
	}

	@Override
	protected ShowView<Entity> getInstanceOfShowView(ViewClosedListener vcl) {
		ActionListener openLinkListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String id = ((ShowEntityView) getView()).getSelectedLink();
				
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", getData().getLink(id));
				
				getOpenViewListener().actionPerformed(ove);
			}
		};
		
		ActionListener createGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = ((ShowEntityView) getView()).sendInputGroup();
				
				if(!(name == null || name.equals(""))) {
					getData().createGroup(name);
					
					commitEdit();
				}
			}
		};
		
		ActionListener openGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Group group = getData().getGroup(((ShowEntityView) getView()).getSelectedGroup());
				OpenViewEvent ove = new OpenViewEvent(arg0.getSource(), ActionEvent.ACTION_PERFORMED, "", group);
				
				getOpenViewListener().actionPerformed(ove);
			}
		};
		
		ActionListener deleteGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				getData().removeGroup(((ShowEntityView) getView()).sendInputGroup());
				commitEdit();
			}
		};
		
		ActionListener addLinkToGroupListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String linkID = ((ShowEntityView) getView()).getSelectedLink();
				String groupID = ((ShowEntityView) getView()).getSelectedGroup();
				
				if(!(linkID == null || linkID.equals("") || groupID == null || groupID.equals(""))) {
					Link link = getData().getLink(linkID);
					Group group = getData().getGroup(groupID);
					
					group.add(link);
					commitEdit();
				}
			}
		};
		
		return new ShowEntityView(new SwapListener(), openLinkListener, vcl, createGroupListener, openGroupListener, deleteGroupListener, addLinkToGroupListener, getData());
	}

	protected ListView<Entity> getInstanceOfListView(ViewClosedListener vcl) { return null; }
	
	@Override
	protected void writeEditToModel(Entity entity) {
		getData().setName(entity.getName());
		getData().setType(entity.get().getType());
		getData().setDescription(entity.get().getDescription());
		getData().setDate(entity.getValidFrom());
		getData().setDuration(entity.getValidFrom(), Searchable.getValidUntil(entity));
	}

	
	@Override
	protected Entity createEdited(Map<String, String> input) {
		Entity e = new Entity(input.get("id"), Types.getByValue(input.get("type")));
		e.setDescription(input.get("description"));

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		LocalDate startDateInclusive = LocalDate.parse(input.get("date from"), formatter);
		LocalDate endDateExclusive = LocalDate.parse(input.get("date until"), formatter);
		
		e.setDate(startDateInclusive);
		e.setDuration(startDateInclusive, endDateExclusive);
		
		return e;
	}

	@Override
	protected Map<String, Boolean> isSingleValid(Map<String, String> input) {
		Map<String, Boolean> valids = new HashMap<String, Boolean>();
		
		//id: cannot be null, empty or contain o – (in cause of links)
		valids.put("id", (input.get("id") != null) && !input.get("id").equals("") && !input.get("id").contains("–"));
		//type: has to come from Entity.Types
		valids.put("type", Entity.Types.getByValue(input.get("type")) != null);
		//description: nothing but a (probably empty) string
		valids.put("description", true);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		
		try { //if "date from" is a valid date…
			LocalDate startDateInclusive = LocalDate.parse(input.get("date from"), formatter);
			valids.put("date from", true);
			
			try {//if "date until" is a valid date…
				LocalDate endDateExclusive = LocalDate.parse(input.get("date until"), formatter);
				//and after "date from"
				valids.put("date until", endDateExclusive.isAfter(startDateInclusive));
				
			} catch (DateTimeParseException e) {
				valids.put("date until", false);
			}
			
		} catch (DateTimeParseException e) {
			valids.put("date from", false);
			valids.put("date until", false);
		}
		
		return valids;
	}


}
