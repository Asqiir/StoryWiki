package core;

import java.util.*;

import java.io.*;
import java.time.*;

public abstract class SearchContainer<INTERN> implements Serializable {
	private List<Searchable<INTERN>> containing = new ArrayList<Searchable<INTERN>>();
	
	public SearchContainer(){}
	
	public SearchContainer(SearchContainer sc) {
		containing = new ArrayList<Searchable<INTERN>>(sc.containing);
	}
	
	public SearchContainer(List<Searchable<INTERN>> list) {
		containing.addAll(list);
	}
	
	public abstract String getTitle();
	
	//manipulate inner stuff
	public void add(Searchable<INTERN> s) {
		if(get(s.getIdentifier())==null) {
			containing.add(s);
		}
	}
	
	protected final void remove(String identifier) {
		containing.remove(get(identifier));
	}
	
	public abstract boolean unContain(String identifier);
	
	//get ordered stuff
	public static List<Searchable<?>> search(List<? extends Searchable<?>> searchIn, String searchPhrase) {
		List<Searchable<?>> result = new ArrayList<Searchable<?>>();
		
		for(Searchable<?> s:searchIn) {
			if(null != s.match(searchPhrase) && s.match(searchPhrase)) {
				result.add(s);
			}
		}
		
		for(Searchable<?> s:searchIn) {
			if(s.match(searchPhrase) == null) {
				result.add(s);
			}
		}
		
		return result;
	}

	public static List<Searchable<?>> filterForValid(final List<? extends Searchable> unfiltered, LocalDate stamp, Period gap) {
		List<Searchable<?>> filtered = new ArrayList<Searchable<?>>();
		
		for(Searchable<?> sb:unfiltered) {
			if(Searchable.isValidTo(sb, stamp, gap)) {
				filtered.add(sb);
			}
		}
		
		return filtered;
	}
	
	public static List<Searchable<?>> orderByDate(final List<? extends Searchable<?>> unordered, boolean earliestFirst) {
		List<Searchable<?>> ordered = new ArrayList<Searchable<?>>(unordered);
		
		Collections.sort(ordered, new Comparator<Searchable<?>>() {
			@Override
			public int compare(Searchable<?> arg0, Searchable<?> arg1) {
				if(earliestFirst) {
					if(arg0.getValidFrom() == null || arg1.getValidFrom() == null) {
						return 0;
					}
					return arg0.getValidFrom().compareTo(arg1.getValidFrom());
				} else {
					if(arg0.getValidTime() == null || arg1.getValidTime() == null) {
						return 0;
					}
					return Searchable.getValidUntil(arg0).compareTo(Searchable.getValidUntil(arg1));
				}
			}
		});
		
		return ordered;
	}
	
	public static List<Searchable<?>> filterForType(final List<? extends Searchable<?>> unfiltered, Entity.Types type) {
		List<Searchable<?>> filtered = new ArrayList<Searchable<?>>();
		
		for(Searchable<?> element:unfiltered) {
			if(element.getType() != null && element.getType().equals(type)) {
				filtered.add(element);
			}
		}
		return filtered;
	}
	
	public List<Searchable<INTERN>> getAll() {
		return new ArrayList<Searchable<INTERN>>(containing);
	}

	
	//mostly for tests
	public boolean contains(String key) {
		return get(key) != null;
	}
	
	public boolean isEmpty() {
		return containing.isEmpty();
	}

	public int size() {
		return containing.size();
	}
	
	//getter – THE one
	public Searchable<INTERN> get(String identifier) {
		for(Searchable<INTERN> s:containing) {
			if(s.getIdentifier().equals(identifier)) {
				return s;
			}
		}
		return null;
	}

	public SearchContainer replaceInnerWith(List<Searchable<INTERN>> newContent) {
		containing = newContent;
		return this;
	}
}
