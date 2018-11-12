package hac.data_objects;

import data_objects.TestCase;
import priorization.main.Main;

public class PassedTCsCluster {
	public final TestCase[] passedTCs;
	private DStarTerms[] methodDStarTerms;

	public PassedTCsCluster(TestCase[] passedTCs) {
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
	public DStarTerms[] getMethodDStarTerms() {
		return methodDStarTerms;
	}
}
