package priorization.main;

import java.util.List;
import java.util.Set;

import evaluation.EvaluationEntry;
import evaluation.EvaluationUtils;
import hac.evaluation.ClusteringEvaluationEntry;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import prioritization.evaluation.ProjectEvaluationEntry;

public abstract class PrioritizationStrategyBase {
	protected TestCase[] failures;
	protected TestCase[] passedTCs;
	protected Set<Fault> faults;
	protected List<TestCase> prioritizedFailures;
	protected String strategyName = "StrategyBaseClass";
	protected EvaluationUtils evaluationHelper;
	protected ClusteringEvaluationEntry clusteringMetrics;
	
	
	public PrioritizationStrategyBase(TestCase[] failures,TestCase[] passedTCs, Set<Fault> faults) {
		this.failures = failures;
		this.passedTCs = passedTCs;
		this.faults = faults;
		evaluationHelper = new EvaluationUtils();
		clusteringMetrics = null;
	}
	/**
	 * Returns a prioritized order of all failures in which they should be investigated according
	 * to the concrete prioritization strategy.
	 */
	public abstract void prioritizeFailures();
	
	/**
	 * Evaluates the strategy and returns a respective EvaluationEntry object which can
	 * be written to the output file. The method prioritizeFailures must be called before
	 * calling this method.
	 * The clusteringMetrics in the returned EvaluationEntry object are null by default and
	 * must be set within the prioritizeFailures method in respective subclasses.
	 */
	public EvaluationEntry evaluatePrioritizationStrategy(int failuresToInvestigateCount, ProjectEvaluationEntry projectMetrics) {
		int investigatedFailuresActual = failuresToInvestigateCount;
		if (prioritizedFailures.size() < failuresToInvestigateCount) {
			investigatedFailuresActual = prioritizedFailures.size();
		}
		Set<Fault> foundFaults = evaluationHelper.getFoundFaults(prioritizedFailures.subList(0, investigatedFailuresActual));
		Set<TestCase> fixedFailures = evaluationHelper.getFixedFailures(foundFaults, failures);
		EvaluationEntry metrics = new EvaluationEntry(strategyName, foundFaults.size(),
				fixedFailures.size(), investigatedFailuresActual, failuresToInvestigateCount,
				projectMetrics, clusteringMetrics);
		return metrics;
	}
}
