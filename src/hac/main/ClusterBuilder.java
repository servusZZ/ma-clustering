package hac.main;

import java.util.ArrayList;
import java.util.List;

import ch.usi.inf.sape.hac.dendrogram.DendrogramNode;
import ch.usi.inf.sape.hac.dendrogram.MergeNode;
import ch.usi.inf.sape.hac.dendrogram.ObservationNode;
import data_objects.TestCase;
import hac.data_objects.PassedTCsCluster;
import hac.experiment.custom.DendrogramHelper;
import hac.sbfl.SBFLConfiguration;
/**
 * Builds the Clusters based on a Dendrogram (root node). Uses fault localization ranks, i.e. the 
 * suspicious set to determine the cutting point of the failure tree.
 * Merging of clusters stops as soon as two child clusters are not similar anymore
 *
 */
public class ClusterBuilder {
	private DendrogramNode root;
	private PassedTCsCluster passedTCsCluster;
	private SBFLConfiguration sbflConfig;

	private TestCase[] failures;
	
	public ClusterBuilder(DendrogramNode root, TestCase[] passedTCs, TestCase[] failures,
			SBFLConfiguration sbflConfig) {
		this.root = root;
		this.failures = failures;
		this.passedTCsCluster = new PassedTCsCluster(passedTCs);
		this.sbflConfig = sbflConfig;
	}

	public List<Cluster> getClustersOfCuttingLevel(){
		List<Cluster> resultClusters = new ArrayList<Cluster>();
		List<DendrogramNode> queue = new ArrayList<DendrogramNode>();
		DendrogramNode tmpNode = root;
		
		while(!clustersAreSimilar(tmpNode.getLeft(), tmpNode.getRight())) {
			insertNodeIntoQueue(queue, tmpNode.getLeft());
			insertNodeIntoQueue(queue, tmpNode.getRight());
			tmpNode = queue.remove(0);
			
			if (tmpNode instanceof ObservationNode) {
				// all clusters are splitted into atomic test cases (failures)
				break;
			}
		}
		
		// if node is ObservationNode --> build all TC Cluster
		// if node is MergeNode and child1, child2 are similar --> build cluster for tmpNode but not for childs
		queue.add(tmpNode);
		resultClusters = getClustersByDendrogramNodes(queue);
		return resultClusters;
	}
	private List<Cluster> getClustersByDendrogramNodes(List<DendrogramNode> nodes){
		List<Cluster> clusters = new ArrayList<Cluster>();
		for (DendrogramNode node: nodes) {
			clusters.add(getClusterByDendrogramNode(node));
		}
		return clusters;
	}
	/**
	 * Builds a cluster for each node and computes the similarity of both.
	 * 		True,  iff similarity > Threshold
	 * 		False, iff similarity <= Threshold
	 */
	private boolean clustersAreSimilar(DendrogramNode n1, DendrogramNode n2) {
		Cluster clusterN1 = getClusterByDendrogramNode(n1);
		Cluster clusterN2 = getClusterByDendrogramNode(n2);
		return sbflConfig.clustersAreSimilar(clusterN1, clusterN2);
	}
	private Cluster getClusterByDendrogramNode(DendrogramNode n) {
		TestCase[] failingTCsN = getFailingTestCasesByIndex(DendrogramHelper.getObservations(n));
		return new Cluster(failingTCsN, passedTCsCluster.getMethodDStarTerms(), sbflConfig);
	}
	private TestCase[] getFailingTestCasesByIndex(List<Integer> tcIndexes){
		List<TestCase> failingTCs = new ArrayList<TestCase>();
		for (int tcIndex: tcIndexes) {
			failingTCs.add(failures[tcIndex]);
		}
		TestCase[] failingTCsArray = new TestCase[failingTCs.size()];
		failingTCs.toArray(failingTCsArray);
		return failingTCsArray;
	}
	/**
	 * Inserts the node at the right position in the queue. The start of the queue contains MergeNodes sorted by Dissimilarity.
	 * The end of the queue consists of ObservationNodes (no defined order).
	 * @param queue
	 * @param n
	 */
	private void insertNodeIntoQueue(List<DendrogramNode> queue, DendrogramNode n) {
		if(queue.isEmpty() || (n instanceof ObservationNode)) {
			queue.add(n);
			return;
		}
		int position = 0;
		MergeNode mergeNode = (MergeNode)n;
		while(position < queue.size()) {
			DendrogramNode tmpNode = queue.get(position);
			if (tmpNode instanceof ObservationNode) {
				queue.add(position, mergeNode);
				return;
			}
			if (mergeNode.getDissimilarity() > ((MergeNode)tmpNode).getDissimilarity()) {
				queue.add(position, mergeNode);
				return;
			}
			position++;
		}
		// mergeNode has the greatest dissimilarity value and is inserted as last element
		queue.add(mergeNode);
	}
	public PassedTCsCluster getPassedTCsCluster() {
		return passedTCsCluster;
	}
}
