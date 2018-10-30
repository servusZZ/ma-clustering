package clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data_objects.PassedTCsCluster;
import data_objects.TestCase;

public class AgglomerativeHierarchicalClustering implements ClusteringStrategy{

	public void performClustering(final List<TestCase> testCases, PassedTCsCluster passedTCsCluster) {
		List<TestCase> failedTCs = getFailedTCs(testCases);
		List<Cluster> clusters = initFailedTCsClusters(failedTCs, passedTCsCluster);
		//TODO: go on here
		// nochmal [4] prüfen wie genau geclustered wird
		//			compare SuspSet O(n^2)
		//			pro Step: 2 ähnlichsten mergen
		//			update für das neue cluster
		//			delete Sim-Werte für gelöschtes Cluster, compare alle zum neuen cluster
		
		System.out.println("Initialized " + clusters.size() + " Failed TC Clusters.");
	}
	private List<Cluster> initFailedTCsClusters(List<TestCase> failedTCs, PassedTCsCluster passedTCsCluster){
		List<Cluster> clusters = new ArrayList<Cluster>();
		for(TestCase failedTC: failedTCs) {
			clusters.add(new Cluster(Arrays.asList(failedTC), passedTCsCluster.getMethodDStarTerms()));
		}
		return clusters;
	}
	private List<TestCase> getFailedTCs(final List<TestCase> testCases) {
		List<TestCase> failedTCs= new ArrayList<TestCase>();
		for(TestCase tc: testCases) {
			if(!tc.passed) {
				failedTCs.add(tc);
			}
		}
		return failedTCs;
	}


}
