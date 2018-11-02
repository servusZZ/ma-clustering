package hac.experiment.custom;

import java.util.List;

import data_objects.TestCase;

public interface CustomDissimilarityMeasure {
	public double computeDistanceToCenter(double[] center, int[] tc);
	public double[] computeCenter(List<TestCase> failedTCs);
}