package hac.data_objects;

import data_objects.TestCase;
import priorization.main.AnalysisWrapper;

public class PassedTCsCluster {
	public final TestCase[] passedTCs;
	private DStarTerms[] methodDStarTerms;

	public PassedTCsCluster(TestCase[] passedTCs) {
		this.passedTCs = passedTCs;
		initDStarTerms();
	}
	
	private void initDStarTerms() {
		methodDStarTerms = new DStarTerms[AnalysisWrapper.methodsCount];
		for (int i = 0; i < AnalysisWrapper.methodsCount; i++) {
			methodDStarTerms[i] = new DStarTerms(i);
			methodDStarTerms[i].updateTermValues(passedTCs);
		}
	}	
	public DStarTerms[] getMethodDStarTerms() {
		return methodDStarTerms;
	}
}
