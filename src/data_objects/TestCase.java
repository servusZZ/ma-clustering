package data_objects;

import java.util.HashSet;
import java.util.Set;

import main.Main;

public class TestCase {
	public final String name;
	public final boolean passed;
	public final Boolean[] coverage;
	public int[] numericCoverage;
	public final Set<Integer> coveredMethods;
	private Fault fault = null;
	
	public TestCase(String name, boolean passed, Boolean[] coverage) {
		this.name = name;
		this.passed = passed;
		this.coverage = coverage;
		this.coveredMethods = initCoveredMethods();
		initNumericCoverage();
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
	private void initNumericCoverage() {
		numericCoverage = new int[Main.methodsCount];
		for (int i = 0; i < coverage.length; i++) {
			if(coverage[i]) {
				numericCoverage[i]++;
			}
		}
	}
	@Override
	public String toString() {
		return this.name;
	}
	public Fault getFault() {
		return fault;
	}
	public void setFault(Fault fault) {
		this.fault = fault;
	}
}
