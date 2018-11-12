package priorization.main;

import java.util.List;
import java.util.Set;

import data_objects.Fault;
import data_objects.TestCase;

public abstract class PrioritizationStrategyBase {
	protected TestCase[] testCases;
	protected TestCase[] failures;
	protected TestCase[] passedTCs;
	protected Set<Fault> faults;
	
	//protected int failuresToInvestigateCount;
	
	public PrioritizationStrategyBase(TestCase[] testCases,TestCase[] failures,TestCase[] passedTCs, Set<Fault> faults) {
		//this.failuresToInvestigateCount = failuresToInvestigateCount;
		this.testCases = testCases;
		this.failures = failures;
		this.passedTCs = passedTCs;
		this.faults = faults;
	}
	/**
	 * Returns a prioritized order of the failures in which they should be investigated according
	 * to the concrete prioritization strategy.
	 * @return
	 */
	public abstract List<TestCase> prioritizeFailures();
}
