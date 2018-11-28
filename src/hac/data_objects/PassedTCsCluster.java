package hac.data_objects;

import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.TestCase;

public class PassedTCsCluster {
	public final TestCase[] passedTCs;
	private DStarTerms[] methodDStarTerms;

	public PassedTCsCluster(TestCase[] passedTCs) {
		this.passedTCs = passedTCs;
		initDStarTerms();
	}
	
	private void initDStarTerms() {
		methodDStarTerms = new DStarTerms[FaultyProjectGlobals.methodsCount];
		for (int i = 0; i < FaultyProjectGlobals.methodsCount; i++) {
			methodDStarTerms[i] = new DStarTerms(i);
			methodDStarTerms[i].updateTermValues(passedTCs);
		}
	}	
	public DStarTerms[] getMethodDStarTerms() {
		return methodDStarTerms;
	}
}
