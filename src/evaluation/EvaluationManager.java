package evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import clustering.Cluster;
import data_objects.Fault;
import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;

public class EvaluationManager {
	private CustomDissimilarityMeasure dissimilarityMeasure;
	private RepresentativeSelectionStrategy representativeSelection;
	private List<Cluster> clusters;
	private Set<Fault> faults;
	
	public EvaluationManager(CustomDissimilarityMeasure dissimilarityMeasure, RepresentativeSelectionStrategy representativeSelection, List<Cluster> clusters, Set<Fault> faults) {
		this.dissimilarityMeasure = dissimilarityMeasure;
		this.representativeSelection = representativeSelection;
		this.clusters = clusters;
		this.faults = faults;
	}
	/**
	 * calls all metric computations/ evaluation methods and print results
	 */
	public void evaluateClustering() {
		evaluateFaultDetection();
	}
	/**
	 * Checks whether all Faults would be detected by just fixing the representative Failures.
	 */
	private void evaluateFaultDetection() {
		System.out.println("Check whether all Faults are revealed...");
		Set<Fault> revealedFaults = new HashSet<Fault>();
		for (Cluster c: clusters) {
			System.out.println("Cluster  " + c);
			TestCase representative = c.getRepresentative(representativeSelection, dissimilarityMeasure);
			System.out.println("Representative: " + representative + " \tRevealed Fault: " + representative.getFault());
			revealedFaults.add(representative.getFault());
		}
		if (revealedFaults.size() != faults.size()) {
			System.err.println("Not all faults could be revealed by the representative Tests!");
		}
	}
	private void FMeasure() {
		//TODO: implement more methods for evaluation and metric calculations
		//		Red, ARed
		//		F
		//		purity, entropy, precision, recall
	}
}
