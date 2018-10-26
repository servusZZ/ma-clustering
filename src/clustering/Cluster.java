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
	private List<TestCase> newFailedTCs;
	/**	values for Ncf, Nuf, Ncs for each method **/
	private DStarTerms[] methodDStarTerms;
	
	public Cluster(List<TestCase> newFailedTCs, DStarTerms[] passedMethodDStarTerms) {
		failedTCs = new ArrayList<TestCase>();
		this.newFailedTCs = newFailedTCs;
		initMethodDStarTerms(passedMethodDStarTerms);
		updateSupsiciousSet(newFailedTCs);
	}
	public void initMethodDStarTerms(DStarTerms[] passedMethodDStarTerms) {
		methodDStarTerms = new DStarTerms[Main.methodsCount];
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i] = passedMethodDStarTerms[i].clone();
		}
	}
	
	public void updateSupsiciousSet(List<TestCase> newFailedTCs) {
		if (newFailedTCs == null || newFailedTCs.size() == 0) {
			System.err.println("New Failed TCs are null or empty. No reason to update the sups Set.");
			return;
		}
		Map<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i].updateTermValues(newFailedTCs);
			methodDStarSusp.put(i, methodDStarTerms[i].getD4Suspiciousnes());
		}
		suspiciousSet = retrieveMostSuspiciousSet(methodDStarSusp);
		return;
	}

	public Set<Integer> retrieveMostSuspiciousSet(Map<Integer, Double> methodDStarSusp) {
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
	public void merge(Cluster c2) {
		// failing TCs von c2 in c1 newFailing schreiben
		//	updateSuspiciousnesSet
	}
}
