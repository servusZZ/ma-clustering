package hac.experiment.custom;

import java.util.Iterator;

import data_objects.TestCase;
import priorization.main.AnalysisWrapper;

public class AverageCenterCalculation implements ICenterCalculation {

	/**
	 * Sums up the absolute distance from each point to the center.
	 */
	public double computeDistanceToCenter(double[] center, int[] tc) {
		double diff = 0;
		for (int i = 0; i < AnalysisWrapper.methodsCount; i++) {
			diff += Math.abs(center[i] - tc[i]);
		}
		return diff;
	}

	/**
	 * Computes the center of a cluster as the point that takes for each method as value
	 * the average value of all method values of the TCs in the cluster:
	 * 		m1 is covered by 3 Test Cases and not covered by 2
	 * 		m1.value = 3/5
	 */
	public double[] computeCenter(TestCase[] failedTCs) {
		double[] center = new double[AnalysisWrapper.methodsCount];
		for (int i = 0; i < failedTCs.length; i++) {
			Iterator<Integer> coveredMethods = failedTCs[i].coveredMethods.iterator();
			while (coveredMethods.hasNext()) {
				center[coveredMethods.next()] += ((double)1)/failedTCs.length;
			}
		}
		return center;
	}

}
