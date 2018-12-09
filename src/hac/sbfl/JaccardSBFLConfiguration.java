package hac.sbfl;


import hac.data_objects.Cluster;
import utils.MetricUtils;

public class JaccardSBFLConfiguration extends SBFLConfiguration{

	@Override
	public boolean clustersAreSimilar(Cluster c1, Cluster c2) {
		return clustersAreSimilar(getSimilarityValue(c1, c2));
	}

	@Override
	public double getSimilarityValue(Cluster c1, Cluster c2) {
		return MetricUtils.jaccardSetSimilarity(c1.getSuspiciousSet(), c2.getSuspiciousSet());
	}

}
