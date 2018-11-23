package priorization.main;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		//		for each pit project
		//			importProject (prepares faulty versions)
		//			runAnalysis
		System.out.println("Program started...");
		String projectName = "biojava";
		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_merged\\" +  projectName + "\\pit-data\\";
		System.out.println("Processing project " + projectName);
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		wrapper.importProject(dir, projectName);
		wrapper.analyze();
//		System.out.println("Print statistics for project " + projectName);
//		wrapper.printStatistics(dir);
	}
}
