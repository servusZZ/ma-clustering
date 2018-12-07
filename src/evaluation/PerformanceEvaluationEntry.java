package evaluation;

import org.apache.commons.lang3.StringUtils;

public class PerformanceEvaluationEntry {
	private double performanceIndex;
	private double foundFaultsPerformance;
	private double wastedEffortPerformance;
	private double fixedFailuresPerformance;
	
	public PerformanceEvaluationEntry(EvaluationEntry metrics, EvaluationEntry optimalMetrics) {
		foundFaultsPerformance = PerformanceMetricsCalculation.getFoundFaultsPerformance(metrics.getFoundFaults(), optimalMetrics.getFoundFaults());
		wastedEffortPerformance = PerformanceMetricsCalculation.getWastedEffortPerformance(metrics.getWastedEffort(), optimalMetrics.getBiggestPossibleWastedEffort());
		fixedFailuresPerformance = PerformanceMetricsCalculation.getFixedFailuresPerformance(metrics.getFixedFailures(), optimalMetrics.getFixedFailures());
		performanceIndex = PerformanceMetricsCalculation.getOverallPerformanceIndex(foundFaultsPerformance, wastedEffortPerformance, fixedFailuresPerformance);
	}
	public String getValues() {
		return getGermanRepresentation(performanceIndex) +
				 ";" + getGermanRepresentation(foundFaultsPerformance) +
				 ";" + getGermanRepresentation(wastedEffortPerformance) +
				 ";" + getGermanRepresentation(fixedFailuresPerformance);
	}
	private String getGermanRepresentation(double val) {
		return StringUtils.replaceChars("" + val, '.', ',');
	}
}
