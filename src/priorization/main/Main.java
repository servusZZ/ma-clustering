package priorization.main;

import java.io.IOException;

public class Main {
	public static final double MOST_SUSP_THRESHOLD = 0.2;
	public static final int MOST_SUSP_MAX_COUNT = 15;
	public static final double SIMILARITY_THRESHOLD = 0.85;
	//TODO: Berechnung für MOST_SUSP_SET überlegen
	//	nur Werte aufnehmen, welche nicht 0.0 sind
	//		dann muss ich berücksichtigen, wenn 2 sets unterschiedliche Größe haben
	//			--> eher parameter trainieren
	//	MOST_SUSP_MAX_COUNT verwenden? --> denke eher nicht
	public static final double MAX_SUSP_VALUE = 1000000000;
	
	public static void main(String[] args) throws IOException {
		//		for each pit project
		//			importProject (prepares faulty versions)
		//			runAnalysis
		System.out.println("Program started...");
		String projectName = "biojava-test-small";
		System.out.println("Print statistics for project " + projectName);
		String dir = "C:\\study\\SWDiag\\sharedFolder_UbuntuVM\\MA\\pit_data_merged\\" +  projectName + "\\pit-data\\";
		//List<PitMethod> methods = PitMergedMutationsReader.readPitMergedMethods(dir);
		AnalysisWrapper	wrapper = new AnalysisWrapper();
		//wrapper.printStatistics(dir);
		wrapper.importProject(dir, projectName);
		wrapper.analyze();
	}
}
