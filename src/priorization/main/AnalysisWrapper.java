package priorization.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data_export.evaluation.EvaluationFileWriter;
import data_import.faulty_versions.FaultyVersionsReader;
import evaluation.OptimalEvaluationEntry;
import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;
import prioritization.data_objects.TrainSet;
import prioritization.evaluation.ProjectEvaluationEntry;
import prioritization.strategies.OptimalPrioritization;
/**
 * Wrapper to analyze a pit project. Usage:<br>
 * processProject()<br>
 * __ import faulty versions per file<br>
 * __ and analyze each<br>
 */
public class AnalysisWrapper {
	private static final String OUTPUT_DIR = "C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\";
	private final static String OUTPUT_FILE_NAME = "prioritization-evaluation.csv";
	
	private static final int MIN_FAILURES_TO_INVESTIGATE = 3;
	private static final int MAX_FAILURES_TO_INVESTIGATE = 20;
	private static final int[] EXTRA_FAILURES_TO_INVESTIGATE = {30, 50, 80, 130};
	
	private EvaluationFileWriter outputWriter;
	private TrainSet trainSet;
	
	public AnalysisWrapper() {
		outputWriter = new EvaluationFileWriter(OUTPUT_DIR, OUTPUT_FILE_NAME);
		trainSet = new TrainSet();
	}

