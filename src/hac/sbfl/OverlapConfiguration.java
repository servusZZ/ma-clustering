package hac.sbfl;


import hac.data_objects.Cluster;

import utils.MetricUtils;
/**
 * see doc of superclass
 */
public class OverlapConfiguration extends SBFLConfiguration{

	/**
	 * Compares two clusters.
	 * 		True,  iff similarity > Threshold
	 * 		False, iff similarity <= Threshold
	 */
	@Override
	public boolean clustersAreSimilar(Cluster c1, Cluster c2) {
		boolean result = clustersAreSimilar(getSimilarityValue(c1, c2));
		clusterComparisonEvaluation.evaluateComparison(result, c1, c2);
		return result;
	}
	@Override
	public double getSimilarityValue(Cluster c1, Cluster c2) {
		double simValue = MetricUtils.overlapSimilarity(c1.getSuspiciousSet(), c2.getSuspiciousSet());
		boolean result = clustersAreSimilar(simValue);
		clusterComparisonEvaluation.evaluateComparison(result, c1, c2);
		return simValue;
	}
}
