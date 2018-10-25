package data_objects;

import java.util.List;

public class DStarTerms {
	private int n_cf = 0;
	private int n_uf = 0;
	private int n_cs = 0;

	private final Integer methodID;
	
	public DStarTerms(int methodID) {
		this.methodID = methodID;
	}
	public void updateTermValues(List<TestCase> testCases) {
		for (TestCase tc : testCases) {
			if (tc.coveredMethods.contains(methodID)) {
				if (tc.passed) {
					n_cs++;
				} else {
					n_cf++;
				}
			} else {
				if (!tc.passed) {
					n_uf++;
				}
			}
		}
	}
	public double getD4Suspiciousnes() {
		//TODO:
		//	Ausnahmefälle mit Nenner = 0 berücksichtigen --> Wie?
		return (Math.pow(n_cf, 4) / (n_uf + n_cs));
	}
	public int getN_cf() {
		return n_cf;
	}
	public int getN_uf() {
		return n_uf;
	}
	public int getN_cs() {
		return n_cs;
	}
	
}
