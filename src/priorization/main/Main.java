package priorization.main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		//		for each pit project
		//			importProject (prepares faulty versions)
		//			runAnalysis
		System.out.println("Program started...");
		String projectName = "biojava-test-small";
		System.out.println("Print statistics for project " + projectName);
		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_merged\\" +  projectName + "\\pit-data\\";
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		//wrapper.printStatistics(dir);
		wrapper.importProject(dir, projectName);
		wrapper.analyze();
	}
}
