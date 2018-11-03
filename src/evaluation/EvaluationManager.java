package evaluation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		boolean allFaultsRevealed = evaluateFaultDetection();
		if (evaluateRepresentativeSelection()) {
			System.out.println("Representative selection is successful. All Representatives reveal the major Fault in their clusters.");
		} else {
			System.out.println("Representative selection failed!");
		}
		System.out.println("Purity of Clusters: " + purity());
		f1Measure();
		if (allFaultsRevealed) {
			evaluateReduction();
		}
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
	 * returns whether the check was successful (true) or not (false).
	 */
	private boolean evaluateFaultDetection() {
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
			System.err.println("ERROR: Not all faults could be revealed by the representative Tests!\n");
			System.out.println("Remaining Faults: " + remainingFaults);
			return false;
		}
		System.out.println("Fault Revealing is successful. All Faults are revealed by at least one representative.");
		return true;
	}
	private boolean evaluateRepresentativeSelection() {
		//TODO: Fall prüfen, dass >1 Faults am häufigsten vorkommen
		//		dann wären alle Faults mit den häufigsten Vorkommnissen als Repräsentativen ok
		boolean evaluationSuccessful = true;
		for (Cluster c: clusters) {
			if (!c.getMajorFaults().contains(c.getRepresentative().getFault())) {
				System.err.println("ERROR: The Representative " + c.getRepresentative() + " of Cluster " + c + " reveals the Fault " + c.getRepresentative().getFault() + ". However the major Faults of the cluster are " + c.getMajorFaults() + "\n");
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
	/**
	 * precision = TP / (TP + FP)
	 * recall = TP / (TP + FN)
	 * F1-Score = 2 * (precision x recall)/(precision + recall)	
	 */
	private void f1Measure() {
		Map<Fault, Integer> faultToIndexMapping = getFaultToIndexMapping();
		int[][] failuresPerFaultPerCluster = new int[clusters.size()][];
		int i = 0;
		for (Cluster c: clusters) {
			failuresPerFaultPerCluster[i] = c.getFailuresPerFaultCount(faultToIndexMapping);
			i++;
		}
		int[] terms = calculateTPandFP(failuresPerFaultPerCluster, faultToIndexMapping);
		int TP = terms[0];
		int FP = terms[1];
		int FN = calculateFalseNegatives(failuresPerFaultPerCluster, faultToIndexMapping);
		double precision = (double) TP / (TP + FP);
		double recall = (double) TP / (TP + FN);
		double F1 = 2.0 * ((precision * recall) / (precision + recall));
		System.out.println("Precision:  " + precision);
		System.out.println("Recall:     " + recall);
		System.out.println("F1-Score:   " + F1);
	}
	/**
	 * FN = 2 Failures with same faults assigned to different clusters
	 */
	private int calculateFalseNegatives(int[][] failuresPerFaultPerCluster, Map<Fault, Integer> faultToIndexMapping) {
		int FN = 0;
		// loop over faults to calculate FN
		for (Map.Entry<Fault, Integer> entry: faultToIndexMapping.entrySet()) {
			for (int i = 0; i < failuresPerFaultPerCluster.length; i++) {
				for (int k = i+1; k < failuresPerFaultPerCluster.length; k++) {
					FN += failuresPerFaultPerCluster[i][entry.getValue()] * failuresPerFaultPerCluster[k][entry.getValue()];
				}
				
			}
		}
		return FN;
	}
	/**
	 * [0] = TP = 2 Failures grounded in the same fault in the same cluster
	 * [1] = FP = 2 Failures diff faults same cluster
	 */
	private int[] calculateTPandFP(int[][] failuresPerFaultPerCluster, Map<Fault, Integer> faultToIndexMapping) {
		int[] terms = new int[2];
		// loop over clusters to calculate TP and FP
		for (int i = 0; i < failuresPerFaultPerCluster.length; i++) {
			for (int j = 0; j < faultToIndexMapping.size(); j++) {
				terms[0] += (failuresPerFaultPerCluster[i][j] * (failuresPerFaultPerCluster[i][j]-1))/2;
				for (int k = j+1; k < faultToIndexMapping.size(); k++) {
					terms[1] += failuresPerFaultPerCluster[i][j] * failuresPerFaultPerCluster[i][k];
				}
			}
		}
		return terms;
	}
	private Map<Fault, Integer> getFaultToIndexMapping(){
		Map<Fault, Integer> faultToIndexMapping = new HashMap<Fault, Integer>();
		int i = 0;
		for (Fault f: faults) {
			faultToIndexMapping.put(f, i);
			i++;
		}
		return faultToIndexMapping;
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
