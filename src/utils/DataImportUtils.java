package utils;

import java.util.Collection;
import java.util.List;

import data_objects.Fault;
import data_objects.TestCase;

public class DataImportUtils {
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
	public static void addFaultsToFailures(TestCase[] failures, Collection<Fault> faults) {
		for (int i = 0; i < failures.length; i++) {
			for (Fault f: faults) {
				if (f.failures.contains(failures[i].name)) {
					failures[i].setFault(f);
					break;
				}
			}
		}
	}
	
	public static void splitTestCases(final List<TestCase> testCases, List<TestCase> failedTCs, List<TestCase> passedTCs) {
		for(TestCase tc: testCases) {
			if(tc.passed) {
				passedTCs.add(tc);
			} else {
				failedTCs.add(tc);
			}
		}
	}
}
