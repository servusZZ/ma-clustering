package hac.evaluation;

import java.util.Arrays;

import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.main.Cluster;

public class KNNToCenterSelection implements RepresentativeSelectionStrategy{

	@Override
	public TestCase selectRepresentative(Cluster c1, CustomDissimilarityMeasure dissimilarityMeasure) {
		double[] center = c1.getCenter(dissimilarityMeasure);
		System.out.println("DEBUG: center " + Arrays.toString(center));
		TestCase[] failures = c1.getFailedTCs();
		double minDistance = dissimilarityMeasure.computeDistanceToCenter(center, failures[0].numericCoverage);
		int minDistanceTCIndex = 0;
		for (int i = 1; i < failures.length; i++) {
			double tmpDistance = dissimilarityMeasure.computeDistanceToCenter(center, failures[i].numericCoverage);
			System.out.println("DEBUG: Diss Value for TC " + failures[i] + " to center is " + tmpDistance);
			if (tmpDistance < minDistance) {
				minDistance = tmpDistance;
				minDistanceTCIndex = i;
			}
		}
		System.out.println("INFO: TC  " + failures[minDistanceTCIndex] + " is the representative with the smallest diss value of " + minDistance);
		return failures[minDistanceTCIndex];
	}

}
