package evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data_objects.Fault;
import data_objects.TestCase;

public class EvaluationUtils {

	public Set<Fault> getFoundFaults(List<TestCase> failuresInvestigated) {
		Set<Fault> foundFaults = new HashSet<Fault>();
		for (TestCase failure:failuresInvestigated) {
			foundFaults.add(failure.getFault());
		}
		return foundFaults;
	}
	public Set<TestCase> getFixedFailures(Set<Fault> foundFaults, TestCase[] allFailures){
		Set<TestCase> fixedFailures = new HashSet<TestCase>();
		for (TestCase failure: allFailures) {
			if (foundFaults.contains(failure.getFault())) {
				fixedFailures.add(failure);
			}
		}
		return fixedFailures;
	}
}
