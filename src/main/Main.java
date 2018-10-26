package main;

import java.io.IOException;
import java.util.List;

import clustering.AgglomerativeHierarchicalClustering;
import clustering.ClusteringManager;
import clustering.ClusteringStrategy;
import data_objects.TestCase;
import input.GzoltarCsvReader;

public class Main {
	public static final double MOST_SUSP_THRESHOLD = 0.2;
	public static final double SIMILARITY_THRESHOLD = 0.8;
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Program started...");
		List<TestCase> testCases = GzoltarCsvReader.importTestCases();
		testsCount = testCases.size();
		methodsCount = testCases.get(0).coverage.length;
		System.out.println("Imported " + testCases.size() + " test cases");
		ClusteringStrategy clStrategy = new AgglomerativeHierarchicalClustering();
		ClusteringManager cm = new ClusteringManager(testCases, clStrategy);
		cm.runClustering();
		//TODO: Start Clustering, compute Metric algorithm
		System.out.println("Program finished!");
	}

}
