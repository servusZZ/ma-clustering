package hac.experiment.custom;

import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import priorization.main.Main;

public class CountDiffsDistance extends CustomDissimilarityMeasure {

	public CountDiffsDistance(ICenterCalculation centerCalculation) {
		super(centerCalculation);
	}

	@Override
	public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {
		TestCase tc1 = (TestCase) experiment.getObservation(observation1);
		TestCase tc2 = (TestCase) experiment.getObservation(observation2);
		double diff = 0;
		for (int i = 0; i < Main.methodsCount; i++) {
			if(tc1.coverage[i] != tc2.coverage[i]) {
				diff++;
			}
		}
		return diff;
	}

	@Override
	public double computeDistanceToCenter(double[] center, int[] tc) {
		return centerCalculation.computeDistanceToCenter(center, tc);
	}

	/**
	 * Sum up the coverage values and divide by the number of Failures for each method.
	 */
	@Override
	public double[] computeCenter(TestCase[] failedTCs) {
		return centerCalculation.computeCenter(failedTCs);
	}
}
