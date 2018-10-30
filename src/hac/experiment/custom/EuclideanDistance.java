package hac.experiment.custom;

import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import main.Main;

public class EuclideanDistance implements DissimilarityMeasure{
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
}
