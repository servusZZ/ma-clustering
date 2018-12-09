package utils;

import java.util.Set;
import java.util.stream.Collectors;

public class MetricUtils {
	/**
	 * Used to calculate the distance between two test cases (hac experiment objects).
	 * Used to calculate similarity of suspicious sets (cutting level of failure tree and refinement).
	 */
	public static double jaccardSetSimilarity(final Set<Integer> s1, final  Set<Integer> s2) {
		Set<Integer> intersection = s1.stream()
			    .filter(s2::contains)
			    .collect(Collectors.toSet());
		return((double)intersection.size()/(s1.size() + s2.size() - intersection.size()));
	}
	/**
	 * Used to calculate similarity of suspicious sets.
	 */
	public static double overlapSimilarity(final Set<Integer> s1, final Set<Integer> s2) {
		Set<Integer> intersection = s1.stream()
			    .filter(s2::contains)
			    .collect(Collectors.toSet());
		return ((double)intersection.size()/(Math.min(s1.size(), s2.size())));
	}
}
