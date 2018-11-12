package hac.experiment.custom;

import org.apache.commons.lang3.NotImplementedException;

import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import priorization.main.Main;

public class EuclideanDistance extends CustomDissimilarityMeasure{
	
	public EuclideanDistance(ICenterCalculation centerCalculation) {
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
		return Math.sqrt(diff);
	}

	@Override
	public double computeDistanceToCenter(double[] center, int[] tc) {
		throw new NotImplementedException("Not implemented to compute distance for euclidean distance");
		//return Math.sqrt(diff);
	}

	@Override
	public double[] computeCenter(TestCase[] failedTCs) {
		throw new NotImplementedException("Not implemented to compute center for euclidean distance");
	}
}
