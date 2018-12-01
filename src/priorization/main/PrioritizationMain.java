package priorization.main;

import java.io.IOException;

public class PrioritizationMain {
	
	public static void main(String[] args) throws IOException {
//				for each pit project
//					importProject (prepares faulty versions)
//					runAnalysis
//					or: printStatistics
		System.out.println("Program started...");
		String projectName = "commons-geometry";
		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data\\" +  projectName + "\\pit-data\\";
		System.out.println("Processing project " + projectName);
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		wrapper.importProject(dir, projectName);
		wrapper.analyze();
//		System.out.println("Print statistics for project " + projectName);
//		wrapper.printStatistics(dir);
	}
}
