package evaluation;

public class PerformanceMetricsCalculation {

	public static double getOverallPerformanceIndex(double foundFaultsPerformance,
			double wastedEffortPerformance, double fixedFailuresPerformance) {
		if (Double.isNaN(wastedEffortPerformance)) {
			wastedEffortPerformance = 1.0;
		}
		return (foundFaultsPerformance*5.5 + wastedEffortPerformance*3 + fixedFailuresPerformance*1.5) / 10;
	}
	public static double getFoundFaultsPerformance(int foundFaults, int foundFaultsOptimal) {
		if (foundFaults >= foundFaultsOptimal) {
			return 1.0;
		}
		return ((double)foundFaults) / (foundFaultsOptimal);
	}
	public static double getWastedEffortPerformance(int wastedEffort, int biggestWastedEffortPossible) {
		if (biggestWastedEffortPossible == 0) {
			return Double.NaN;
		}
// 		case can happen if a failure with 2 underlying faults is found first
		if (wastedEffort >= biggestWastedEffortPossible) {
			return 0;
		}
		return 1 - (wastedEffort/(double)biggestWastedEffortPossible);
	}
	public static double getFixedFailuresPerformance(int fixedFailures, int fixedFailuresOptimal) {
//		 case fixedFailuresOptimal == 0 can happen if all failures caused by the biggest fault
//		 have multiple underlying faults but only 1 of the faults was found.
		if (fixedFailuresOptimal == 0 || fixedFailures >= fixedFailuresOptimal) {
			return 1.0;
		}
		return ((double)fixedFailures)/fixedFailuresOptimal;
	}
}
