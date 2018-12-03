package data_import.faulty_versions;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import directories.globals.Directories;
import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;

public class FaultyVersionsReader {

	public static List<FaultyVersion> importFaultyVersions(String dir) throws IOException{
		String outputFile = dir + Directories.FAULTY_VERSIONS_FILE_NAME;
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<FaultyVersion> faultyVersions = (List<FaultyVersion>) decoder.readObject();
		decoder.close();
		fis.close();
		initTestCases(faultyVersions);
		return faultyVersions;
	}
	
	/**
	 * Inits the coverage fields of the Test Case objects.
	 */
	private static void initTestCases(List<FaultyVersion> faultyVersions) {
		for (FaultyVersion faultyVersion: faultyVersions) {
			FaultyProjectGlobals.init(faultyVersion);
			for (int i = 0; i < FaultyProjectGlobals.failuresCount; i++) {
				faultyVersion.getFailures()[i].initBooleanAndNumericCoverage();
			}
			for (int i = 0; i < faultyVersion.getPassedTCs().length; i++) {
				faultyVersion.getPassedTCs()[i].initBooleanAndNumericCoverage();
			}
		}
	}
}
