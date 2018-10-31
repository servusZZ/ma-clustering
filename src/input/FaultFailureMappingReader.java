package input;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import data_objects.Fault;
import data_objects.TestCase;
import main.Main;

public class FaultFailureMappingReader {
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
	
	public static void addFaultsToFailures(List<TestCase> failures, Collection<Fault> faults) {
		for (TestCase tc: failures) {
			for (Fault f: faults) {
				if (f.failures.contains(tc.name)) {
					tc.setFault(f);
					break;
				}
			}
		}
	}

}
