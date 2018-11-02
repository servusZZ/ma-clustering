package evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import clustering.Cluster;
import data_objects.Fault;
import data_objects.TestCase;
import hac.experiment.custom.CustomDissimilarityMeasure;
import main.Main;

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
		if (evaluateRepresentativeSelection()) {
			System.out.println("Representative selection is successful. All Representatives reveal the major Fault in their clusters.");
		} else {
			System.out.println("Representative selection failed!");
		}
		System.out.println("Purity of Clusters: " + purity());
		evaluateReduction();
	}
	private Set<Fault> copyFaults(){
		Set<Fault> faultsCopy = new HashSet<Fault>(faults.size());
		for (Fault f: faults) {
			faultsCopy.add(f);
		}
		return faultsCopy;
	}
	/**
	 * Checks whether all Faults would be detected by just fixing the representative Failures.
	 */
	private void evaluateFaultDetection() {
		System.out.println("Check whether all Faults are revealed...");
		System.out.println("All Faults: " + faults);
		//Set<Fault> revealedFaults = new HashSet<Fault>();
		Set<Fault> remainingFaults = copyFaults();
		for (Cluster c: clusters) {
			System.out.println("Cluster  " + c);
			TestCase representative = c.computeRepresentative(representativeSelection, dissimilarityMeasure);
			System.out.println("Representative: " + representative + " \tRevealed Fault: " + representative.getFault());
			//revealedFaults.add(representative.getFault());
			remainingFaults.remove(representative.getFault());
		}
		if (!remainingFaults.isEmpty()) {
			System.err.println("Not all faults could be revealed by the representative Tests!\n");
			System.out.println("Remaining Faults: " + remainingFaults);
			return;
		}
		System.out.println("Fault Revealing is successful. All Faults are revealed by at least one representative.");
	}
	private boolean evaluateRepresentativeSelection() {
		boolean evaluationSuccessful = true;
		for (Cluster c: clusters) {
			if (!(c.getRepresentative().getFault() == c.getMajorFault())) {
				System.err.println("The Representative " + c.getRepresentative() + " of Cluster " + c + " reveals the Fault " + c.getRepresentative().getFault() + ". However the major Fault of the cluster is " + c.getMajorFault());
				evaluationSuccessful = false;
			}
		}
		return evaluationSuccessful;
	}
	private void evaluateReduction() {
		double reduction = (1 - ((double)clusters.size() / Main.failuresCount)) * 100;
		double idealReduction = (1 - ((double)faults.size() / Main.failuresCount)) * 100;
		double achievedReduction = (reduction / idealReduction) * 100;
		System.out.println("Reduction: " + reduction + "%");
		System.out.println("ARed:      " + achievedReduction + "%");
	}
	private void FMeasure() {
		//TODO: implement more methods for evaluation and metric calculations
		//		F
		//		entropy
		//	precision = TP / (TP + FP)
		//  recall = TP / (TP + FN)
		// GO ON HERE
		//		denke für precision und recall muss ich Paare von TCs berücksichtigen
		//		mal nach unterschied zwischen precision und purity googlen
	}
	/**
	 * Calculates the ratio of correctly assigned test cases to clusters.
	 *   'Not correctly assigned' means that the underlying fault of a test case differs from the
	 *   major fault in the cluster. 
	 */
	private double purity() {
		int correctlyAssignedSum = 0;
		for (Cluster c: clusters) {
			correctlyAssignedSum += c.correctlyAssignedTCs();
		}
		return (((double)correctlyAssignedSum)/Main.failuresCount);
	}
}
