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

public class GzoltarCsvReader {
	public static String TESTS_FILE_PATH = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\gzoltars\\Tests\\testMedium\\tests";
	public static String MATRIX_FILE_PATH = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\gzoltars\\Tests\\testMedium\\matrix";
	
	public static List<TestCase> importTestCases() throws IOException{
		List<TestCase> testCases = new ArrayList<TestCase>();
		
		final CSVParser matrixParser = new CSVParserBuilder().withSeparator(' ').build();
		final CSVReader matrixReader = new CSVReaderBuilder(new FileReader(MATRIX_FILE_PATH)).withCSVParser(matrixParser).build();
		
		final CSVParser testsParser = new CSVParserBuilder().withSeparator(',').build();
		final CSVReader testsReader = new CSVReaderBuilder(new FileReader(TESTS_FILE_PATH)).withCSVParser(testsParser).withSkipLines(1).build();
		
		String[] nextTestEntry;
		List<Boolean[]> coverageMatrix = getCoverageMatrix(matrixReader.readAll());
		Iterator iter = coverageMatrix.iterator();
		while(iter.hasNext()) {
			nextTestEntry = testsReader.readNext();
			String testCaseName = nextTestEntry[0];
			String testCaseResult = nextTestEntry[1];
			if (nextTestEntry[1].equals("FAIL")) {
				testCases.add(new TestCase(nextTestEntry[0], false, (Boolean[])iter.next()));
			} else {
				testCases.add(new TestCase(nextTestEntry[0], true, (Boolean[])iter.next()));
			}
		}
		return testCases;
	}
	private static List<Boolean[]> getCoverageMatrix(List<String[]> coverageEntries){
		List<Boolean[]> coverageMatrix = new ArrayList<Boolean[]>();
		for (String[] coverageEntry : coverageEntries) {
			Boolean[] booleanEntry = new Boolean[coverageEntry.length];
			for (int i = 0; i < coverageEntry.length; i++) {
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
