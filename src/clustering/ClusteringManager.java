package clustering;

import java.util.ArrayList;
import java.util.List;

import data_objects.PassedTCsCluster;
import data_objects.TestCase;

public class ClusteringManager {
	private List<TestCase> testCases;
	private PassedTCsCluster passedTCsCluster;
	public ClusteringManager(List<TestCase> testCases) {
		this.testCases = testCases;
		initPassedTCsCluster();
	}
	
	public void runClustering() {
		System.out.println("Initialized Passed TC Cluster with " + passedTCsCluster.passedTCs.size() + " test cases.");
		printResults();
		return;
	}
	private void printResults() {
		System.out.println("Clustering finished!");
		System.out.println("Printing results...");
		return;
	}
	private void initPassedTCsCluster() {
		List<TestCase> passedTestCases = new ArrayList<TestCase>();
		for (TestCase tc : testCases) {
			if (tc.passed) {
				passedTestCases.add(tc);
			}
		}
		passedTCsCluster = new PassedTCsCluster(passedTestCases);
	}
}
