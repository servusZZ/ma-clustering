package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import data_objects.DStarTerms;
import data_objects.TestCase;
import main.Main;

public class Cluster {
	/**	(sorted) list of methodIDs which are most suspicious **/
	private List<Integer> suspiciousSet;
	private List<TestCase> failedTCs;
	private List<TestCase> newFailedTCs;
	/**	values for Ncf, Nuf, Ncs for each method **/
	private DStarTerms[] methodDStarTerms;
	
	public Cluster(List<TestCase> newFailedTCs, DStarTerms[] passedMethodDStarTerms) {
		failedTCs = new ArrayList<TestCase>();
		this.newFailedTCs = newFailedTCs;
		methodDStarTerms = passedMethodDStarTerms.clone();
		updateSupsiciousSet(newFailedTCs);
	}
	
	public void updateSupsiciousSet(List<TestCase> newFailedTCs) {
		if (newFailedTCs == null || newFailedTCs.size() == 0) {
			System.err.println("New Failed TCs are null or empty. No reason to update the sups Set.");
			return;
		}
		HashMap<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i].updateTermValues(newFailedTCs);
			methodDStarSusp.put(i, methodDStarTerms[i].getD4Suspiciousnes());
		}
		//TODO: 	D* in HashMap speichern, methodID ist key
		//			sort hashmap, siehe blauer kommentar
		//			nur most Susp part wählen
		/**
		 * 
	final Map<String, Integer> sortedByCount = wordCounts.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		 */
		return;
	}

	public void merge(Cluster c2) {
		// failing TCs von c2 in c1 newFailing schreiben
		//	updateSuspiciousnesSet
	}
	public void dStarCalculation() {
		
	}
}