	/**
	 * Performs a step-wise analysis of a project. I.e. it imports each faulty-versions file and 
	 * analyzes it.
	 */
	public void processProject(String dir, String projectName) throws IOException {
		while(FaultyVersionsReader.hasNextFaultyVersionsFile(dir)) {
			List<FaultyVersion> faultyVersions = FaultyVersionsReader.importNextFaultyVersionsFile(dir);
			analyze(faultyVersions);
		}
		FaultyVersionsReader.resetFilesCounter();
	}
	/**
	 * Analyzes the passed imported faulty versions of a PIT project.
	 */
	private void analyze(List<FaultyVersion> faultyVersions) throws IOException {
		for (FaultyVersion faultyVersion:faultyVersions) {
			FaultyProjectGlobals.init(faultyVersion);
			System.out.println("Processing next faulty Version " + faultyVersion.getProjectMetrics().getId() + " with " + faultyVersion.getFaults().size() + " faults, " + FaultyProjectGlobals.failuresCount + " failures, " + faultyVersion.getPassedTCs().length + " passing Test Cases and " + FaultyProjectGlobals.methodsCount + " relevant methods.");
			Map<Integer, OptimalEvaluationEntry> optimalMetrics = analyzeOptimalStrategy(faultyVersion);
			List<PrioritizationStrategyBase> strategies = PrioritizationStrategyFactory.createStrategies(
					faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
			System.out.println("Analyzing the version with " + strategies.size() + " strategies.");
			for (PrioritizationStrategyBase strategy: strategies) {
				analyzeStrategy(strategy, faultyVersion.getProjectMetrics(), optimalMetrics);
			}
		}
	}
	private void analyzeStrategy(PrioritizationStrategyBase strategy, ProjectEvaluationEntry projectMetrics, Map<Integer, OptimalEvaluationEntry> optimalMetrics) throws IOException {
		strategy.prioritizeFailures();
		for (int failuresToInvestigate: getFailuresToInvestigateCounts()) {
			outputWriter.writeEvaluationEntry(strategy.evaluatePrioritizationStrategy(failuresToInvestigate, projectMetrics, optimalMetrics.get(failuresToInvestigate)));
		}
	}
	private Map<Integer, OptimalEvaluationEntry> analyzeOptimalStrategy(FaultyVersion faultyVersion) throws IOException {
		OptimalPrioritization optimalStrategy = new OptimalPrioritization(faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
		optimalStrategy.prioritizeFailures();
		Map<Integer, OptimalEvaluationEntry> optimalMetrics = new HashMap<Integer, OptimalEvaluationEntry>();
		for (int failuresToInvestigate: getFailuresToInvestigateCounts()) {
			OptimalEvaluationEntry optimalMetric = (OptimalEvaluationEntry)optimalStrategy.evaluatePrioritizationStrategy(failuresToInvestigate, faultyVersion.getProjectMetrics(), null);
			optimalMetrics.put(failuresToInvestigate, optimalMetric);
			outputWriter.writeEvaluationEntry(optimalMetric);
		}
		return optimalMetrics;
	}
	private List<Integer> getFailuresToInvestigateCounts(){
		List<Integer> failuresToInvestigateCounts = new ArrayList<Integer>();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= getMaxFailuresToInvestigate(); i++) {
			failuresToInvestigateCounts.add(i);
		}
		for (int i = 0; i < EXTRA_FAILURES_TO_INVESTIGATE.length; i++) {
			if (EXTRA_FAILURES_TO_INVESTIGATE[i] > FaultyProjectGlobals.failuresCount) {
				break;
			}
			failuresToInvestigateCounts.add(EXTRA_FAILURES_TO_INVESTIGATE[i]);
		}
		return failuresToInvestigateCounts;
	}
	private int getMaxFailuresToInvestigate() {
		if (MAX_FAILURES_TO_INVESTIGATE > FaultyProjectGlobals.failuresCount) {
			return FaultyProjectGlobals.failuresCount;
		}
		return MAX_FAILURES_TO_INVESTIGATE;
	}
	/**
	 * Prints the project metrics data for each faulty version into the 
	 * prioritization-evaluation.csv output file.
	 */
	public void printProject(String dir, String projectName) throws IOException {
		while(FaultyVersionsReader.hasNextFaultyVersionsFile(dir)) {
			List<FaultyVersion> faultyVersions = FaultyVersionsReader.importNextFaultyVersionsFile(dir);
			printFaultyVersions(faultyVersions);
		}
		FaultyVersionsReader.resetFilesCounter();
	}
	/**
	 * Prints the passed faultyVersions by analyzing them with the optimal strategy, the 
	 * parameter failuresToInvestigatePlanned is fixed to the value 10.
	 */
	private void printFaultyVersions(List<FaultyVersion> faultyVersions) throws IOException {
		for (FaultyVersion faultyVersion:faultyVersions) {
			FaultyProjectGlobals.init(faultyVersion);
			System.out.println("Printing next faulty Version " + faultyVersion.getProjectMetrics().getId() + " with " + faultyVersion.getFaults().size() + " faults, " + FaultyProjectGlobals.failuresCount + " failures, " + faultyVersion.getPassedTCs().length + " passing Test Cases and " + FaultyProjectGlobals.methodsCount + " relevant methods.");
			OptimalPrioritization optimalStrategy = new OptimalPrioritization(faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
			optimalStrategy.prioritizeFailures();
			OptimalEvaluationEntry optimalMetric = (OptimalEvaluationEntry)optimalStrategy.evaluatePrioritizationStrategy(10, faultyVersion.getProjectMetrics(), null);
			outputWriter.writeEvaluationEntry(optimalMetric);
		}
	}
	
	public void analyzeTrainSet(String dir, String projectName) throws IOException {
		if (!trainSet.containsProject(projectName)) {
			return;
		}
		while(FaultyVersionsReader.hasNextFaultyVersionsFile(dir)) {
			List<FaultyVersion> trainSetVersionsPerFile = FaultyVersionsReader.importNextFaultyVersionsFile_TrainSet(dir, trainSet);
			if (trainSetVersionsPerFile.isEmpty()) {
				continue;
			}
			analyzeStrategiesForTrainSet(trainSetVersionsPerFile);
		}
		FaultyVersionsReader.resetFilesCounter();
	}
	/**
	 * Analyzes the passed imported faulty versions of a PIT project with the HAC Configs, so that
	 * the parameters are trained.
	 */
	private void analyzeStrategiesForTrainSet(List<FaultyVersion> faultyVersions) throws IOException {
		for (FaultyVersion faultyVersion:faultyVersions) {
			FaultyProjectGlobals.init(faultyVersion);
			System.out.println("Processing next faulty Version " + faultyVersion.getProjectMetrics().getId() + " with " + faultyVersion.getFaults().size() + " faults, " + FaultyProjectGlobals.failuresCount + " failures, " + faultyVersion.getPassedTCs().length + " passing Test Cases and " + FaultyProjectGlobals.methodsCount + " relevant methods.");
			Map<Integer, OptimalEvaluationEntry> optimalMetrics = analyzeOptimalStrategy(faultyVersion);
			List<PrioritizationStrategyBase> strategies = PrioritizationStrategyFactory.createTrainSetStrategies(
					faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
			System.out.println("Analyzing the version with " + strategies.size() + " strategies.");
			for (PrioritizationStrategyBase strategy: strategies) {
				analyzeStrategy(strategy, faultyVersion.getProjectMetrics(), optimalMetrics);
			}
		}
	}
}
