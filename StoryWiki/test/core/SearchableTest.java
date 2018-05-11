package core;

import java.time.*;

import org.junit.Test;
import static org.junit.Assert.*;

import core.Entity.Types;

public class SearchableTest {
	
	@Test
	public void testIsValidTo() {
		Entity e = new Entity("a", Types.ERA);
		e.setDuration(LocalDate.of(1900, 1, 1), LocalDate.of(2050, 1, 1));
		
		assertTrue(Searchable.isValidTo(e, LocalDate.of(2000, 1, 1), Period.ofDays(1)));
	}
	
	@Test
	public void testDescriptionBehavior() {
		Entity e = new Entity("a", Types.NOTE);
		e.setDescription("this entity is great");
		
		assertEquals(e.getDescription(), "this entity is great");
		
		String s = "this\nare\nmultiple\nlines";
		
		String[] d = s.split("\n");
		assertEquals(d.length, 4);
	}
}
