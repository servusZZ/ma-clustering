package data_objects;

import java.util.List;

public class Fault {
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
}
