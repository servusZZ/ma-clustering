package hac.experiment.custom;

import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import data_objects.TestCase;

public abstract class CustomDissimilarityMeasure implements DissimilarityMeasure{
	protected ICenterCalculation centerCalculation;
	
	public CustomDissimilarityMeasure(ICenterCalculation centerCalculation) {
		this.centerCalculation = centerCalculation;
	}
	public abstract double computeDistanceToCenter(double[] center, int[] tc);
	public abstract double[] computeCenter(TestCase[] failedTCs);
}
