package hac.sbfl;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import faulty_project.globals.FaultyProjectGlobals;
import hac.data_objects.Cluster;
import hac.evaluation.ClusterComparingEvaluation;
import utils.SortingUtils;

/**
 * A specific configuration that holds all relevant parameters and implementations regarding
 * the use of D* as SBFL metric. This includes parameters:<br>
 * 	- MOST_SUSP_SET_MIN_SIZE
 *  - MOST_SUSP_SET_REL_SIZE
 *  - MOST_SUSP_SET_MAX_SIZE<br>
 *  - SIMILARITY_THRESHOLD<br>
 *
 * and Methods:<br>
 *  - compute if 2 clusters are similar<br>
 *  - computeMostSuspSet (for each cluster)
 *  - compute Similarity Value (for 2 clusters)
 *
 */
public abstract class SBFLConfiguration {
	public double MOST_SUSP_THRESHOLD;
	public int MOST_SUSP_MAX_COUNT;
	public int MOST_SUSP_MIN_COUNT;
	public double SIMILARITY_THRESHOLD;
	/**	can only be exceeded by 178 or more failing tests */
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	protected ClusterComparingEvaluation clusterComparisonEvaluation;
	
	public SBFLConfiguration() { 
		clusterComparisonEvaluation = new ClusterComparingEvaluation();
	}
	public abstract boolean clustersAreSimilar(Cluster c1, Cluster c2);
	public abstract double getSimilarityValue(Cluster c1, Cluster c2);
	
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
		// additionally fill all elements that have the same value than the lowest suspiciousness
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
	
	public boolean clustersAreSimilar(double similarityValue) {
		if (similarityValue > SIMILARITY_THRESHOLD) {
			return true;
		}
		return false;
	}
	protected int getMostSuspiciousSetSize() {
		int mostSuspSetSize = (int) (FaultyProjectGlobals.methodsCount * MOST_SUSP_THRESHOLD);
		if(mostSuspSetSize < MOST_SUSP_MIN_COUNT) {
			return MOST_SUSP_MIN_COUNT;
		}
		else if(mostSuspSetSize > MOST_SUSP_MAX_COUNT) {
			return MOST_SUSP_MAX_COUNT;
		}
		return mostSuspSetSize;
	}
	public ClusterComparingEvaluation getClusterComparisonEvaluation() {
		return clusterComparisonEvaluation;
	}
}
