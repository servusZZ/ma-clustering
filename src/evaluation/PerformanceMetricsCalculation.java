package evaluation;

public class PerformanceMetricsCalculation {

	public static double getOverallPerformanceIndex(double foundFaultsPerformance,
			double wastedEffortPerformance, double fixedFailuresPerformance) {
		return (foundFaultsPerformance*5.5 + wastedEffortPerformance*3 + fixedFailuresPerformance*1.5) / 10;
	}
	public static double getFoundFaultsPerformance(int foundFaults, int foundFaultsOptimal) {
		if (foundFaults >= foundFaultsOptimal) {
			return 1.0;
		}
		return ((double)foundFaults) / foundFaultsOptimal;
	}
	public static double getWastedEffortPerformance(int wastedEffort, int biggestWastedEffortPossible) {
		if (biggestWastedEffortPossible == 0) {
			return 1.0;
		}
		return 1 - (wastedEffort/(double)biggestWastedEffortPossible);
	}
	public static double getFixedFailuresPerformance(int fixedFailures, int fixedFailuresOptimal) {
		if (fixedFailures >= fixedFailuresOptimal) {
			return 1.0;
		}
		return ((double)fixedFailures)/fixedFailuresOptimal;
	}
}
