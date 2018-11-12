package hac.evaluation;

import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.main.Cluster;

public interface RepresentativeSelectionStrategy {
	public TestCase selectRepresentative(Cluster c1, CustomDissimilarityMeasure dis);
}
