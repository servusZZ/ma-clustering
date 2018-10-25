package data_objects;

import java.util.HashSet;
import java.util.Set;

public class TestCase {
	public final String name;
	public final boolean passed;
	public final Boolean[] coverage;
	public final Set<Integer> coveredMethods;
	
	public TestCase(String name, boolean passed, Boolean[] coverage) {
		this.name = name;
		this.passed = passed;
		this.coverage = coverage;
		this.coveredMethods = initCoveredMethods();
	}
	private Set<Integer> initCoveredMethods(){
		Set<Integer> coveredMethods = new HashSet<Integer>();
		for (int i = 0; i < coverage.length; i++) {
			if (coverage[i]) {
				coveredMethods.add(i);
			}
		}
		return coveredMethods;
	}
}
