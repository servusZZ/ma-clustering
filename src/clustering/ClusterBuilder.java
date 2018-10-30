package clustering;

import java.util.ArrayList;
import java.util.List;

import ch.usi.inf.sape.hac.dendrogram.DendrogramNode;
import ch.usi.inf.sape.hac.dendrogram.MergeNode;
import ch.usi.inf.sape.hac.dendrogram.ObservationNode;
import data_objects.PassedTCsCluster;
import data_objects.TestCase;
import hac.experiment.custom.DendrogramHelper;

public class ClusterBuilder {
	private DendrogramNode root;
	private PassedTCsCluster passedTCsCluster;
	
	public ClusterBuilder(DendrogramNode root, List<TestCase> passedTCs) {
		this.root = root;
		this.passedTCsCluster = new PassedTCsCluster(passedTCs);
	}
	public List<Cluster> getClustersOfCuttingLevel(){
		List<Cluster> resultClusters = new ArrayList<Cluster>();
		List<DendrogramNode> queue = new ArrayList<DendrogramNode>();
		DendrogramNode tmpNode = root;
		
		boolean childsNotSimilar = DendrogramHelper.clustersAreSimilar(tmpNode.getLeft(), tmpNode.getRight());
		while(childsNotSimilar) {
			insertNodeIntoQueue(queue, tmpNode.getLeft());
			insertNodeIntoQueue(queue, tmpNode.getRight());
			//TODO GO ON HERE
			// clustersAreSimilar implementieren + algorithmus überdenken ob das so passt
		}
		
		return resultClusters;
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
}
