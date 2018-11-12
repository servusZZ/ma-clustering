package priorization.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import data_import.gzoltars.GzoltarDataReader;
import data_objects.Fault;
import data_objects.TestCase;
import hac.main.HierarchicalAgglomerativeClustering;
import utils.DataImportUtils;

public class Main {
	public static final double MOST_SUSP_THRESHOLD = 0.2;
	public static final int MOST_SUSP_MAX_COUNT = 15;
	public static final double SIMILARITY_THRESHOLD = 0.85;
	//TODO:
	//	sinnvolle Berechnung für MOST_SUSP_SET überlegen
	//	nur Werte aufnehmen, welche nicht 0.0 sind?
	//		dann muss ich berücksichtigen, wenn 2 sets unterschiedliche Größe haben
	//	MOST_SUSP_MAX_COUNT verwenden?
	//		
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	public static final String BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\";
	public static final String PROJECT_DIR = "gzoltars\\Lang\\37\\";
	
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static int failuresCount = 0;
	public static int passedTestsCount = 0;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Program started...");
		List<TestCase> testCasesList = GzoltarDataReader.importTestCases();
		Set<Fault> faults = GzoltarDataReader.importFaults();
		TestCase[] testCases = new TestCase[testCasesList.size()];
		testCasesList.toArray(testCases);
		testsCount = testCases.length;
		if (methodsCount == 0) {
			// methodsCount should get set by the class that imports the data
			methodsCount = testCases[0].coverage.length;
		}
		System.out.print("Imported " + testCases.length + " test cases, ");
		List<TestCase> failedTCsList = new ArrayList<TestCase>();
		List<TestCase> passedTCsList = new ArrayList<TestCase>();
		DataImportUtils.splitTestCases(testCasesList, failedTCsList, passedTCsList);
		failuresCount = failedTCsList.size();
		TestCase[] failures = new TestCase[failuresCount];
		failedTCsList.toArray(failures);
		TestCase[] passedTCs = new TestCase[passedTCsList.size()];
		passedTCsList.toArray(passedTCs);
		System.out.println(failuresCount + " of them are Failures.");
		DataImportUtils.addFaultsToFailures(failedTCsList, faults);
		System.out.println("Imported " + faults.size() + " underlying faults.");
		
		System.out.println("Perform hierarchical agglomerative clustering of Failures...");
		PrioritizationStrategyBase hac = new HierarchicalAgglomerativeClustering(testCases, failures, passedTCs, faults);
		List<TestCase> prioritizedFailures = hac.prioritizeFailures();
		
		}
	



}
