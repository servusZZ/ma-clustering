package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.AverageLinkage;
import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import clustering.Cluster;
import clustering.ClusterBuilder;
import data_objects.Fault;
import data_objects.TestCase;
import evaluation.EvaluationManager;
import evaluation.KNNToCenterSelection;
import hac.experiment.custom.CountDiffsDistance;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.experiment.custom.FailureClusteringExperiment;
import input.FaultFailureMappingReader;
import input.GzoltarCsvReader;

public class Main {
	public static final double MOST_SUSP_THRESHOLD = 0.2;
	public static final double SIMILARITY_THRESHOLD = 0.85;
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	public static final String BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\";
	public static final String PROJECT_DIR = "gzoltars\\Lang\\37\\";
	
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static int failuresCount = 0;
	public static int passedTestsCount = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Program started...");
		List<TestCase> testCases = GzoltarCsvReader.importTestCases();
		testsCount = testCases.size();
		if (methodsCount == 0) {
			// methodsCount should get set by the class that imports the data
			methodsCount = testCases.get(0).coverage.length;
		}
		System.out.print("Imported " + testCases.size() + " test cases, ");
		List<TestCase> failedTCsList = new ArrayList<TestCase>();
		List<TestCase> passedTCs = new ArrayList<TestCase>();
		splitTestCases(testCases, failedTCsList, passedTCs);
		TestCase[] failures = new TestCase[failedTCsList.size()];
		failedTCsList.toArray(failures);
		System.out.println(failures.length + " of them are Failures.");
		Set<Fault> faults = FaultFailureMappingReader.importFaults();
		FaultFailureMappingReader.addFaultsToFailures(failedTCsList, faults);
		System.out.println("Imported " + faults.size() + " underlying faults.");
		
		System.out.println("Perform agglomerative hierarchical clustering of Failures...");
		Experiment experiment = new FailureClusteringExperiment(failures);
		DissimilarityMeasure dissimilarityMeasure = new CountDiffsDistance();
		AgglomerationMethod agglomerationMethod = new AverageLinkage();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
		System.out.println("Finished clustering Failures! Dump Dendrogram: ");
		dendrogram.dump();
		
		System.out.println("Determine cutting point of the Failure Tree...");
		ClusterBuilder cb = new ClusterBuilder(dendrogram.getRoot(), passedTCs, failures);
		List<Cluster> clusters = cb.getClustersOfCuttingLevel();
		System.out.println("Created " + clusters.size() + " clusters");
		dumpClusters(clusters);
		
		System.out.println("Evaluate the Clustering...");
		EvaluationManager em = new EvaluationManager((CustomDissimilarityMeasure)dissimilarityMeasure, new KNNToCenterSelection(), clusters, faults);
		em.evaluateClustering();
		// TODO:
		//		Prüfen, warum in faults_components.csv mapping nur .lang. statt .lang3. angegeben ist?
		//		check if evaluation works, check log
		//		provide more metrics
		//		prepare more gzoltar projects
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
	private static void dumpClusters(List<Cluster> clusters) {
		for (Cluster cl: clusters) {
			cl.dump();
		}
	}

}
