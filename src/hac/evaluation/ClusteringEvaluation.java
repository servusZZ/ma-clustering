package hac.evaluation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import faulty_project.globals.FaultyProjectGlobals;
import hac.data_objects.Cluster;
import hac.experiment.custom.CustomDissimilarityMeasure;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;

public class ClusteringEvaluation {
	private CustomDissimilarityMeasure dissimilarityMeasure;
	private RepresentativeSelectionStrategy representativeSelection;
	private List<Cluster> clusters;
	private Set<Fault> faults;
	private ClusteringEvaluationEntry clusteringMetrics;
	
	public ClusteringEvaluation(CustomDissimilarityMeasure dissimilarityMeasure, RepresentativeSelectionStrategy representativeSelection, List<Cluster> clusters, Set<Fault> faults) {
		this.dissimilarityMeasure = dissimilarityMeasure;
		this.representativeSelection = representativeSelection;
		this.clusters = clusters;
		this.faults = faults;
	}
	/**
	 * calls all metric computations/ evaluation methods and print results
	 */
	public void evaluateClustering() {
		int[] repSelectionSuccessfulFailedCount = evaluateRepresentativeSelection();
		System.out.println("DEBUG: The representative selection was successful for " + repSelectionSuccessfulFailedCount[0] + " clusters.");
		System.out.println("DEBUG: The representative selection failed for " + repSelectionSuccessfulFailedCount[1] + " clusters.");
		double purity = purity();
		System.out.println("DEBUG: Purity of Clusters:       " + purity);
		//TODO: _FF, f1Measure, precision, recall und fault Entropy für multiple underlying faults
		//			nicht berücksichtigen (zumindest im Excel dann ignorieren)
		Map<Fault, Integer> faultToIndexMapping = getFaultToIndexMapping();
		int[][] failuresPerFaultPerCluster = getFailuresPerFaultPerCluster(faultToIndexMapping);
		
		double[] precisionRecallF1 = f1Measure(failuresPerFaultPerCluster, faultToIndexMapping);
		System.out.println("DEBUG: Precision:                " + precisionRecallF1[0]);
		System.out.println("DEBUG: Recall:                   " + precisionRecallF1[1]);
		System.out.println("DEBUG: F1-Score:                 " + precisionRecallF1[2]);
		double totalFaultEntropy = totalFaultEntropy(failuresPerFaultPerCluster, faultToIndexMapping);
		System.out.println("DEBUG: Total Deviation Entropy:  " + totalFaultEntropy);
		clusteringMetrics = new ClusteringEvaluationEntry(dissimilarityMeasure.getClass().getSimpleName(),
				clusters.size(), representativeSelection.getClass().getSimpleName(),
				repSelectionSuccessfulFailedCount[0], repSelectionSuccessfulFailedCount[1],
				purity, precisionRecallF1[0], precisionRecallF1[1], precisionRecallF1[2], totalFaultEntropy);
	}


