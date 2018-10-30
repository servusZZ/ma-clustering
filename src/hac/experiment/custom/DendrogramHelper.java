package hac.experiment.custom;

import java.util.ArrayList;
import java.util.List;

import ch.usi.inf.sape.hac.dendrogram.DendrogramNode;
import ch.usi.inf.sape.hac.dendrogram.ObservationNode;

public class DendrogramHelper {
	
	public static List<Integer> getObservations(DendrogramNode n) {
		List<Integer> observations = new ArrayList<Integer>(n.getObservationCount());
		getObservations(n, observations);
		return observations;
	}
	private static void getObservations(DendrogramNode n, List<Integer> observations) {
		if (n == null) {
			return;
		}
		if(n instanceof ObservationNode) {
			observations.add(((ObservationNode)n).getObservation());
			return;
		}
		getObservations(n.getLeft(), observations);
		getObservations(n.getRight(), observations);
	}
	/**
	 * Builds a cluster for each node and computes the similarity of both.
	 * 		True,  iff similarity > Threshold
	 * 		False, iff similarity <= Threshold
	 */
	public static boolean clustersAreSimilar(DendrogramNode n1, DendrogramNode n2) {
		
		return false;
	}
}
