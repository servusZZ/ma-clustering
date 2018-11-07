package hac.experiment.custom;

import java.util.Iterator;
import java.util.List;

import data_objects.TestCase;
import main.Main;

public class AverageCenterCalculation implements ICenterCalculation {

	/**
	 * Sums up the absolute distance from each point to the center.
	 */
	public double computeDistanceToCenter(double[] center, int[] tc) {
		double diff = 0;
		for (int i = 0; i < Main.methodsCount; i++) {
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
