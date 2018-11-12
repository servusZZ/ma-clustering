package data_import.gzoltars;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import data_objects.Fault;
import data_objects.TestCase;
import priorization.main.Main;

public class GzoltarDataReader {
	public static String TESTS_FILE_PATH = Main.BASE_DIR + Main.PROJECT_DIR + "tests";
	public static String MATRIX_FILE_PATH = Main.BASE_DIR + Main.PROJECT_DIR + "matrix";
	public static String MAPPING_FILE_PATH = Main.BASE_DIR + Main.PROJECT_DIR + "faults_failures.csv";
	
	
	public static Set<Fault> importFaults() throws IOException{
		Map<String, List<String>> faults = new HashMap<String, List<String>>();
		
		final CSVParser faultsParser = new CSVParserBuilder().withSeparator(',').build();
		final CSVReader faultsReader = new CSVReaderBuilder(new FileReader(MAPPING_FILE_PATH)).withCSVParser(faultsParser).withSkipLines(1).build();
		
		String[] nextFaultFailureEntry;
		Iterator<String[]> iter = faultsReader.iterator();
		while(iter.hasNext()) {
			nextFaultFailureEntry = iter.next();
			String faultName = nextFaultFailureEntry[0];
			String failureName = nextFaultFailureEntry[1];
			if (faults.containsKey(faultName)) {
				faults.get(faultName).add(failureName);
			} else {
				List<String> failureNames = new ArrayList<String>();
				failureNames.add(failureName);
				faults.put(faultName, failureNames);
			}
		}
		Set<Fault> importedFaults = new HashSet<Fault>();
		faults.forEach((k, v) -> importedFaults.add(new Fault(k, v)));
		return importedFaults;
	}
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
