package core;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.*;
import java.util.*;

import core.Entity.Types;

public class EntityTest {

	//LINK STUFF
	@Test
	public void testLink() {
		Entity e1 = new Entity("a", Types.NOTE);
		Entity e2 = new Entity("b", Types.NOTE);
		Entity e3 = new Entity("c", Types.NOTE);
		
		e1.link(e2, "your only linked entity");
		
		assertTrue(e2.isLinkedTo(e1));
		assertNotNull(e1.getLink(e2));
		
		for(Link l: e1.getLinks()) {
			assertEquals(l.getDescription(), "your only linked entity");
		}
		
		for(Link l: e2.getLinks()) {
			assertEquals(l.getDescription(), "unknown");
		}
		
		assertEquals(1, e1.getLinks().size());
		assertEquals(1, e2.getLinks().size());
		assertEquals(0, e3.getLinks().size());
		
		e1.link(e1);
		assertFalse(e1.isLinkedTo(e1));
		
		e1.link(e3);
		int linkNumber = e1.getLinks().size();
		assertTrue(e1.isLinkedTo(e3));
		e1.link(e3);
		assertEquals(linkNumber, e1.getLinks().size());
	}
	
	@Test
	public void testIsLinkedTo() {
		Entity e1 = new Entity("a", Types.NOTE);
		Entity e2 = new Entity("b", Types.NOTE);
		Entity e3 = new Entity("c", Types.NOTE);
		
		e1.link(e2);
		e1.link(e3);
		
		assertTrue(e1.isLinkedTo(e2));
		assertTrue(e2.isLinkedTo(e1));
		assertTrue(e1.isLinkedTo(e3));
		assertTrue(e3.isLinkedTo(e1));
		assertFalse(e3.isLinkedTo(e2));
		assertFalse(e2.isLinkedTo(e3));
	}
	
	@Test
	public void testUnlink() {
		Entity e1 = new Entity("a", Types.NOTE);
		Entity e2 = new Entity("b", Types.NOTE);
		Entity e3 = new Entity("c", Types.NOTE);
		
		Project p = new Project("p");
		p.add(e1);
		p.add(e2);
		p.add(e3);
		
		e1.link(e2);
		e1.link(e3);
		
		assertFalse(e3.isLinkedTo(e2));
		
		e3.unLink(e1);
		assertFalse(e1.isLinkedTo(e3));
	}
	
	@Test
	public void testUnlinkAny() {
		Entity e1 = new Entity("a", Types.NOTE);
		Entity e2 = new Entity("b", Types.NOTE);
		Entity e3 = new Entity("c", Types.NOTE);
		
		Project p = new Project("p");
		p.add(e1);
		p.add(e2);
		p.add(e3);
		
		e1.link(e2);
		e1.link(e3);
		
		e1.unLinkAny();
		
		assertTrue(e1.getLinks().isEmpty());
	}

	@Test
	public void testSearchLink() {
		Project pr = new Project("pr");
		
		pr.add(new Entity("Max Mustermann", Types.PERSON));
		pr.add(new Entity("Flauschi", Types.NOTE));
		pr.add(new Entity("Festival", Types.EVENT));
		pr.add(new Entity("Karl Krar", Types.PERSON));
		
		((Entity) SearchContainer.search(pr.getAll(), "Max").get(0)).link((Entity) SearchContainer.search(pr.getAll(), "Flauschi").get(0), "sein Lieblingskuscheltier");
		((Entity) SearchContainer.search(pr.getAll(), "Karl Krar").get(0)).link((Entity) SearchContainer.search(pr.getAll(), "Max").get(0), "beste Freunde");
		((Entity) SearchContainer.search(pr.getAll(), "Max").get(0)).searchLink("Karl").get(0).setDescription("bester Freund, soll Anne mit ihm verkuppeln");
		
		
		for(Entity e:pr.getAll(Types.PERSON)) {
			e.link(pr.getAll(Types.EVENT).get(0), "das erste Treffen mit dem besten Freund");
		}
		
		assertEquals("Festival", ((Entity) SearchContainer.search(pr.getAll(), "Max").get(0)).searchLink("e").get(0).getEntity().getName());
		assertEquals(3, ((Entity) SearchContainer.search(pr.getAll(), "Max").get(0)).searchLink("e").size());
		assertTrue(((Entity) SearchContainer.search(pr.getAll(), "Max").get(0)).searchLink("juchhei!").isEmpty());
	}

	@Test
	public void testConvertToLink() {
		Link l1 = new Link(new Entity("e1", Types.NOTE), "bab");
		Link l2 = new Link(new Entity("Mark", Types.PERSON), "men");
		Link l3 = new Link(new Entity("twig", Types.EVENT), "meet");
		
		ArrayList<Searchable<Link>> al = new ArrayList<Searchable<Link>>();
		al.add(l1);
		al.add(l2);
		al.add(l3);
		
		List<Link> l = Entity.convertToLink(al);
		
		assertEquals(3, l.size());
		assertTrue(l.contains(l1));
		assertTrue(l.contains(l2));
		assertTrue(l.contains(l3));
	}

