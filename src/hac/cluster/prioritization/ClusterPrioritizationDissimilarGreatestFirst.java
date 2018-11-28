package hac.cluster.prioritization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hac.data_objects.Cluster;
import hac.sbfl.SBFLConfiguration;
import utils.MathUtils;

/**
 * Prioritization according to dissimilarity and size.
 * The biggest cluster is at position 0, then
 * 	compute the most dissimilar clusters to all already picked clusters
 *		and pick the biggest cluster of these as next cluster.
 *
 * Similarity to a set of clusters is computed by adding up all the similarity values for each cluster.
 * 		If 2 clusters have a identical similarity, the one with the smaller maximum similarity value is picked
 * 			c1		c2		c3
 * -----|-----------------------
 * cA 	|	0.1		0.1		0.2
 * cB	|	0		0		0.4		--> cB has highest similarity because of high value 0.4
 * cD	|	0.2		0.05	0.15
 * 
 * 	cA and cD have equal similarity, --> the bigger cluster is picked (to fix more failures).
 */
public class ClusterPrioritizationDissimilarGreatestFirst extends ClusterPrioritizationBase{

	
	public ClusterPrioritizationDissimilarGreatestFirst(SBFLConfiguration sbflConfig) {
		super(sbflConfig);
	}
	@Override
	public List<Cluster> prioritizeClusters(List<Cluster> clusters) {
		List<Cluster> prioritizedClusters = new ArrayList<Cluster>();
		Cluster tmpCluster = Collections.max(clusters);
		clusters.remove(tmpCluster);
		prioritizedClusters.add(tmpCluster);
		while(!clusters.isEmpty()) {
			List<List<Double>> similarityValues = getSimilarityValues(prioritizedClusters, clusters);
			tmpCluster = selectNextCluster(similarityValues, clusters);
			clusters.remove(tmpCluster);
			prioritizedClusters.add(tmpCluster);
		}
		return prioritizedClusters;
	}
	/**
	 * Returns the index of the cluster that is prioritized next.
	 * The prio is calculated based on the summed up similarity to other clusters,
	 * the smallest maximum similarity value and the cluster size.
	 */
	private Cluster selectNextCluster(List<List<Double>> similarityValues, List<Cluster> clusters) {
		List<Integer> smallestSumIndexes = new ArrayList<Integer>();
		smallestSumIndexes.add(0);
		double smallestSum = MathUtils.sum(similarityValues.get(0));
		
		// get clusters (indexes) with the smallest similarity sums
		for (int i = 1; i < similarityValues.size(); i++) {
			double tmpSum = MathUtils.sum(similarityValues.get(i));
			if (smallestSum == tmpSum) {
				smallestSumIndexes.add(i);
			}
			else if (smallestSum > tmpSum) {
				smallestSum = tmpSum;
				smallestSumIndexes.clear();
				smallestSumIndexes.add(i);
			}
		}
		// out of these clusters, get the clusters with the smallest maximum similarity value
		List<Integer> smallestMaxValueIndexes = new ArrayList<Integer>();
		int tmpClusterIndex = smallestSumIndexes.get(0);
		smallestMaxValueIndexes.add(tmpClusterIndex);
		double smallestMaxValue = Collections.max(similarityValues.get(tmpClusterIndex));
		for (int i = 1; i < smallestSumIndexes.size(); i++) {
			tmpClusterIndex = smallestSumIndexes.get(i);
			double tmpMaxValue = Collections.max(similarityValues.get(tmpClusterIndex));
			if (smallestMaxValue == tmpMaxValue) {
				smallestMaxValueIndexes.add(tmpClusterIndex);
			}
			else if (smallestMaxValue > tmpMaxValue) {
				smallestMaxValue = tmpMaxValue;
				smallestMaxValueIndexes.clear();
				smallestMaxValueIndexes.add(tmpClusterIndex);
			}
		}
		// out of these clusters, get the biggest one
		List<Cluster> nextClusterCandidates = new ArrayList<Cluster>();
		for (int i: smallestMaxValueIndexes) {
			nextClusterCandidates.add(clusters.get(i));
		}
		Cluster nextCluster = Collections.max(nextClusterCandidates);
		return nextCluster;
	}
	private List<List<Double>> getSimilarityValues(List<Cluster> selectedClusters, List<Cluster> remainingClusters){
		/**	stores the similarity values for each remaining cluster to all selectedCluster */
		List<List<Double>> pairwiseSimilarity = new ArrayList<List<Double>>();
		/**	stores the summed up similarity for each remaining cluster */
		for (Cluster remainingCluster: remainingClusters) {
			pairwiseSimilarity.add(computePairwiseSimilarity(selectedClusters, remainingCluster));
		}
		return pairwiseSimilarity;
	}
	private List<Double> computePairwiseSimilarity(List<Cluster> selectedClusters, Cluster c) {
		List<Double> pairwiseSimilarity = new ArrayList<Double>();
		for (Cluster selectedCluster: selectedClusters) {
			pairwiseSimilarity.add(sbflConfig.getSimilarityValue(selectedCluster, c));
		}
		return pairwiseSimilarity;
	}
}
