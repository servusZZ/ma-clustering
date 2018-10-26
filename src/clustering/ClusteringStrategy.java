package clustering;

import java.util.List;

import data_objects.PassedTCsCluster;
import data_objects.TestCase;

public interface ClusteringStrategy {
	public void performClustering(List<TestCase> testCases, PassedTCsCluster passedTCsCluster);
}
