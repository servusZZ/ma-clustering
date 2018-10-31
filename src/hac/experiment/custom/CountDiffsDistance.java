package hac.experiment.custom;

import java.util.Iterator;
import java.util.List;

import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import main.Main;

public class CountDiffsDistance implements DissimilarityMeasure, CustomDissimilarityMeasure {

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
		double diff = 0;
		for (int i = 0; i < Main.methodsCount; i++) {
			diff += Math.abs(center[i] - tc[i]);
		}
		return diff;
	}

	/**
	 * Sum up the coverage values and divide by the number of Failures for each method.
	 */
	@Override
	public double[] computeCenter(List<TestCase> failedTCs) {
		double[] center = new double[Main.methodsCount];
		for (int i = 0; i < failedTCs.size(); i++) {
			Iterator<Integer> coveredMethods = failedTCs.get(i).coveredMethods.iterator();
			while (coveredMethods.hasNext()) {
				center[coveredMethods.next()] += ((double)1)/failedTCs.size();
			}
		}
		return center;
	}
}
