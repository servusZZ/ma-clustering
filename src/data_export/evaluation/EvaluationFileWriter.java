package data_export.evaluation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import evaluation.EvaluationEntry;

public class EvaluationFileWriter {
	private Path outputFilePath;
	
	public EvaluationFileWriter(String outputDir, String outputFileName) {
		outputFilePath = Paths.get(outputDir, outputFileName);
		System.out.println("Evaluation output file: " + outputFilePath.toString());
	}
	public void writeEvaluationEntry(EvaluationEntry entry) throws IOException {
		List<String> lines = new ArrayList<String>();
		StandardOpenOption createOrAppend = StandardOpenOption.APPEND;
		if (!Files.exists(outputFilePath)) {
			createOrAppend = StandardOpenOption.CREATE_NEW;
			lines.add(entry.getHeader());
		}
		lines.add(entry.getValues());
		Files.write(outputFilePath, lines, createOrAppend);
	}
}
