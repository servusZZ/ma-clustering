package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.statements.Fail;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.AverageLinkage;
import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.dendrogram.DendrogramNode;
import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;
import hac.experiment.custom.CountDiffsDistance;
import hac.experiment.custom.FailureClusteringExperiment;
import input.GzoltarCsvReader;
import unused.AgglomerativeHierarchicalClustering;
import unused.ClusteringManager;
import unused.ClusteringStrategy;

public class Main {
	public static final double MOST_SUSP_THRESHOLD = 0.2;
	public static final double SIMILARITY_THRESHOLD = 0.85;
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static int failuresCount = 0;
	public static int passedTestsCount = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Program started...");
		List<TestCase> testCases = GzoltarCsvReader.importTestCases();
		testsCount = testCases.size();
		methodsCount = testCases.get(0).coverage.length;
		System.out.print("Imported " + testCases.size() + " test cases, ");
		List<TestCase> failedTCsList = new ArrayList<TestCase>();
		List<TestCase> passedTCs = new ArrayList<TestCase>();
		splitTestCases(testCases, failedTCsList, passedTCs);
		TestCase[] failures = new TestCase[failedTCsList.size()];
		failedTCsList.toArray(failures);
		System.out.println(failures.length + " of them are Failures.");
		System.out.println("Perform agglomerative hierarchical clustering of Failures...");
		Experiment experiment = new FailureClusteringExperiment(failures);
		DissimilarityMeasure dissimilarityMeasure = new CountDiffsDistance();
		AgglomerationMethod agglomerationMethod = new AverageLinkage();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
		//TODO: create actual clusters
		
		
		DendrogramNode root = dendrogram.getRoot();
		System.out.println("Finished clustering Failures. Observations: " + root.getObservationCount());
		dendrogram.dump();
		
		//TODO: evaluate clusters: check if representative leads to main fault in cluster
		//		check if all faults are found
		
	}
	
	private static TestCase[] getFailedTCs(final List<TestCase> testCases) {
		List<TestCase> failedTCs = new ArrayList<TestCase>();
		
		for(TestCase tc: testCases) {
			if(!tc.passed) {
				failedTCs.add(tc);
			}
		}
		TestCase[] failures = new TestCase[failedTCs.size()];
		return failedTCs.toArray(failures);
	}
	private static void splitTestCases(final List<TestCase> testCases, List<TestCase> failedTCs, List<TestCase> passedTCs) {
		for(TestCase tc: testCases) {
			if(tc.passed) {
				passedTCs.add(tc);
			} else {
				failedTCs.add(tc);
			}
		}
	}

}
