package hac.experiment.custom;

import java.util.List;

import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import utils.MetricUtils;

public class JaccardDistance extends CustomDissimilarityMeasure{

	public JaccardDistance(ICenterCalculation centerCalculation) {
		super(centerCalculation);
	}

	@Override
	public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {
		TestCase tc1 = (TestCase) experiment.getObservation(observation1);
		TestCase tc2 = (TestCase) experiment.getObservation(observation2);
		double similarity = MetricUtils.jaccardSetSimilarity(tc1.coveredMethods, tc2.coveredMethods);
		double distance = 1-similarity;
		return distance;
	}

	@Override
	public double computeDistanceToCenter(double[] center, int[] tc) {
		return centerCalculation.computeDistanceToCenter(center, tc);
	}

	@Override
	public double[] computeCenter(List<TestCase> failedTCs) {
		return centerCalculation.computeCenter(failedTCs);
	}

}
