package hac.experiment.custom;

import data_objects.TestCase;

public interface ICenterCalculation {
	public double computeDistanceToCenter(double[] center, int[] tc);
	public double[] computeCenter(TestCase[] failedTCs);
}
