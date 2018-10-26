package utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SortingUtils {
	/**
	 * Sorts a Map by values in descending order.
	 * @param unsortedMap	the map to be sorted
	 * @return a new LinkedHashMap containing all entries of the passed map sorted by Values
	 */
	public static Map<Integer, Double> getSortedMapByValuesDescending(final Map<Integer, Double> unsortedMap){
		final Map<Integer, Double> sortedByValues = unsortedMap.entrySet()
				.stream().
				sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return sortedByValues;
	}
}
