package priorization.main;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import data_export.evaluation.EvaluationFileWriter;
import data_import.pit.FaultSelectionStrategy1;
import data_import.pit.PitAnalysisPreparation;
import data_import.pit.PitDataObjectsConverter;
import data_import.pit.PitFaultSelectionStrategyBase;
import data_import.pit.data_objects.PitMutation;
import evaluation.ProjectEvaluationEntry;
import evaluation.StatisticsPrinter;
import hac.main.HierarchicalAgglomerativeClustering;

public class AnalysisWrapper {
	private static final String OUTPUT_DIR = "C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\";
	private final static String OUTPUT_FILE_NAME = "prioritization-evaluation.csv";
	
	private static final int MIN_FAILURES_TO_INVESTIGATE = 3;
	private static final int MAX_FAILURES_TO_INVESTIGATE = 5;
	
	private PitAnalysisPreparation prep;
	private PitFaultSelectionStrategyBase faultSelectionStrategy;
	private List<Set<PitMutation>> faultyVersions;
	private EvaluationFileWriter outputWriter;
	
	private String projectName;
	
	/**	must be set for every faulty version */
	public static int methodsCount = 0;
	public static int failuresCount = 0;
	public static int passedTestsCount = 0;
	public static int testsCount = 0;
	
	public AnalysisWrapper() {
		faultSelectionStrategy = new FaultSelectionStrategy1(9, 10, 2);
		outputWriter = new EvaluationFileWriter(OUTPUT_DIR, OUTPUT_FILE_NAME);
	}
/**
 * import()
 * analyze()
 * 		get faulty versions
 * 		build and analyze each
 */
	/**
	 * imports data and creates multiple faulty versions to analyze for a PIT project
	 */
	public void importProject(String dir, String projectName) throws IOException {
		prep = new PitAnalysisPreparation(dir);
		faultyVersions = faultSelectionStrategy.selectFaultyVersions(prep.getPitFaults(), prep.getPitTests());
		this.projectName = projectName;
	}
	/**
	 * Analyzes all faulty versions for a PIT project.
	 */
	public void analyze() throws IOException {
		int id = 1;
		for (Set<PitMutation> faultyVersion:faultyVersions) {
			PitDataObjectsConverter converter = prep.initTestsAndFaults(faultyVersion);
			ProjectEvaluationEntry projectMetrics = new ProjectEvaluationEntry(id, projectName,
					converter.getFaults().size(), converter.getFailures().length,
					converter.getPassedTCs().length);
			//TODO: PrioritizationStrategy Factory anlegen, welche alle strategien
			//		initialisiert und zurück gibt
			analyzeStrategy(new HierarchicalAgglomerativeClustering(converter.getFailures(),
					converter.getPassedTCs(), converter.getFaults()), projectMetrics);
			id++;
		}
	}
	
	private void analyzeStrategy(PrioritizationStrategyBase strategy, ProjectEvaluationEntry projectMetrics) throws IOException {
		strategy.prioritizeFailures();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= MAX_FAILURES_TO_INVESTIGATE; i++) {
			outputWriter.writeEvaluationEntry(strategy.evaluatePrioritizationStrategy(i, projectMetrics));
		}
	}
	/*private void analyze_HAC(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) throws IOException {
		PrioritizationStrategyBase hac = new HierarchicalAgglomerativeClustering(failures,
				passedTCs, faults);
		List<TestCase> prioritizedFailures = hac.prioritizeFailures();
		System.out.println("Prioritized Failures: " + prioritizedFailures);
	}
	private void analyze_Random() {
	}	*/

	
	/**
	 * Method to simply print the statistics for a project and then exit the program.
	 * Imports the data itself.
	 */
	public void printStatistics(String dir) throws IOException {
		prep = new PitAnalysisPreparation(dir);
		StatisticsPrinter statisticsPrinter = new StatisticsPrinter(prep.getPitMethods(), prep.getPitTests());
		statisticsPrinter.printTestStatistics();
	}
}
