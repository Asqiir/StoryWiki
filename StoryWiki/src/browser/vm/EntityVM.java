package browser.vm;

import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import browser.detailIO.*;

import browser.views.*;

import core.*;
import core.Entity.Types;

class EntityVM extends ViewModel<Entity> {
	public EntityVM(ActionListener cvl, Entity data, ActionListener ovl, ActionListener ctrlQListener, ActionListener cel) {
		super(cvl, data, ovl, ctrlQListener, cel);
	}

	//old. remove somewhen
	protected EditView<Entity> getInstanceOfEditView(ViewClosedListener vcl) {
		return new EditEntityView(vcl, new SwapAndEditListener(), getData());
	}

	protected View<Entity> getInstanceOfShowView(ViewClosedListener vcl) {
		return new ShowView<Entity>(vcl,getData(),new SwapAndEditListener());
	}

	protected ListView<Entity> getInstanceOfListView(ViewClosedListener vcl) {
		Map<String,Buffer> buffers = new HashMap<String,Buffer>();
		Buffer linkBuffer = new Buffer(getData().getLinkContainer());
		Buffer groupBuffer = new Buffer(getData().getGroupContainer());
		
		buffers.put("gen add to l", linkBuffer);
		buffers.put("gen add to g", groupBuffer);
		
		ListManager<Link> linkManager = new ListManager<Link>(getData().getLinkContainer(), new String[] {"id", "description", "type", "from", "until"}, new Buffer[] {linkBuffer}, "Links durchsuchen");
		ListManager<Group> groupManager = new ListManager<Group>(getData().getGroupContainer(), new String[] {"id"}, new Buffer[] {groupBuffer}, "Gruppen durchsuchen");
		
		List<String> inputs = new ArrayList<String>();
		Map<String, ActionListener> actions = new HashMap<String, ActionListener>();
		Map<String, String> optNames = new HashMap<String, String>();
		
		actions.put("open link", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(linkManager.getSelected() != null) {
					OpenVMEvent ove = new OpenVMEvent(this, ActionEvent.ACTION_PERFORMED, "", linkManager.getSelected());
					getOpenViewListener().actionPerformed(ove);
				}
			}
		});
		optNames.put("open link", "Link öffnen");
		
		actions.put("delete link", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Link l = (Link) linkManager.getSelected();
				
				if(l != null && getData().isLinkedTo(l.getEntity())) {
					getData().unLink(l.getEntity());
					commitEdit();
				}
			}
		});
		optNames.put("delete link", "Link entfernen");
		
		actions.put("open group", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(groupManager.getSelected() != null) {
					OpenVMEvent ove = new OpenVMEvent(this, ActionEvent.ACTION_PERFORMED, "", groupManager.getSelected());
					getOpenViewListener().actionPerformed(ove);
				}
			}
		});
		optNames.put("open group", "Gruppe öffnen");
		
		actions.put("delete group", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Group g = (Group) groupManager.getSelected();
				
				if(g != null) {
					getData().removeGroup(g.getIdentifier());
					commitEdit();
				}
			}
		});
		optNames.put("delete group", "Gruppe löschen");
		
		actions.put("create group", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String input = ((ListView<?>) getView()).getInput("create group");
				
				if(input !=  null && !input.equals("")) {
					getData().createGroup(input);
					((ListView<?>) getView()).clearField("create group");
					commitEdit();
				}
			}
		});
		inputs.add("create group");
		optNames.put("create group", "Gruppe erstellen");
		
		
		actions.put("gen add to", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Group g = (Group) groupBuffer.getSelected();
				Link l = (Link) linkBuffer.getSelected();
				
				if(g != null && l != null) {
					g.add(l);
					commitEdit();
				}
			}
		});
		optNames.put("gen add to", "Link zu Gruppe hinzufügen");
		
		actions.put("gen swap", new SwapAndEditListener());
		optNames.put("gen swap", "Weiter");		
		
		return new GroupsAndLinksListView(vcl, getData(), linkManager, groupManager, inputs, actions, optNames, buffers,
				new String[] {"open link", "open group", "delete link", "delete group", "create group", "gen add to", "gen swap"}, getData().getType().showName() + " :: " + getData().getIdentifier());
	}
	
	//new. maybe going to be removed for viewFactory
	protected View<Entity> createInitView(ViewClosedListener vcl) {
		return createNextView(vcl);
	}
	
	protected View<Entity> createNextView(ViewClosedListener vcl) {
		if(getView() instanceof ShowView) {
			return getInstanceOfEditView(vcl);
		} else {
			if(getView() instanceof EditView) {
				return getInstanceOfListView(vcl);
			} else {
				return getInstanceOfShowView(vcl);
			}
			
		}
	}
	
	/* ==========================
	 * 		edit stuff
	 * ========================== */
	
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
