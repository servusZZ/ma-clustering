package data_objects;

import java.util.HashSet;
import java.util.Set;

import priorization.main.AnalysisWrapper;

public class TestCase {
	public final String name;
	public final boolean passed;
	public final Boolean[] coverage;
	public final int[] numericCoverage;
	public final Set<Integer> coveredMethods;
	private Fault fault = null;
	
	public TestCase(String name, boolean passed, Boolean[] coverage) {
		this.name = name;
		this.passed = passed;
		this.coverage = coverage;
		this.coveredMethods = initCoveredMethods();
		this.numericCoverage = initNumericCoverage();
	}
	public TestCase(String name, boolean passed, Set<Integer> coveredMethods) {
		this.name = name;
		this.passed = passed;
		this.coveredMethods = coveredMethods;
		this.coverage = initBooleanCoverage();
		this.numericCoverage = initNumericCoverage();
	}
	/**
	 * Fills the coveredMethods set based on the boolean coverage.
	 */
	private Set<Integer> initCoveredMethods(){
		Set<Integer> coveredMethods = new HashSet<Integer>();
		for (int i = 0; i < coverage.length; i++) {
			if (coverage[i]) {
				coveredMethods.add(i);
			}
		}
		return coveredMethods;
	}
	/**
	 * Fills the numeric coverage based on the boolean coverage. 
	 */
	private int[] initNumericCoverage() {
		int[] numericCoverage = new int[AnalysisWrapper.methodsCount];
		for (int i = 0; i < AnalysisWrapper.methodsCount; i++) {
			if(coverage[i]) {
				numericCoverage[i]++;
			}
		}
		return numericCoverage;
	}
	/**
	 * Fills the boolean coverage based on the coveredMethods set.
	 */
	private Boolean[] initBooleanCoverage() {
		Boolean[] coverage = new Boolean[AnalysisWrapper.methodsCount];
		for (int i = 0; i < AnalysisWrapper.methodsCount; i++) {
			if (coveredMethods.contains(i)) {
				coverage[i] = true;
			} else {
				coverage[i] = false;
			}
		}
		
		return coverage;
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
