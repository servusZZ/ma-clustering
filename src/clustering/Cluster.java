package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import data_objects.DStarTerms;
import data_objects.TestCase;
import main.Main;
import utils.SortingUtils;

public class Cluster {
	/**	(sorted) list of methodIDs which are most suspicious **/
	private Set<Integer> suspiciousSet;
	private List<TestCase> failedTCs;
	/**	values for Ncf, Nuf, Ncs for each method **/
	private DStarTerms[] methodDStarTerms;
	
	public Cluster(List<TestCase> failedTCs, DStarTerms[] passedMethodDStarTerms) {
		this.failedTCs = failedTCs;
		initMethodDStarTerms(passedMethodDStarTerms);
		updateSupsiciousSet(failedTCs);
	}
	private void initMethodDStarTerms(DStarTerms[] passedMethodDStarTerms) {
		methodDStarTerms = new DStarTerms[Main.methodsCount];
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i] = passedMethodDStarTerms[i].clone();
		}
	}
	
	private void updateSupsiciousSet(List<TestCase> newFailedTCs) {
		if (newFailedTCs == null || newFailedTCs.size() == 0) {
			System.err.println("New Failed TCs are null or empty. No reason to update the susp Set.");
			return;
		}
		Map<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
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
		Iterator<Integer> it = sortedMethodDStarSusp.keySet().iterator();
		for (int i = 0; i < lastSuspEntry; i++) {
			mostSusp.add(it.next());
		}
		return mostSusp;
	}
	public Set<Integer> getSuspiciousSet() {
		return suspiciousSet;
	}
}
