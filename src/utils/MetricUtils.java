package utils;

import java.util.Set;
import java.util.stream.Collectors;

public class MetricUtils {
	public static double jaccardSetSimilarity(final Set<Integer> s1, final  Set<Integer> s2) {
		Set<Integer> intersection = s1.stream()
			    .filter(s2::contains)
			    .collect(Collectors.toSet());
		return((double)intersection.size()/(s1.size() + s2.size() - intersection.size()));
	}
}
