package hac.sbfl;


import hac.data_objects.Cluster;
import utils.MetricUtils;

public class JaccardSBFLConfiguration extends SBFLConfiguration{

	@Override
	public boolean clustersAreSimilar(Cluster c1, Cluster c2) {
		boolean result = clustersAreSimilar(getSimilarityValue(c1, c2));
		clusterComparisonEvaluation.evaluateComparison(result, c1, c2);
		return result;
	}

	@Override
	public double getSimilarityValue(Cluster c1, Cluster c2) {
		double simValue = MetricUtils.jaccardSetSimilarity(c1.getSuspiciousSet(), c2.getSuspiciousSet());
		boolean result = clustersAreSimilar(simValue);
		clusterComparisonEvaluation.evaluateComparison(result, c1, c2);
		return simValue;
	}

}
