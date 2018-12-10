package hac.evaluation;

import hac.data_objects.Cluster;

/**
 * Whenever 2 clusters are compared, this class evaluates the result of the comparison.
 * 'similar' = all Major faults of clusterA are also Major faults of clusterB
 */
public class ClusterComparingEvaluation {
	/**	2 clusters are similar and the comparison returns similar */
	private int TP = 0;
	/**	2 clusters are different and the comparison returns different */
	private int TN = 0;
	/**	2 clusters are different and the comparison returns similar */
	private int FP = 0;
	/**	2 clusters are similar and the comparison returns different */
	private int FN = 0;

	public void evaluateComparison(boolean returnedSimilar, Cluster c1, Cluster c2) {
		boolean expectedSimilar = getExpectedSimilarity(c1, c2);
		if (expectedSimilar == returnedSimilar) {
			if (returnedSimilar) {
				TP++; return;
			}
			TN++; return;
		}
		else {
			if (returnedSimilar) {
				FP++; return;
			}
			FN++; return;
		}
	}
	private boolean getExpectedSimilarity(Cluster c1, Cluster c2) {
		if (c1.getMajorFaults().containsAll(c2.getMajorFaults()) ||
				c2.getMajorFaults().containsAll(c1.getMajorFaults())) {
			return true;
		}
		return false;
	}
	public int getTP() {
		return TP;
	}
	public int getFP() {
		return FP;
	}
	public int getTN() {
		return TN;
	}
	public int getFN() {
		return FN;
	}
}
