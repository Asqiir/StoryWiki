package core;

import java.time.*;
import java.util.*;

import org.junit.Test;

import core.Entity.Types;

import static org.junit.Assert.*;

public class SearchContainerTest {
	
	class ObjectToOrder implements Searchable<ObjectToOrder> {
		String id;
		LocalDate validFrom;
		Period validTime;

		@Override
		public Boolean match(String search) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getIdentifier() {
			return id;
		}

		@Override
		public LocalDate getValidFrom() {
			return validFrom;
		}

		@Override
		public Period getValidTime() {
			return validTime;
		}

		@Override
		public ObjectToOrder get() {
			return this;
		}
		
	}
	
	@Test
	public void testOrderByDate() {
		ObjectToOrder a = new ObjectToOrder();
		a.validFrom = LocalDate.of(2000, 2, 17);
		a.validTime = Period.ofDays(1);
		ObjectToOrder b = new ObjectToOrder();
		b.validFrom = LocalDate.of(1990, 1, 1);
		b.validTime = Period.ofYears(30);
		
		List<Searchable<ObjectToOrder>> unordered = new ArrayList<Searchable<ObjectToOrder>>();
		unordered.add(a);
		unordered.add(b);
		
		List<Searchable<?>> ordered1 = SearchContainer.orderByDate(unordered, true);
		
		assertEquals(b, ordered1.get(0));
		assertEquals(a, ordered1.get(1));
		
		List<Searchable<?>> ordered2 = SearchContainer.orderByDate(unordered, false);
		
		assertEquals(a, ordered2.get(0));
		assertEquals(b, ordered2.get(1));
	}

	@Test
	public void testAdd() {
		Project p = new Project("hib");
		
		p.add(new Entity("troff", Types.NOTE));
		p.add(new Entity("troff", Types.NOTE));
		
		assertEquals(1, p.getAll().size());
	}
}