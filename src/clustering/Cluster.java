package clustering;

import java.util.List;

import data_objects.DStarTerms;
import data_objects.TestCase;

public class Cluster {
	private List<Integer> suspiciousSet;
	private List<TestCase> passedTCs;
	private List<TestCase> failedTCs;
	private List<TestCase> newFailedTCs;
	/**	values for Ncf, Nuf, Ncs for each method **/
	private DStarTerms[] methodDStarTerms;
	
	public Cluster(List<TestCase> passedTCs, List<TestCase> failedTCs) {
		this.passedTCs = passedTCs;
		this.failedTCs = failedTCs;
	}
	
	public void initSuspiciousnesSet() {
		if (passedTCs == null || failedTCs == null || failedTCs.size() == 0) {
			System.err.println("Invalid amount of passed or failed TCs or one of the fields is null.");
		}
		//TODO: go on here
		//		test DStarTerms for small matrix
		methodDStarTerms = null;
		return;
	}
	public void updateSupsiciousnesSet() {
		
	}
}
