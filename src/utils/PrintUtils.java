package utils;

import java.util.List;
import java.util.Map;

import data_objects.TestCase;
import hac.main.Cluster;

public class PrintUtils {
	public static String printTestCasesList(List<TestCase> testCases) {
		String result = "";
		boolean first = true;
		for (TestCase tc: testCases) {
			if (first) {
				result = tc.name;
				first = false;
				continue;
			}
			result += ", " + tc.name;
		}
		return result;
	}
	public static String printMapValuesOrdered(Map<Integer, Double> map) {
		String mapValues = "";
		if (map == null || map.isEmpty()) {
			return mapValues;
		}
		mapValues = map.get(0).toString();
		int i = 1;
		while (map.containsKey(i)) {
			mapValues += ", " + map.get(i).toString();
			i++;
		}
		return mapValues;
	}
	public static void dumpClusters(List<Cluster> clusters) {
		for (Cluster cl: clusters) {
			cl.dump();
		}
	}
}
