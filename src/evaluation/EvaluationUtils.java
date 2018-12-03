package evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;

public class EvaluationUtils {

	public Set<Fault> getFoundFaults(List<TestCase> failuresInvestigated) {
		Set<Fault> foundFaults = new HashSet<Fault>();
		for (TestCase failure:failuresInvestigated) {
			// all faults of an investigated failure get fixed
			foundFaults.addAll(failure.getFaults());
		}
		return foundFaults;
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
