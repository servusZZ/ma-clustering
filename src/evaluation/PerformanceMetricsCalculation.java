package evaluation;

public class PerformanceMetricsCalculation {

	public static double getOverallPerformanceIndex(double foundFaultsPerformance,
			double wastedEffortPerformance, double fixedFailuresPerformance) {
		if (Double.isNaN(foundFaultsPerformance)) {
			foundFaultsPerformance = 1.0;
		} if (Double.isNaN(fixedFailuresPerformance)) {
			fixedFailuresPerformance = 1.0;
		} if (Double.isNaN(wastedEffortPerformance)) {
			wastedEffortPerformance = 1.0;
		}
		return (foundFaultsPerformance*5.5 + wastedEffortPerformance*3 + fixedFailuresPerformance*1.5) / 10;
	}
	public static double getFoundFaultsPerformance(int foundFaults, int foundFaultsOptimal, int foundFaultsMinimal) {
		if (foundFaultsOptimal == foundFaultsMinimal) {
			return Double.NaN;
		}
		if (foundFaults >= foundFaultsOptimal) {
			return 1.0;
		}
		return ((double)foundFaults-foundFaultsMinimal) / (foundFaultsOptimal-foundFaultsMinimal);
	}
	public static double getWastedEffortPerformance(int wastedEffort, int biggestWastedEffortPossible) {
		if (biggestWastedEffortPossible == 0) {
			return Double.NaN;
		}
		return 1 - (wastedEffort/(double)biggestWastedEffortPossible);
	}
	public static double getFixedFailuresPerformance(int fixedFailures, int fixedFailuresOptimal, int fixedFailuresMinimal) {
//		 case fixedFailuresOptimal == 0 can happen if all failures caused by the biggest fault
//		 have multiple underlying faults but only 1 of the faults was found.
		if (fixedFailuresOptimal == 0 || fixedFailuresOptimal == fixedFailuresMinimal) {
			return Double.NaN;
		}
		if (fixedFailures >= fixedFailuresOptimal) {
			return 1.0;
		}
		return ((double)fixedFailures - fixedFailuresMinimal)/(fixedFailuresOptimal-fixedFailuresMinimal);
	}
}
