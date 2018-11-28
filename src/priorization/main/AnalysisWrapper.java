package priorization.main;

import java.io.IOException;
import java.util.List;

import data_export.evaluation.EvaluationFileWriter;
import data_import.faulty_versions.FaultyVersionsReader;
import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;
import prioritization.evaluation.ProjectEvaluationEntry;
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
	
	private List<FaultyVersion> faultyVersions;
	private EvaluationFileWriter outputWriter;
	
	public AnalysisWrapper() {
		outputWriter = new EvaluationFileWriter(OUTPUT_DIR, OUTPUT_FILE_NAME);
	}
	/**
	 * imports data multiple faulty versions to analyze for a PIT project
	 */
	public void importProject(String dir, String projectName) throws IOException {
		faultyVersions = FaultyVersionsReader.importFaultyVersions(dir);
	}
	/**
	 * Analyzes all imported faulty versions for a PIT project.
	 */
	public void analyze() throws IOException {
		for (FaultyVersion faultyVersion:faultyVersions) {
			initFaultyProjectGlobals(faultyVersion);
			System.out.println("Processing next faulty Version with " + faultyVersion.getFaults().size() + " faults, " + faultyVersion.getFailures().length + " failures, " + faultyVersion.getPassedTCs().length + " passing Test Cases and " + FaultyProjectGlobals.methodsCount + " relevant methods.");
			ProjectEvaluationEntry projectMetrics = faultyVersion.getProjectMetrics();
			List<PrioritizationStrategyBase> strategies = PrioritizationStrategyFactory.createStrategies(
					faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
			for (PrioritizationStrategyBase strategy: strategies) {
				System.out.println("Analyzing strategy " + strategy.strategyName);
				analyzeStrategy(strategy, projectMetrics);
			}
			//TODO: SameName/Class/Package clustering implementieren
			//		Train/Test split Konzept überlegen (vllt. paar Faults aus random speichern und vorerst immer die zum debuggen nehmen?)
		}
	}
	private void analyzeStrategy(PrioritizationStrategyBase strategy, ProjectEvaluationEntry projectMetrics) throws IOException {
		strategy.prioritizeFailures();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= MAX_FAILURES_TO_INVESTIGATE; i++) {
			outputWriter.writeEvaluationEntry(strategy.evaluatePrioritizationStrategy(i, projectMetrics));
		}
	}
	private void initFaultyProjectGlobals(FaultyVersion faultyVersion) {
		FaultyProjectGlobals.methodsCount = faultyVersion.getFailures()[0].coverage.length;
		FaultyProjectGlobals.failuresCount = faultyVersion.getFailures().length;
	}
	/**
	 * Method to simply print the statistics for a project and then exit the program.
	 * Imports the data itself.
	 */
	public void printStatistics(String dir) throws IOException {
//		prep = new PitAnalysisPreparation(dir);
//		StatisticsPrinter statisticsPrinter = new StatisticsPrinter(prep.getPitMethods(), prep.getPitTests());
//		statisticsPrinter.printTestStatistics();
	}
}
