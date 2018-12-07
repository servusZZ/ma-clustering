package evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;

public class EvaluationUtils {

	/**
	 * Returns the wasted effort, i.e. the count of the failures which were investigated without
	 * finding at least one new fault.
	 * @param foundFaults the faults that were found by investigating in the passed failures, pass an empty HashSet here
	 */
	public int getFoundFaultsAndWastedEffort(List<TestCase> failuresInvestigated, Set<Fault> foundFaults) {
		int wastedEffort = 0;
		foundFaults.clear();
		for (TestCase failure:failuresInvestigated) {
			// all faults of an investigated failure get fixed
			if (!foundFaults.addAll(failure.getFaults())) {
				wastedEffort++;
			}
		}
		return wastedEffort;
	}
	public Set<TestCase> getFixedFailures(Set<Fault> foundFaults, TestCase[] allFailures){
		Set<TestCase> fixedFailures = new HashSet<TestCase>();
		for (TestCase failure: allFailures) {
			if (foundFaults.containsAll(failure.getFaults())) {
				fixedFailures.add(failure);
			}
		}
		return fixedFailures;
	}
}
