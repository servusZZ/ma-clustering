package evaluation;

import hac.evaluation.ClusteringEvaluationEntry;
import prioritization.evaluation.ProjectEvaluationEntry;

/**
 * The class holds all evaluation information for one project
 */
public class EvaluationEntry {
	// metrics related to the prioritization strategy
	private String prioritizationStrategyName;
	private int foundFaults;
	private int fixedFailures;
	/** stores the number of failures that are (superfluously) investigated without finding a new fault	*/
	private int wastedEffort;
	private int investigatedFailures_actual;
	/**	metrics that indicate the relative performance of the prio strategy compared
	 * to the best possible prioritization. */
	private PerformanceEvaluationEntry performanceMetrics;
	// metrics related to the context
	private int investigatedFailures_planned;
	
	/** metrics related to the faulty project for which the prioritization is performed */
	private ProjectEvaluationEntry projectMetrics;
	
	/** optional: clustering metrics if the prioritization strategy is clustering */
	private ClusteringEvaluationEntry clusteringMetrics;
	
	/**
	 * Constructor. debuggingStrategy is set to "NA".
	 * @param clusteringMetrics optional, pass null if the strategy doesn't have clustering metrics
	 */
	public EvaluationEntry(String prioritizationStrategyName, int foundFaults, int fixedFailures,
			int wastedEffort, int investigatedFailures_actual, int investigatedFailures_planned,
			ProjectEvaluationEntry projectMetrics, ClusteringEvaluationEntry clusteringMetrics) {
		this.prioritizationStrategyName = prioritizationStrategyName;
		this.foundFaults = foundFaults;
		this.fixedFailures = fixedFailures;
		this.wastedEffort = wastedEffort;
		this.investigatedFailures_actual = investigatedFailures_actual;
		this.investigatedFailures_planned = investigatedFailures_planned;
		this.projectMetrics = projectMetrics;
		this.clusteringMetrics = clusteringMetrics;
	}

	public String getHeader() {
		return "PrioritizationStrategy;" + getPerformanceMetricsHeader() +
				";#FoundFaults;#FixedFailures;#WastedEffort;#InvestigatedFailuresActual;#InvestigatedFailuresPlanned;"
				+ projectMetrics.getHeader()
				+ ";" + getClusteringMetricsHeader();
	}
	private String getClusteringMetricsHeader() {
		return "DissimilarityMeasure;#Clusters;RepresentativeSelectionStrategy;#SuccessfulRepresentatives;#FailedRepresentatives;Purity;Precision;Recall;F1-Score;FaultEntropy";
	}
	private String getPerformanceMetricsHeader() {
		return "PerformanceIndex;PerformanceIndexCA;FoundFaultsPerformance;WastedEffortPerformance;FixedFailuresPerformance";
	}
	public String getValues() {
		return prioritizationStrategyName + 
				 ";" + getPerformanceMetricValues() +
				 ";" + foundFaults + ";" + fixedFailures +
				 ";" + wastedEffort + ";" + investigatedFailures_actual + ";" + investigatedFailures_planned +
				 ";" + projectMetrics.getValues() +
				 ";" + getClusteringMetricValues();
	}
	
	private String getClusteringMetricValues() {
		if (clusteringMetrics != null) {
			return clusteringMetrics.getValues();
		}
		return ";;;;;;;;;";
	}
	private String getPerformanceMetricValues() {
		if (performanceMetrics != null) {
			return performanceMetrics.getValues();
		}
		return ";;;;";
	}

	public int getFoundFaults() {
		return foundFaults;
	}
	public int getFixedFailures() {
		return fixedFailures;
	}
	public int getWastedEffort() {
		return wastedEffort;
	}
	public void setPerformanceMetrics(PerformanceEvaluationEntry performanceMetrics) {
		this.performanceMetrics = performanceMetrics;
	}
}
