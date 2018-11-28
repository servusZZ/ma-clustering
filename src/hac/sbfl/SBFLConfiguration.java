package hac.sbfl;

import java.util.Map;
import java.util.Set;

import hac.data_objects.Cluster;

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
	/**	can only be exceeded by 178 or more failing tests */
	public static final double MAX_SUSP_VALUE = 1000000000;
	public SBFLConfiguration() { }
	public abstract Set<Integer> computeMostSuspiciousSet(Map<Integer, Double> methodDStarSusp);
	public abstract boolean clustersAreSimilar(Cluster c1, Cluster c2);
	public abstract boolean clustersAreSimilar(double similarityValue);
	public abstract double getSimilarityValue(Cluster c1, Cluster c2);
}
