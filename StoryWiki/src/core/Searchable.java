package core;

import java.time.*;

public interface Searchable<INTERN> {
	public static boolean isValidTo(Searchable<?> s, LocalDate stamp, Period p) {
		return (s.getValidFrom().isBefore(stamp.plus(p)) && getValidUntil(s).isAfter(stamp));
	}
	public static LocalDate getValidUntil(Searchable<?> s) {
		return s.getValidFrom().plus(s.getValidTime());
	}
	
	public Boolean match(String search);
	
	public String getIdentifier();
	
	public LocalDate getValidFrom();
	public Period getValidTime();
	
	public INTERN get();
}
