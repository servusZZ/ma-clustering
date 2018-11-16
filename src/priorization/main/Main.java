package priorization.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import data_export.PitMergedMutationsWriter;
import data_import.gzoltars.GzoltarDataReader;
import data_import.pit.PitMergedMutationsReader;
import data_import.pit.data_objects.PitMethod;
import data_objects.Fault;
import data_objects.TestCase;
import evaluation.PrioritizationEvaluation;
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
	//TODO: Auf MAX_SUSP_VALUE Anzahl an FailingTests addieren, in denen die methode vorkommt? 
	
	public static final String BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\";
	public static final String PROJECT_DIR = "gzoltars\\Tests\\testMedium\\";
	
	public static final String OUTPUT_DIR = "C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\";
	public static final int FAILURES_TO_INVESTIGATE = 5;
	
	public static int testsCount = 0;
	public static int methodsCount = 0;
	
	public static int failuresCount = 0;
	public static int passedTestsCount = 0;
	
	public static void main(String[] args) throws IOException {
		// TODO: Go on here, Restructure Main
		//		for each project
		//			K = 100 Times
		//				prepare faulty version
		//				runAnalysis
		
		System.out.println("Program started...");
		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_tests\\test_small\\pit-data\\";
		List<PitMethod> methods = PitMergedMutationsReader.readPitMergedMethods(dir);
		System.out.println("DEBUG: Imported " + methods.size() + " PitMethods");
		/*List<TestCase> testCasesList = GzoltarDataReader.importTestCases();
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
		System.out.println("Prioritized Failures: " + prioritizedFailures);
		System.out.println("Write Evaluation to output file.");
		PrioritizationEvaluation eval = new PrioritizationEvaluation(faults, failures, FAILURES_TO_INVESTIGATE);
		eval.evaluatePrioritizationStrategy(prioritizedFailures, "HAC");	*/
		}
}
