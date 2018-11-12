package evaluation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVParser;
import com.opencsv.ICSVWriter;

import data_objects.Fault;
import data_objects.TestCase;
import priorization.main.Main;

public class PrioritizationEvaluation {
	private final String OUTPUT_FILE_NAME = "prioritization-evaluation.csv";
	private List<Fault> faultsOrdered;
	private int failuresToInvestigateCount;
	private TestCase[] allFailures;
	
	public PrioritizationEvaluation(Set<Fault> faults, TestCase[] allFailures, int failuresToInvestigateCount) {
		faultsOrdered = new ArrayList<Fault>();
		faultsOrdered.addAll(faults);
		Collections.sort(faultsOrdered);
		this.failuresToInvestigateCount = failuresToInvestigateCount;
		this.allFailures = allFailures;
	}
	
	public void evaluatePrioritizationStrategy(List<TestCase> failuresOrdered, String strategyName) throws IOException {
		String[] evaluationEtnry = new String[5];
		evaluationEtnry[0] = strategyName;
		evaluationEtnry[1] = "" + failuresToInvestigateCount;
		
		//TODO: failuresToInvestigateCount berücksichtigen!
		Set<Fault> foundFaults = new HashSet<Fault>();
		for (TestCase failure: failuresOrdered){
			foundFaults.add(failure.getFault());
		}
		evaluationEtnry[2] = "" + foundFaults.size();
		int fixedFailuresCount = 0;
		for (TestCase failure: allFailures) {
			if (foundFaults.contains(failure.getFault())){
				fixedFailuresCount++;
			}
		}
		evaluationEtnry[3] = "" + fixedFailuresCount;
		evaluationEtnry[4] = "FIND_EXACTLY_ONE_FAULT";
		writeEvaluationEntry(evaluationEtnry);
	}
	private Path createOutputFileHeaderIfNotExisting() throws IOException {
		Path outputFilePath = Paths.get(Main.OUTPUT_DIR, OUTPUT_FILE_NAME);
		if (!Files.exists(outputFilePath)) {
			List<String> header = new ArrayList<String>();
			header.add("PrioritizationStrategy;InvestigatedFailures;FoundFaults;FixedFailures;DebuggingStrategy");
			//TODO: Debugging strategy klären + Code entsprechend updaten
			Files.write(outputFilePath, header);
		}
		return outputFilePath;
	}
	private void writeEvaluationEntry(String[] evaluationEntry) throws IOException {
		Path outputFilePath = createOutputFileHeaderIfNotExisting();
		//Writer writer = new FileWriter(Main.OUTPUT_DIR + OUTPUT_FILE_NAME);
		//ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(';').build();
		//csvWriter.writeNext(evaluationEntry);
		List<String> lines = new ArrayList<String>();
		String line = evaluationEntry[0];
		for (int i = 1; i < evaluationEntry.length; i++) {
			line += ";" + evaluationEntry[i];
		}
		lines.add(line);
		Files.write(outputFilePath, lines, StandardOpenOption.APPEND);
	}
}