	public ClusteringEvaluationEntry getClusteringMetrics() {
		return clusteringMetrics;
	}
	/**
	 * Counts the clusters for which the representative selection was successful (result[0])
	 * and for which clusters it failed (result[1]).
	 * 'Successful' means, that the representative reveals the major fault in the respective
	 * cluster.
	 */
	private int[] evaluateRepresentativeSelection() {
		int successful = 0, failed = 0;
		boolean tmpIsSuccessful;
		for (Cluster c: clusters) {
			tmpIsSuccessful = false;
			TestCase representative = c.computeRepresentative(representativeSelection, dissimilarityMeasure);
			for (Fault fault: representative.getFaults()) {
				if (c.getMajorFaults().contains(fault)) {
					tmpIsSuccessful = true;
					successful++;
					break;
				}
			}
			if (!tmpIsSuccessful) {
				failed++;
			}
		}
		return new int[] {successful, failed};
	}
	/**
	 * precision = TP / (TP + FP)
	 * recall = TP / (TP + FN)
	 * F1-Score = 2 * (precision x recall)/(precision + recall)	
	 */
	private double[] f1Measure(int[][] failuresPerFaultPerCluster, Map<Fault, Integer> faultToIndexMapping) {
		int[] terms = calculateTPandFP(failuresPerFaultPerCluster, faultToIndexMapping);
		int TP = terms[0];
		int FP = terms[1];
		int FN = calculateFalseNegatives(failuresPerFaultPerCluster, faultToIndexMapping);
		double precision = (double) TP / (TP + FP);
		double recall = (double) TP / (TP + FN);
		double F1 = 2.0 * ((precision * recall) / (precision + recall));
		return new double[] {precision, recall, F1};
	}
	/**
	 * Computes the deviation entropy (entropy of the underlying faults).
	 * The cluster entropy measured by the purity (1 is the best value).
	 */
	private double totalFaultEntropy(int[][] failuresPerFaultPerCluster, Map<Fault, Integer> faultToIndexMapping) {
		double totalDeviationEntropy = 0.0;
		for (Map.Entry<Fault, Integer> faultToIndex:faultToIndexMapping.entrySet()) {
			double deviationEntropyPerFault = deviationEntropyPerFault(failuresPerFaultPerCluster, faultToIndex.getValue());
			System.out.println("   Deviation Entropy of " + faultToIndex.getKey().id + " is " + deviationEntropyPerFault);
			totalDeviationEntropy += deviationEntropyPerFault;
		}
		return totalDeviationEntropy;
	}
	private double deviationEntropyPerFault(int[][] failuresPerFaultPerCluster, int faultIndex) {
		int sumOfFailures = 0;
		double deviationEntropy = 0.0;
		for (int i = 0; i < failuresPerFaultPerCluster.length; i++) {
			sumOfFailures += failuresPerFaultPerCluster[i][faultIndex];
		}
		for (int i = 0; i < failuresPerFaultPerCluster.length; i++) {
			if (failuresPerFaultPerCluster[i][faultIndex] == 0) {
				// assumption: 0log(0) = 0
				continue;
			}
			 deviationEntropy -= (((double)failuresPerFaultPerCluster[i][faultIndex] / (double)sumOfFailures) * 
					Math.log(((double)failuresPerFaultPerCluster[i][faultIndex] / (double)sumOfFailures)));
		}
		return deviationEntropy;
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
	private int[][] getFailuresPerFaultPerCluster(Map<Fault, Integer> faultToIndexMapping){
		int[][] failuresPerFaultPerCluster = new int[clusters.size()][];
		int i = 0;
		for (Cluster c: clusters) {
			failuresPerFaultPerCluster[i] = c.getFailuresPerFaultCount(faultToIndexMapping);
			i++;
		}
		return failuresPerFaultPerCluster;
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
		return (((double)correctlyAssignedSum)/FaultyProjectGlobals.failuresCount);
	}
	
	/*private Set<Fault> copyFaults(){
		Set<Fault> faultsCopy = new HashSet<Fault>(faults.size());
		for (Fault f: faults) {
			faultsCopy.add(f);
		}
		return faultsCopy;
	}	*/
	/**
	 * Checks whether all Faults would be detected by just fixing the representative Failures.
	 * returns whether the check was successful (true) or not (false).
	 * Not needed.
	 */
	/*private boolean evaluateFaultDetection() {
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
	}	*/
	/*private void evaluateReduction() {
	double reduction = (1 - ((double)clusters.size() / Main.failuresCount)) * 100;
	double idealReduction = (1 - ((double)faults.size() / Main.failuresCount)) * 100;
	double achievedReduction = (reduction / idealReduction) * 100;
	System.out.print("Reduction:                " + String.format("%.3g%n", reduction));
	System.out.print("ARed:                     " + String.format("%.3g%n", achievedReduction));
}	*/
}
