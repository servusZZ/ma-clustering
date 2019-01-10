package evaluation;

import org.apache.commons.lang3.StringUtils;

public class PerformanceEvaluationEntry {
	private double performanceIndex;
	/**	same as performanceIndex but excludes all cases for which it appears that the 
	 * optimal strategy is equal to the worst prioritization. */
	private double performanceIndexForCorrelationAnalysis;
	private double foundFaultsPerformance;
	private double wastedEffortPerformance;
	private double fixedFailuresPerformance;
	
	public PerformanceEvaluationEntry(EvaluationEntry metrics, OptimalEvaluationEntry optimalMetrics) {
		foundFaultsPerformance = PerformanceMetricsCalculation.getFoundFaultsPerformance(metrics.getFoundFaults(), optimalMetrics.getFoundFaults());
		wastedEffortPerformance = PerformanceMetricsCalculation.getWastedEffortPerformance(metrics.getWastedEffort(), optimalMetrics.getBiggestPossibleWastedEffort());
		fixedFailuresPerformance = PerformanceMetricsCalculation.getFixedFailuresPerformance(metrics.getFixedFailures(), optimalMetrics.getFixedFailures());
		performanceIndex = PerformanceMetricsCalculation.getOverallPerformanceIndex(foundFaultsPerformance, wastedEffortPerformance, fixedFailuresPerformance);
		initPerformanceIndexForCorrelationAnalysis();
	}
	public String getValues() {
		return getGermanRepresentation(performanceIndex) +
				 ";" + getGermanRepresentation(performanceIndexForCorrelationAnalysis) +
				 ";" + getGermanRepresentation(foundFaultsPerformance) +
				 ";" + getGermanRepresentation(wastedEffortPerformance) +
				 ";" + getGermanRepresentation(fixedFailuresPerformance);
	}
	private String getGermanRepresentation(double val) {
		return StringUtils.replaceChars("" + val, '.', ',');
	}
	private void initPerformanceIndexForCorrelationAnalysis() {
		if (Double.isNaN(fixedFailuresPerformance) || Double.isNaN(foundFaultsPerformance) || Double.isNaN(wastedEffortPerformance)) {
			performanceIndexForCorrelationAnalysis = Double.NaN;
			return;
		}
		performanceIndexForCorrelationAnalysis = performanceIndex;
	}
}
