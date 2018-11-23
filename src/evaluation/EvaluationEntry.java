package evaluation;

import hac.evaluation.ClusteringEvaluationEntry;

/**
 * The class holds all evaluation information for one project
 */
public class EvaluationEntry {
	// metrics related to the prioritization strategy
	private String prioritizationStrategyName;
	private int foundFaults;
	private int fixedFailures;
	private int investigatedFailures_actual;
	// metrics related to the context
	private int investigatedFailures_planned;
	private String debuggingStrategy;
	
	/** metrics related to the faulty project for which the prioritization is performed */
	private ProjectEvaluationEntry projectMetrics;
	
	/** optional: clustering metrics if the prioritization strategy is clustering */
	private ClusteringEvaluationEntry clusteringMetrics;
	
	/**
	 * Constructor. debuggingStrategy is set to "NA".
	 * @param clusteringMetrics optional, pass null if the strategy doesn't have clustering metrics
	 */
	public EvaluationEntry(String prioritizationStrategyName, int foundFaults, int fixedFailures,
			int investigatedFailures_actual, int investigatedFailures_planned,
			ProjectEvaluationEntry projectMetrics, ClusteringEvaluationEntry clusteringMetrics) {
		this.prioritizationStrategyName = prioritizationStrategyName;
		this.foundFaults = foundFaults;
		this.fixedFailures = fixedFailures;
		this.investigatedFailures_actual = investigatedFailures_actual;
		this.investigatedFailures_planned = investigatedFailures_planned;
		this.debuggingStrategy = "NA";
		this.projectMetrics = projectMetrics;
		this.clusteringMetrics = clusteringMetrics;
	}
	
	public String getHeader() {
		return "PrioritizationStrategy;#FoundFaults;#FixedFailures;#InvestigatedFailuresActual;#InvestigatedFailuresPlanned;DebuggingStrategy"
				+ ";" + projectMetrics.getHeader()
				+ ";" + getClusteringMetricsHeader();
	}
	private String getClusteringMetricsHeader() {
		return "DissimilarityMeasure;#Clusters;RepresentativeSelectionStrategy;#SuccessfulRepresentatives;#FailedRepresentatives;Purity;Precision;Recall;F1-Score;FaultEntropy";
	}
	public String getValues() {
		return prioritizationStrategyName + ";" + foundFaults + ";" + fixedFailures +
				 ";" + investigatedFailures_actual + ";" + investigatedFailures_planned +
				 ";" + debuggingStrategy +
				 ";" + projectMetrics.getValues() +
				 ";" + getClusteringMetricValues();
	}
	
	private String getClusteringMetricValues() {
		if (clusteringMetrics != null) {
			return clusteringMetrics.getValues();
		}
		return ";;;;;;;;;";
	}
}
