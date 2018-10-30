package data_objects;

import java.util.List;

import main.Main;

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
	public double getD4Suspiciousness() {
		if ((n_uf + n_cs) == 0) {
			System.out.println("INFO: Denominator in D* formula is null for method " + methodID + ". Return max value instead.");
			return Main.MAX_SUSP_VALUE;
		}
		double susp = Math.pow(n_cf, 4) / (n_uf + n_cs);
		if (susp > Main.MAX_SUSP_VALUE) {
			System.out.println("WARNING: Maximum susp. value was exceeded by a normal susp value of " + susp);
		}
		return susp;
	}
	public DStarTerms clone() {
		DStarTerms clone = new DStarTerms(methodID);
		clone.setN_cf(n_cf);
		clone.setN_cs(n_cs);
		clone.setN_uf(n_uf);
		return clone;
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
	public void setN_cf(int n_cf) {
		this.n_cf = n_cf;
	}
	public void setN_uf(int n_uf) {
		this.n_uf = n_uf;
	}
	public void setN_cs(int n_cs) {
		this.n_cs = n_cs;
	}
	
}
