package hac.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hac.data_objects.Cluster;
import hac.data_objects.PassedTCsCluster;
import hac.sbfl.SBFLConfiguration;
import prioritization.data_objects.TestCase;

public class Refinement {
	private int nextClusterID = 0;
	Map<Cluster, Integer> clusterRowIndexMapping = new HashMap<Cluster, Integer>();
	private PassedTCsCluster passedTCsCluster;
	private SBFLConfiguration sbflConfig;
	
	public Refinement(PassedTCsCluster passedTCsCluster, SBFLConfiguration sbflConfig) {
		this.passedTCsCluster = passedTCsCluster;
		this.sbflConfig = sbflConfig;
	}
	/**
	 * Does a pairwise comparison of the passed clusters (clusters of the cutting level of the Failure Tree)
	 * and merges similar clusters.
	 */
	public List<Cluster> refineClusters(List<Cluster> clusters){
		List<Cluster> refinedClusters = new ArrayList<Cluster>();
		double[][] similarity = getPairwiseSimilarity(clusters);
		while (true) {
			int[] maxValueIndexes = getMaxValueOfMatrix(similarity);
			int i = maxValueIndexes[0];
			int j = maxValueIndexes[1];
			double maxSimilarityValue = similarity[i][j];
			Cluster c1 = clusters.get(i);
			Cluster c2 = clusters.get(j);
			if (sbflConfig.clustersAreSimilar(maxSimilarityValue)) {
				TestCase[] mergedFailedTCs = new TestCase[c1.getFailedTCs().length + c2.getFailedTCs().length];
				// no check for duplicate TestCases is necessary because the 
				// failures of all clusters are disjoint.
				System.arraycopy(c1.getFailedTCs(), 0, mergedFailedTCs, 0, c1.getFailedTCs().length);
				System.arraycopy(c2.getFailedTCs(), 0, mergedFailedTCs, c1.getFailedTCs().length, c2.getFailedTCs().length);
				clusterRowIndexMapping.put(c1, null);
				clusterRowIndexMapping.put(c2, null);
				deleteClustersFromMatrix(i, j, similarity);
				Cluster mergedC12 = new Cluster(mergedFailedTCs, passedTCsCluster.getMethodDStarTerms(), sbflConfig);
				clusterRowIndexMapping.put(mergedC12, nextClusterID);
				nextClusterID++;
				updateSimilarityMatrix(similarity, mergedC12);
				clusters.add(mergedC12);
			} else {
				// no merge needed anymore, stop iteration
				break;
			}
		}
		// get all clusters which were not merged and the new clusters
		for (Cluster c: clusterRowIndexMapping.keySet()) {
			if(clusterRowIndexMapping.get(c) != null) {
				refinedClusters.add(c);
			}
		}
		return refinedClusters;
	}
	private void deleteClustersFromMatrix(int i, int j, double[][] matrix) {
		// delete rows
		Arrays.fill(matrix[i], -2.0);
		Arrays.fill(matrix[j], -2.0);
		// delete columns
		for (Integer index: clusterRowIndexMapping.values()) {
			if (index == null) {
				continue;
			}
			matrix[index][i] = -2.0;
			matrix[index][j] = -2.0;
		}
	}
	/**
	 * updates all values of the similarity matrix for the new cluster
	 * 	sim[newCluster][] = sim[][newCluster] = similarityValue
	 * @param similarity
	 * @param c1
	 */
	private void updateSimilarityMatrix(double[][] similarity, Cluster c1) {
		int i = clusterRowIndexMapping.get(c1);
		for (Cluster c2: clusterRowIndexMapping.keySet()) {
			if (c1 == c2 || clusterRowIndexMapping.get(c2) == null) {
				continue;
			}
			double similarityValue = sbflConfig.getSimilarityValue(c1, c2);
			int j = clusterRowIndexMapping.get(c2);
			similarity[i][j] = similarityValue;
			similarity[j][i] = similarityValue;
		}
	}
	private double[][] getPairwiseSimilarity(List<Cluster> clusters){
		double[][] similarity = new double[clusters.size()*2-1][clusters.size()*2-1];
		for (int i = 0; i < (clusters.size()-1); i++) {
			clusterRowIndexMapping.put(clusters.get(i), nextClusterID);
			nextClusterID++;
			for (int j = i+1; j < clusters.size(); j++) {
				double similarityValue = sbflConfig.getSimilarityValue(clusters.get(i), clusters.get(j));
				similarity[i][j] = similarityValue;
				similarity[j][i] = similarityValue;
			}
		}
		if (nextClusterID != clusters.size()-1) {
			System.err.println("ERROR: Cluster index mapping is broken!");
		}
		clusterRowIndexMapping.put(clusters.get(clusters.size()-1), nextClusterID);
		nextClusterID++;
		return similarity;
	}
	/**
	 * returns the indexes of the max value in the matrix as array.
	 * 	a[0] is the row index
	 * 	a[1] is the column index
	 */
	private int[] getMaxValueOfMatrix(double[][] matrix) {
		double maxValue = -1.0;
		int[] maxValueIndexes = new int[2];
		for (int i = 0; i < matrix.length-1; i++) {
			for (int j = i; j < matrix.length; j++) {
				if(matrix[i][j] > maxValue) {
					maxValue = matrix[i][j];
					maxValueIndexes[0] = i;
					maxValueIndexes[1] = j;
				}
			}
		}
		return maxValueIndexes;
	}
}
