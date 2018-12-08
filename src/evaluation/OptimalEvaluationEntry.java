package evaluation;

import hac.evaluation.ClusteringEvaluationEntry;
import prioritization.evaluation.ProjectEvaluationEntry;

public class OptimalEvaluationEntry extends EvaluationEntry{
	/**	only filled within the optimal prioritization strategy, used for calculating the performance */
	private int biggestPossibleWastedEffort;
	private int minimumFoundFaults;
	private int minimumFixedFailures;
	public OptimalEvaluationEntry(String prioritizationStrategyName, int foundFaults, int fixedFailures,
			int wastedEffort, int investigatedFailures_actual, int investigatedFailures_planned,
			ProjectEvaluationEntry projectMetrics, ClusteringEvaluationEntry clusteringMetrics) {
		super(prioritizationStrategyName, foundFaults, fixedFailures, wastedEffort, investigatedFailures_actual,
				investigatedFailures_planned, projectMetrics, clusteringMetrics);
	}
	public int getBiggestPossibleWastedEffort() {
		return biggestPossibleWastedEffort;
	}
	public void setBiggestPossibleWastedEffort(int biggestPossibleWastedEffort) {
		this.biggestPossibleWastedEffort = biggestPossibleWastedEffort;
	}
	public int getMinimumFoundFaults() {
		return minimumFoundFaults;
	}
	public void setMinimumFoundFaults(int minimumFoundFaults) {
		this.minimumFoundFaults = minimumFoundFaults;
	}
	public int getMinimumFixedFailures() {
		return minimumFixedFailures;
	}
	public void setMinimumFixedFailures(int minimumFixedFailures) {
		this.minimumFixedFailures = minimumFixedFailures;
	}
}
