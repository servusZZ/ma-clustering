package priorization.main;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import data_export.evaluation.EvaluationFileWriter;
import data_import.pit.FaultSelectionStrategy1;
import data_import.pit.PitAnalysisPreparation;
import data_import.pit.PitDataObjectsConverter;
import data_import.pit.PitFaultSelectionStrategyBase;
import data_import.pit.SelectSpecificFaults1;
import data_import.pit.data_objects.PitMutation;
import evaluation.ProjectEvaluationEntry;
import evaluation.StatisticsPrinter;
import hac.main.HierarchicalAgglomerativeClustering;
/**
 * Wrapper to analyze a pit project. Usage:<br>
 * import()<br>
 * analyze()<br>
 * __ get faulty versions<br>
 * __ build and analyze each<br>
 */
public class AnalysisWrapper {
	private static final String OUTPUT_DIR = "C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\";
	private final static String OUTPUT_FILE_NAME = "prioritization-evaluation.csv";
	
	private static final int MIN_FAILURES_TO_INVESTIGATE = 1;
	private static final int MAX_FAILURES_TO_INVESTIGATE = 10;
	
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
		//TODO: change to different fault selection strategy again
		faultSelectionStrategy = new SelectSpecificFaults1();
		outputWriter = new EvaluationFileWriter(OUTPUT_DIR, OUTPUT_FILE_NAME);
	}
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
		int faultyProjectId = 1;
		for (Set<PitMutation> faultyVersion:faultyVersions) {
			PitDataObjectsConverter converter = prep.initTestsAndFaults(faultyVersion);
			System.out.println("Processing next faulty Version with " + converter.getFaults().size() + " faults, " + converter.getFailures().length + " failures, " + converter.getPassedTCs().length + " passing Test Cases and " + AnalysisWrapper.methodsCount + " relevant methods.");
			ProjectEvaluationEntry projectMetrics = new ProjectEvaluationEntry(faultyProjectId, projectName,
					converter.getFaults().size(), converter.getFailures().length,
					converter.getPassedTCs().length);
			List<PrioritizationStrategyBase> strategies = PrioritizationStrategyFactory.createStrategies(
					converter.getFailures(), converter.getPassedTCs(), converter.getFaults());
			for (PrioritizationStrategyBase strategy: strategies) {
				System.out.println("Analyzing strategy " + strategy.strategyName);
				analyzeStrategy(strategy, projectMetrics);
			}
			//TODO: SameName/Class/Package clustering implementieren
			//		Train/Test split Konzept überlegen (vllt. paar Faults aus random speichern und vorerst immer die zum debuggen nehmen?)
			faultyProjectId++;
		}
	}
	private void analyzeStrategy(PrioritizationStrategyBase strategy, ProjectEvaluationEntry projectMetrics) throws IOException {
		strategy.prioritizeFailures();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= MAX_FAILURES_TO_INVESTIGATE; i++) {
			outputWriter.writeEvaluationEntry(strategy.evaluatePrioritizationStrategy(i, projectMetrics));
		}
	}
	
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
