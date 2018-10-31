package evaluation;

import clustering.Cluster;
import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;

public interface RepresentativeSelectionStrategy {
	public TestCase selectRepresentative(Cluster c1, CustomDissimilarityMeasure dis);
}
