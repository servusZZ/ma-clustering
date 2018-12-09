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
		return clustersAreSimilar(getSimilarityValue(c1, c2));
	}
	@Override
	public double getSimilarityValue(Cluster c1, Cluster c2) {
		return MetricUtils.overlapSimilarity(c1.getSuspiciousSet(), c2.getSuspiciousSet());
	}
}
