package prioritization.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import evaluation.EvaluationEntry;
import evaluation.OptimalEvaluationEntry;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import prioritization.evaluation.ProjectEvaluationEntry;
import priorization.main.PrioritizationStrategyBase;

/**
 * Prioritizes the failures in the best possible order. Uses the fault information to accomplish this.
 * Assumption: Failures only fail because of exactly one fault.
 */
public class OptimalPrioritization extends PrioritizationStrategyBase{
	private List<Fault> sortedFaultsByFailureCountDescending;
	public OptimalPrioritization(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.strategyName = "OPTIMAL";
	}

	@Override
	public void prioritizeFailures() {
		sortedFaultsByFailureCountDescending = new ArrayList<Fault>();
		for (Fault fault: faults) {
			sortedFaultsByFailureCountDescending.add(fault);
		}
		Collections.sort(sortedFaultsByFailureCountDescending, Collections.reverseOrder());
//		prioritizedFailures = new ArrayList<TestCase>();
//		for (Fault fault: sortedFaultsByFailureCountDescending) {
//			prioritizedFailures.add(getFailureOfFault(fault));
//		}
	}
	/**
	 * Calculates the metrics for the optimal Strategy.
	 * @param optimalMetrics pass null as this argument
	 */
	@Override
	public EvaluationEntry evaluatePrioritizationStrategy(int failuresToInvestigateCount,
			ProjectEvaluationEntry projectMetrics, OptimalEvaluationEntry o) {
		int faultsFound = Math.min(failuresToInvestigateCount, faults.size());
		Set<Fault> foundFaults = new HashSet<Fault>(sortedFaultsByFailureCountDescending.subList(0, faultsFound));
		int failuresFixed = evaluationHelper.getFixedFailures(foundFaults, failures).size();
		int biggestPossibleWastedEffort = calculateBiggestPossibleWastedEffort(failuresToInvestigateCount);
		OptimalEvaluationEntry optimalMetrics = new OptimalEvaluationEntry(strategyName, faultsFound, failuresFixed,
				0, faultsFound, failuresToInvestigateCount, projectMetrics, null);
		optimalMetrics.setBiggestPossibleWastedEffort(biggestPossibleWastedEffort);
		return optimalMetrics;
	}
	
	/**
	 * To get the biggest possible wasted effort, one has to investigate in all failures of the
	 * biggest fault first, then investigate in all failures of the second biggest fault and so on.
	 */
	private int calculateBiggestPossibleWastedEffort(int failuresToInvestigateCount) {
		int biggestPossibleWastedEffort = 0;
		int failuresInvestigated = 0;
		for (Fault fault: sortedFaultsByFailureCountDescending) {
			biggestPossibleWastedEffort--;
			for (@SuppressWarnings("unused") String failure: fault.failures) {
				failuresInvestigated++;
				biggestPossibleWastedEffort++;
				if (failuresInvestigated == failuresToInvestigateCount) {
					break;
				}
			}
			if (failuresInvestigated == failuresToInvestigateCount) {
				break;
			}
		}
		return biggestPossibleWastedEffort;
	}
	@SuppressWarnings("unused")
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
