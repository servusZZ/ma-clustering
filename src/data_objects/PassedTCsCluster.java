package data_objects;

import java.util.List;

import main.Main;

public class PassedTCsCluster {
	public final List<TestCase> passedTCs;
	private DStarTerms[] methodDStarTerms;
	
	public DStarTerms[] getMethodDStarTerms() {
		return methodDStarTerms;
	}

	public PassedTCsCluster(List<TestCase> passedTCs) {
		this.passedTCs = passedTCs;
		initDStarTerms();
	}
	
	private void initDStarTerms() {
		methodDStarTerms = new DStarTerms[Main.methodsCount];
		for (int i = 0; i < Main.methodsCount; i++) {
			methodDStarTerms[i] = new DStarTerms(i);
			methodDStarTerms[i].updateTermValues(passedTCs);
		}
	}
}
