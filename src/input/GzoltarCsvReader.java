package input;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import data_objects.TestCase;
import main.Main;

public class GzoltarCsvReader {
	public static String TESTS_FILE_PATH = Main.BASE_DIR + Main.PROJECT_DIR + "tests";
	public static String MATRIX_FILE_PATH = Main.BASE_DIR + Main.PROJECT_DIR + "matrix";
	
	public static List<TestCase> importTestCases() throws IOException{
		List<TestCase> testCases = new ArrayList<TestCase>();
		
		final CSVParser matrixParser = new CSVParserBuilder().withSeparator(' ').build();
		final CSVReader matrixReader = new CSVReaderBuilder(new FileReader(MATRIX_FILE_PATH)).withCSVParser(matrixParser).build();
		
		final CSVParser testsParser = new CSVParserBuilder().withSeparator(',').build();
		final CSVReader testsReader = new CSVReaderBuilder(new FileReader(TESTS_FILE_PATH)).withCSVParser(testsParser).withSkipLines(1).build();
		
		String[] nextTestEntry;
		List<Boolean[]> coverageMatrix = getCoverageMatrix(matrixReader.readAll());
		Iterator<Boolean[]> iter = coverageMatrix.iterator();
		while(iter.hasNext()) {
			nextTestEntry = testsReader.readNext();
			if (nextTestEntry[1].equals("FAIL")) {
				testCases.add(new TestCase(nextTestEntry[0], false, iter.next()));
			} else {
				testCases.add(new TestCase(nextTestEntry[0], true, iter.next()));
			}
		}
		return testCases;
	}
	private static List<Boolean[]> getCoverageMatrix(List<String[]> coverageEntries){
		List<Boolean[]> coverageMatrix = new ArrayList<Boolean[]>();
		boolean first = true;
		for (String[] coverageEntry : coverageEntries) {
			if (first) {
				Main.methodsCount = coverageEntry.length;
				first = false;
			}
			Boolean[] booleanEntry = new Boolean[Main.methodsCount];
			for (int i = 0; i < Main.methodsCount; i++) {
				if (coverageEntry[i].equals("1")) {
					booleanEntry[i] = true;
				} else {
					booleanEntry[i] = false;
				}
			}
			coverageMatrix.add(booleanEntry);
		}
		return coverageMatrix;
	}
}