	@Test
	public void testFilterLinksByDate() {
		Entity e = new Entity("e", Types.NOTE);
		e.setDuration(LocalDate.of(2000, 1, 1), LocalDate.of(2001, 7, 3));
		
		Entity e2 = new Entity("name", Types.PERSON);
		Entity e3 = new Entity("n", Types.ERA);
		Entity e4 = new Entity("", Types.EVENT);
		Entity e5 = new Entity("out", Types.EVENT);
		Entity e6 = new Entity("o2", Types.ERA);
		
		e2.setDuration(LocalDate.of(1950, 3, 14), LocalDate.of(2000, 6, 28));
		e3.setDuration(LocalDate.of(1900, 1, 1), LocalDate.of(2020, 2, 4));
		e4.setDate(LocalDate.of(2001, 1, 1));
		e5.setDate(LocalDate.of(3000, 4, 22));
		e6.setDuration(LocalDate.of(1800, 1, 1), LocalDate.of(1950, 1, 1));
		
		e.link(e2);
		e.link(e3);
		e.link(e4);
		e.link(e5);
		e.link(e6);
		
		List<Link> filteredLinks = Entity.convertToLink3(SearchContainer.filterForValid(e.getLinks(), LocalDate.of(2000, 2, 2), Period.ofDays(350)));
		
		List<Entity> filteredEntities = new ArrayList<Entity>();
		
		for(Link l:filteredLinks) {
			filteredEntities.add(l.getEntity());
		}
		
		assertFalse(filteredEntities.contains(e));
		assertTrue(filteredEntities.contains(e2));
		assertTrue(filteredEntities.contains(e3));
		assertTrue(filteredEntities.contains(e4));
		assertFalse(filteredEntities.contains(e5));
		assertFalse(filteredEntities.contains(e6));

		assertEquals(3, filteredEntities.size());
	}
	
	//GROUP STUFF1
	@Test
	public void testCreateGroup() {
		Entity e = new Entity("b", Types.NOTE);
		assertTrue(e.getGroups().isEmpty());
		
		e.createGroup("g1");
		assertEquals(1, e.getGroups().size());
	}
	
	@Test
	public void testRemoveGroup() {
		Entity e = new Entity("b", Types.NOTE);
		Entity e2 = new Entity("c", Types.NOTE);
		Entity e3 = new Entity("d", Types.NOTE);
		Entity e4 = new Entity("e", Types.NOTE);
		
		e.link(e2);
		e.link(e3);
		e.link(e4);
		
		e.createGroup("g1");
		e.addLinkToGroup("c", "g1");
		e.addLinkToGroup("d","g1");
		
		e.createGroup("g2");
		e.addLinkToGroup("c", "g2");
		e.addLinkToGroup("e", "g2");
		
		e.removeGroup("g1");
		
		assertEquals(1, e.getGroups().size());
		assertEquals(3, e.getLinks().size());
	}
	
	@Test
	public void testAddLinkToGroup() {
		Entity e = new Entity("b", Types.NOTE);
		Entity e2 = new Entity("c", Types.NOTE);
		Entity e3 = new Entity("d", Types.NOTE);
		
		e.link(e2);
		e.link(e3);
		
		e.createGroup("g1");
		e.addLinkToGroup("c", "g1");
		//test: does linking work?
		assertEquals(1, e.getGroups().get(0).size());
	}

	@Test
	public void testRemoveLinkFromGroup() {
		Entity e = new Entity("b", Types.NOTE);
		Entity e2 = new Entity("c", Types.NOTE);
		Entity e3 = new Entity("d", Types.NOTE);
		Entity e4 = new Entity("e", Types.NOTE);
		
		e.link(e2);
		e.link(e3);
		e.link(e4);
		
		e.createGroup("g1");
		e.addLinkToGroup("c", "g1");
		e.addLinkToGroup("d","g1");
		
		e.createGroup("g2");
		e.addLinkToGroup("c", "g2");
		e.addLinkToGroup("e", "g2");
		
		assertEquals(2, e.getGroups().get(0).size());
		assertEquals(2, e.getGroups().get(1).size());
		
		e.removeLinkFromGroup("c", "g2");
		
		assertEquals(2, e.getGroups().get(0).size());
		assertEquals(1, e.getGroups().get(1).size());
		
		assertFalse(e.getGroups().get(1).contains("c"));
	}

	@Test
	public void testRenameGroup() {
		Entity e = new Entity("b", Types.NOTE);
		Entity e2 = new Entity("c", Types.NOTE);
		Entity e3 = new Entity("d", Types.NOTE);
		Entity e4 = new Entity("e", Types.NOTE);
		
		e.link(e2);
		e.link(e3);
		e.link(e4);
		
		e.createGroup("g1");
		e.addLinkToGroup("c", "g1");
		e.addLinkToGroup("d","g1");
		
		e.createGroup("g2");
		e.addLinkToGroup("c", "g2");
		e.addLinkToGroup("e", "g2");
		
		boolean noerror = true;
		try {
			e.renameGroup("g1", "g3");			
		} catch(Exception ex) {
			noerror = false;
		}
		assertTrue(noerror);

		
		assertEquals(2, e.getGroups().size());
		
		boolean error = false;		
		try {
			e.renameGroup("g2", "g3");			
		} catch(Exception ex) {
			error = true;
		}
		assertTrue(error);

		assertEquals(2, e.getGroups().size());
		
		boolean testg1 = false;
		boolean testg2 = false;
		boolean testg3 = false;
		for(Group g:e.getGroups()) {
			if(g.getIdentifier()=="g1") {
				testg1 = true;
			}
			if(g.getIdentifier()=="g2") {
				testg2 = true;
			}
			if(g.getIdentifier()=="g3") {
				testg3 = true;
			}
		}
		assertFalse(testg1);
		assertTrue(testg2);
		assertTrue(testg3);
		
		assertEquals(2, e.getGroups().size());
	}
}
