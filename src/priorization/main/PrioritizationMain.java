package priorization.main;

import java.io.File;
import java.io.IOException;

public class PrioritizationMain {
	private static final String PIT_MUTATIONS_BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data\\";
	
//	for each pit project
//	processProject
//		import & analyze for each File
//	or: printStatistics
	public static void main(String[] args) throws IOException {
		
//		String projectName = "commons-geometry";
//		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data\\" +  projectName + "\\pit-data\\";
//		String projectName = "test-lessVersions";
//		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_faultyVersions\\" + projectName + "\\pit-data\\";	
//		wrapper.processProject(dir, projectName);
	}
	private static void analyzeTrainSet() throws IOException {
		
	}
	private static void printProjects() throws IOException {
		System.out.println("Program started...");
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		File[] projectDirectories = new File(PIT_MUTATIONS_BASE_DIR).listFiles(File::isDirectory);
		for (File projectDir : projectDirectories) {
			String projectName = projectDir.getPath().substring(projectDir.getPath().lastIndexOf('\\') + 1, projectDir.getPath().length());
			String dir = projectDir.getPath() + "\\pit-data\\";
			System.out.println("Processing project " + projectName);
			wrapper.printProject(dir, projectName);
		}
		System.out.println("Printing Projects finished!");
	}
}
