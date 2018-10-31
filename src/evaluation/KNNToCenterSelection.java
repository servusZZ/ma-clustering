package evaluation;

import java.util.Arrays;
import java.util.List;

import clustering.Cluster;
import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;

public class KNNToCenterSelection implements RepresentativeSelectionStrategy{

	@Override
	public TestCase selectRepresentative(Cluster c1, CustomDissimilarityMeasure dissimilarityMeasure) {
		double[] center = c1.getCenter(dissimilarityMeasure);
		System.out.println("DEBUG: center " + Arrays.toString(center));
		TestCase[] failures = new TestCase[c1.getFailedTCs().size()];
		c1.getFailedTCs().toArray(failures);
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
