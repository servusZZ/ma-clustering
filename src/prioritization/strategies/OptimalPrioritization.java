package prioritization.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import priorization.main.PrioritizationStrategyBase;

/**
 * Prioritizes the failures in the best possible order. Uses the fault information to accomplish this.
 * Assumption: Failures only fail because of exaclty one fault.
 */
public class OptimalPrioritization extends PrioritizationStrategyBase{

	public OptimalPrioritization(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.strategyName = "OPTIMAL";
	}

	@Override
	public void prioritizeFailures() {
		List<Fault> faultsList = new ArrayList<Fault>();
		for (Fault fault: faults) {
			faultsList.add(fault);
		}
		Collections.sort(faultsList, Collections.reverseOrder());
		prioritizedFailures = new ArrayList<TestCase>();
		for (Fault fault: faultsList) {
			prioritizedFailures.add(getFailureOfFault(fault));
		}
	}
	private TestCase getFailureOfFault(Fault f) {
		for (int i = 0; i < failures.length; i++) {
			if (failures[i].name.equals(getFailureNameOfFault(f))) {
				return failures[i];
			}
		}
		return null;
	}
	private String getFailureNameOfFault(Fault f) {
		return f.failures.get(0);
	}
}
