package hac.sbfl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import hac.data_objects.Cluster;

import java.util.Set;

import faulty_project.globals.FaultyProjectGlobals;
import utils.MetricUtils;
import utils.SortingUtils;
/**
 * see doc of superclass
 */
public class OverlapConfiguration1 extends SBFLConfiguration{
	public final double MOST_SUSP_THRESHOLD = 0.15;
	public final int MOST_SUSP_MAX_COUNT = 12;
	public final int MOST_SUSP_MIN_COUNT = 4;
	/**	a greater value means that 2 clusters are similar.<br>
	 * 3 of 5 overlap elements are not considered as similar, 6 of 9 are similar */
	public final double SIMILARITY_THRESHOLD = 0.60;
	@Override
	public Set<Integer> computeMostSuspiciousSet(Map<Integer, Double> methodDStarSusp) {
		Map<Integer, Double> sortedMethodDStarSusp = SortingUtils.getSortedMapByValuesDescending(methodDStarSusp);
		Set<Integer> mostSuspSet = new LinkedHashSet<>();
		int mostSuspSetSize = getMostSuspiciousSetSize();
		int i = 0;
		double lowestSuspValue = 0.0;
		Entry<Integer, Double> highestNotSuspEntry = null;
		Iterator<Entry<Integer, Double>> iter = sortedMethodDStarSusp.entrySet().iterator();
		// fill only the most suspicious values into the set (the first mostSuspSetSize elements)
		while (iter.hasNext()) {
			Entry<Integer, Double> entry = iter.next();
			if (i >= mostSuspSetSize) {
				highestNotSuspEntry = entry;
				break;
			}
			lowestSuspValue = entry.getValue();
			mostSuspSet.add(entry.getKey());
			i++;
		}
		// additionally fill all elements that have the same value than the lowest supiciousness
		// also into the set to avoid a random selection.
		while (highestNotSuspEntry.getValue() == lowestSuspValue) {
			mostSuspSet.add(highestNotSuspEntry.getKey());
			lowestSuspValue = highestNotSuspEntry.getValue();
			if (!iter.hasNext()) {
				break;
			}
			highestNotSuspEntry = iter.next();
		}
		return mostSuspSet;
	}
	private int getMostSuspiciousSetSize() {
		int mostSuspSetSize = (int) (FaultyProjectGlobals.methodsCount * MOST_SUSP_THRESHOLD);
		if(mostSuspSetSize < MOST_SUSP_MIN_COUNT) {
			return MOST_SUSP_MIN_COUNT;
		}
		else if(mostSuspSetSize > MOST_SUSP_MAX_COUNT) {
			return MOST_SUSP_MAX_COUNT;
		}
		return mostSuspSetSize;
	}
	/**
	 * Compares two clusters.
	 * 		True,  iff similarity > Threshold
	 * 		False, iff similarity <= Threshold
	 */
	@Override
	public boolean clustersAreSimilar(Cluster c1, Cluster c2) {
//		c1.dump();
//		c2.dump();
		double similarity = getSimilarityValue(c1, c2);
//		System.out.println("The clusters have a similarity value of " + similarity);
		if(similarity > SIMILARITY_THRESHOLD) {
			return true;
		}
		return false;
	}
	@Override
	public boolean clustersAreSimilar(double similarityValue) {
		if (similarityValue > SIMILARITY_THRESHOLD) {
			return true;
		}
		return false;
	}
	@Override
	public double getSimilarityValue(Cluster c1, Cluster c2) {
		return MetricUtils.overlapSimilarity(c1.getSuspiciousSet(), c2.getSuspiciousSet());
	}
}
