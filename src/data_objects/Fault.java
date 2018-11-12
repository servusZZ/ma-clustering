package data_objects;

import java.util.List;

public class Fault implements Comparable<Fault>{
	public List<String> failures;
	public String name;
	
	public Fault(String name, List<String> failures) {
		this.name = name;
		this.failures = failures;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public int compareTo(Fault f) {
		if (f == null) {
			return 1;
		}
		if (failures.size() > f.failures.size()) {
			return 1;
		} 
		if (failures.size() == f.failures.size()) {
			return 0;
		}
		return -1;
	}
}
