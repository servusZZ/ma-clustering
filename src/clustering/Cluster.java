package clustering;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import data_objects.DStarTerms;
import data_objects.Fault;
import data_objects.TestCase;
import evaluation.RepresentativeSelectionStrategy;
import hac.experiment.custom.CustomDissimilarityMeasure;
import main.Main;
import utils.PrintUtils;
import utils.SortingUtils;

public class Cluster {
	/**	(sorted) list of methodIDs which are most suspicious **/
	private Set<Integer> suspiciousSet;
	private List<TestCase> failedTCs;
	/**	values for Ncf, Nuf, Ncs for each method **/
	private DStarTerms[] methodDStarTerms;
	/**	the representative TC of the cluster, stores the result of the last call of computeRepresentative()*/
	private TestCase representative;
	/**	the fault that has the majority in the cluster	*/
	private Fault majorFault;
	/**	D* suspiciousness value for each method	 **/
	Map<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
	
	public Cluster(List<TestCase> failedTCs, DStarTerms[] passedMethodDStarTerms) {
		this.failedTCs = failedTCs;
		initMethodDStarTerms(passedMethodDStarTerms);
		updateSupsiciousSet(failedTCs);
		computeMajorFault();
	}
	private void initMethodDStarTerms(DStarTerms[] passedMethodDStarTerms) {
		methodDStarTerms = new DStarTerms[Main.methodsCount];
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i] = passedMethodDStarTerms[i].clone();
		}
	}
	public void dump() {
		System.out.println("Cluster  " + toString());
		System.out.println("    D4 Values       [" + PrintUtils.printMapValuesOrdered(methodDStarSusp) + "]");
		System.out.println("    Suspicious Set  " + suspiciousSet.toString());
	}
	/**
	 * Returns the name of the Cluster as concatenation of the contained Failures.
	 */
	@Override
	public String toString() {
		return ("[" + PrintUtils.printTestCasesList(failedTCs) + "]");
	}
	/**
	 * Computes the representative of the Cluster. Depends on a representative selection strategy and
	 * the distance computation of two Test Cases.
	 * @return
	 */
	public TestCase computeRepresentative(RepresentativeSelectionStrategy strat, CustomDissimilarityMeasure dis) {
		representative = strat.selectRepresentative(this, dis);
		return representative;
	}
	/**
	 * Computes the center of the Cluster, given some Distance metric.
	 * returns the 'coordinates' (i.e. the method coverage array) of the center.
	 * Center = The point for which the sum to all points in the cluster is minimal
	 */
	public double[] getCenter(CustomDissimilarityMeasure dis) {
		return dis.computeCenter(failedTCs);
	}
	/**
	 * calculates the suspicious set.
	 * @param newFailedTCs
	 */
	private void updateSupsiciousSet(List<TestCase> newFailedTCs) {
		if (newFailedTCs == null || newFailedTCs.size() == 0) {
			System.err.println("New Failed TCs are null or empty. No reason to update the susp Set.");
			return;
		}
		for (int i = 0; i < Main.methodsCount; i++) {
			//TODO: updateTermValues soll boolean zurückgeben, welcher signalisiert ob sich ein Wert geändert hat
			//		methodDStarSusp speichern und nur neu berechnen, falls sich ein TermValue geändert hat.
			methodDStarTerms[i].updateTermValues(newFailedTCs);
			methodDStarSusp.put(i, methodDStarTerms[i].getD4Suspiciousness());
		}
		suspiciousSet = retrieveMostSuspiciousSet(methodDStarSusp);
		return;
	}
	/**
	 * Sorts the methodDStarSusp Map and returns only the "most suspicious" elements, defined by the threshold.
	 * @param methodDStarSusp
	 * @return
	 */
	private Set<Integer> retrieveMostSuspiciousSet(Map<Integer, Double> methodDStarSusp) {
		Map<Integer, Double> sortedMethodDStarSusp = SortingUtils.getSortedMapByValuesDescending(methodDStarSusp);
		Set<Integer> mostSusp = new LinkedHashSet<>();
		int lastSuspEntry = (int) (sortedMethodDStarSusp.size() * Main.MOST_SUSP_THRESHOLD);
		if(lastSuspEntry < 1) {
			lastSuspEntry = 1;
		}
		else if(lastSuspEntry > Main.MOST_SUSP_MAX_COUNT) {
			lastSuspEntry = Main.MOST_SUSP_MAX_COUNT;
		}
		Iterator<Integer> it = sortedMethodDStarSusp.keySet().iterator();
		for (int i = 0; i < lastSuspEntry; i++) {
			mostSusp.add(it.next());
		}
		return mostSusp;
	}
	private void computeMajorFault() {
		int max = -1;
		Map<Fault, Integer> faultCounts = new HashMap<Fault, Integer>();
		for (TestCase tc: failedTCs) {
			faultCounts.put(tc.getFault(), (faultCounts.getOrDefault(tc.getFault(), 0) + 1));
		}
		for (Map.Entry<Fault, Integer> entry: faultCounts.entrySet()) {
			if (max < entry.getValue()) {
				majorFault = entry.getKey();
			}
		}
	}
	public Set<Integer> getSuspiciousSet() {
		return suspiciousSet;
	}
	public List<TestCase> getFailedTCs() {
		return failedTCs;
	}
	public TestCase getRepresentative() {
		return representative;
	}
	public Fault getMajorFault() {
		return majorFault;
	}
	public int correctlyAssignedTCs() {
		int correctlyAssignedCount = 0;
		for (TestCase tc: failedTCs) {
			if (tc.getFault() == majorFault) {
				correctlyAssignedCount++;
			}
		}
		return correctlyAssignedCount;
	}
}
