package clustering;

import java.util.ArrayList;
import java.util.List;

import data_objects.PassedTCsCluster;
import data_objects.TestCase;

public class ClusteringManager {
	private List<TestCase> testCases;
	private PassedTCsCluster passedTCsCluster;
	private ClusteringStrategy clStrategy;
	public ClusteringManager(List<TestCase> testCases, ClusteringStrategy clStrategy) {
		this.testCases = testCases;
		this.clStrategy = clStrategy;
		initPassedTCsCluster();
	}
	
	public void runClustering() {
		System.out.println("Initialized Passed TC Cluster with " + passedTCsCluster.passedTCs.size() + " test cases.");
		clStrategy.performClustering(testCases, passedTCsCluster);
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
