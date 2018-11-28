package hac.evaluation;

import hac.data_objects.Cluster;
import hac.experiment.custom.CustomDissimilarityMeasure;
import prioritization.data_objects.TestCase;

public interface RepresentativeSelectionStrategy {
	public TestCase selectRepresentative(Cluster c1, CustomDissimilarityMeasure dis);
}
