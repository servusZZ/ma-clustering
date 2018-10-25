package main;

import java.io.IOException;
import java.util.List;

import clustering.ClusteringManager;
import data_objects.TestCase;
import input.GzoltarCsvReader;

public class Main {
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Program started...");
		List<TestCase> testCases = GzoltarCsvReader.importTestCases();
		testsCount = testCases.size();
		methodsCount = testCases.get(0).coverage.length;
		System.out.println("Imported " + testCases.size() + " test cases");
		
		ClusteringManager cm = new ClusteringManager(testCases);
		cm.runClustering();
		//TODO: Start Clustering, compute Metric algorithm
		System.out.println("Program finished!");
	}

}
