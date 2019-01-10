package priorization.main;

import java.io.File;
import java.io.IOException;

public class PrioritizationMain {
	private static final String PIT_MUTATIONS_BASE_DIR = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data\\";
//	private static final String PIT_MUTATIONS_BASE_DIR = "F:\\Backups\\pit_data\\";
	
//	for each pit project
//	processProject
//		import & analyze for each File
//	or: printStatistics
	public static void main(String[] args) throws IOException {
		analyzeTestSet();
//		String projectName = "commons-geometry";
//		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data\\" +  projectName + "\\pit-data\\";
//		String projectName = "test-lessVersions";
//		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_faultyVersions\\" + projectName + "\\pit-data\\";	
//		wrapper.processProject(dir, projectName);
	}
	private static void analyzeTestSet() throws IOException {
		System.out.println("Analysis of Test Set started...");
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		File[] projectDirectories = new File(PIT_MUTATIONS_BASE_DIR).listFiles(File::isDirectory);
		for (File projectDir : projectDirectories) {
			String projectName = projectDir.getPath().substring(projectDir.getPath().lastIndexOf('\\') + 1, projectDir.getPath().length());
			String dir = projectDir.getPath() + "\\pit-data\\";
			System.out.println("Processing project " + projectName);
			wrapper.processProject(dir, projectName);
		}
		System.out.println("Analysis of Test Set finished!");
	}
	
	@SuppressWarnings("unused")
	private static void analyzeTrainSetStrictVersions() throws IOException{
		System.out.println("Analysis of Train Set started...");
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		File[] projectDirectories = new File(PIT_MUTATIONS_BASE_DIR).listFiles(File::isDirectory);
		for (File projectDir : projectDirectories) {
			String projectName = projectDir.getPath().substring(projectDir.getPath().lastIndexOf('\\') + 1, projectDir.getPath().length());
			String dir = projectDir.getPath() + "\\pit-data\\";
			System.out.println("Processing project " + projectName);
			wrapper.analyzeTrainSetStrictConfigs(dir, projectName);
		}
		System.out.println("Analysis of Train Set finished!");
	}
	@SuppressWarnings("unused")
	private static void analyzeTrainSet() throws IOException {
		System.out.println("Analysis of Train Set started...");
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		File[] projectDirectories = new File(PIT_MUTATIONS_BASE_DIR).listFiles(File::isDirectory);
		for (File projectDir : projectDirectories) {
			String projectName = projectDir.getPath().substring(projectDir.getPath().lastIndexOf('\\') + 1, projectDir.getPath().length());
			String dir = projectDir.getPath() + "\\pit-data\\";
			System.out.println("Processing project " + projectName);
			wrapper.analyzeTrainSet(dir, projectName);
		}
		System.out.println("Analysis of Train Set finished!");
	}
	
	@SuppressWarnings("unused")
	private static void debugSpecificVersions() throws IOException {
		String[] projects = {""};
	}
	@SuppressWarnings("unused")
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
