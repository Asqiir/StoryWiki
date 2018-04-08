package core;

import static org.junit.Assert.*;
import org.junit.*;

import java.io.IOException;
import java.util.*;

import core.Entity.Types;

public class ProjectTest {

	@Test
	public void testAddEntity() {
		Entity e1 = new Entity("a", Types.NOTE);
		Project p = new Project("someWiki");
		
		Entity e2 = new Entity("name", Types.NOTE);
		p.add(e2);
		p.add(e2);
		
		assertEquals(1, p.getEntities().size());
	}
	
	@Test
	public void testRemoveEntity() {
		Entity e1 = new Entity("e1", Types.NOTE);
		Entity e2 = new Entity("fork", Types.NOTE);
		Entity e3 = new Entity("klin", Types.NOTE);
		
		e1.link(e3);
		e2.link(e3);
		
		Project p = new Project("p");
		p.add(e1);
		p.add(e2);
		p.add(e3);
		
		assertTrue(p.unContain(e3.getIdentifier()));
		assertTrue(e1.getLinks().isEmpty());
		assertTrue(e2.getLinks().isEmpty());
	}

	@Test
	public void testSearchEntity() {
		Entity e1 = new Entity("e1", Types.NOTE);
		Entity e2 = new Entity("fork", Types.NOTE);
		Entity e3 = new Entity("klin", Types.NOTE);
		
		e1.link(e3);
		e2.link(e3);
		
		Project p = new Project("p");
		p.add(e1);
		p.add(e2);
		p.add(e3);
		
		e1.setDescription("kl");
		
		List<Searchable<?>> matches1 = SearchContainer.search(p.getAll(), "kl");
		List<Searchable<?>> matches2 = SearchContainer.search(p.getAll(), "k");
		List<Searchable<?>> matches3 = SearchContainer.search(p.getAll(), "j");

		assertEquals(2, matches1.size());
		assertEquals(3, matches2.size());
		assertTrue(matches3.isEmpty());
	}

	@Test
	public void testSaveAndLoad() throws IOException {
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
		
		pr.save("testfile.jf");
		
		
		Project x = Project.load("testfile.jf");
		
		assertEquals(pr.getEntities().size(), x.getEntities().size());
	}
}
