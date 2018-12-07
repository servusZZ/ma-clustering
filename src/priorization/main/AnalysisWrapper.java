package priorization.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data_export.evaluation.EvaluationFileWriter;
import data_import.faulty_versions.FaultyVersionsReader;
import evaluation.EvaluationEntry;
import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;
import prioritization.evaluation.ProjectEvaluationEntry;
import prioritization.strategies.OptimalPrioritization;
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
	
	//TODO: mit Rainer besprechen: Weniger als 3 failures inspizieren passt nicht in den Kontext?
	//		wie die obere Grenze setzen: Alle ansehen macht bei 5 Failures z.B. noch Sinn,
	//			bei 15 failures aber z.B. keinen Sinn weil man ja davon ausgeht, dass weniger Faults im System sind.
	private static final int MIN_FAILURES_TO_INVESTIGATE = 3;
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
			FaultyProjectGlobals.init(faultyVersion);
			System.out.println("Processing next faulty Version with " + faultyVersion.getFaults().size() + " faults, " + faultyVersion.getFailures().length + " failures, " + faultyVersion.getPassedTCs().length + " passing Test Cases and " + FaultyProjectGlobals.methodsCount + " relevant methods.");
			ProjectEvaluationEntry projectMetrics = faultyVersion.getProjectMetrics();
			// evaluation entry um Performance Metriken erweitern und diese anhand der optimal berechnen
			Map<Integer, EvaluationEntry> optimalMetrics = analyzeOptimalStrategy(faultyVersion);
			List<PrioritizationStrategyBase> strategies = PrioritizationStrategyFactory.createStrategies(
					faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
			for (PrioritizationStrategyBase strategy: strategies) {
				System.out.println("Analyzing strategy " + strategy.strategyName);
				analyzeStrategy(strategy, projectMetrics, optimalMetrics);
			}
			//TODO: A_NEXT, debug performance berechnung
			//TODO: HAC mit unterschiedlichen Konfigurationen hinzufügen & evaluieren
			//TODO: Versionen filtern, welche keinen Sinn ergeben:
			//			nur ein investigatedFailure?
			//			mehr investigatedPlanned als es Failures gibt
		}
	}
	private void analyzeStrategy(PrioritizationStrategyBase strategy, ProjectEvaluationEntry projectMetrics, Map<Integer, EvaluationEntry> optimalMetrics) throws IOException {
		strategy.prioritizeFailures();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= MAX_FAILURES_TO_INVESTIGATE; i++) {
			outputWriter.writeEvaluationEntry(strategy.evaluatePrioritizationStrategy(i, projectMetrics, optimalMetrics.get(i)));
		}
	}
	private Map<Integer, EvaluationEntry> analyzeOptimalStrategy(FaultyVersion faultyVersion) {
		OptimalPrioritization optimalStrategy = new OptimalPrioritization(faultyVersion.getFailures(), faultyVersion.getPassedTCs(), faultyVersion.getFaults());
		optimalStrategy.prioritizeFailures();
		Map<Integer, EvaluationEntry> optimalMetrics = new HashMap<Integer, EvaluationEntry>();
		for (int i = MIN_FAILURES_TO_INVESTIGATE; i <= MAX_FAILURES_TO_INVESTIGATE; i++) {
			optimalMetrics.put(i, optimalStrategy.evaluatePrioritizationStrategy(i, faultyVersion.getProjectMetrics(), null));
		}
		return optimalMetrics;			
	}
}
