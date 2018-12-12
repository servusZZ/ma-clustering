package data_import.faulty_versions;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import directories.globals.Directories;
import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;

public class FaultyVersionsReader {

	public static List<FaultyVersion> importFaultyVersions(String dir) throws IOException{
		List<FaultyVersion> faultyVersions = new ArrayList<FaultyVersion>();
		int filesCounter = 1;
		String outputFile = dir + "faulty-versions-1.xml";
		while (Files.exists(Paths.get(outputFile))) {
			FileInputStream fis = new FileInputStream(new File(outputFile));
			XMLDecoder decoder = new XMLDecoder(fis);
			@SuppressWarnings("unchecked")
			List<FaultyVersion> tmpfaultyVersions = (List<FaultyVersion>) decoder.readObject();
			faultyVersions.addAll(tmpfaultyVersions);
			decoder.close();
			fis.close();
			filesCounter++;
			outputFile = dir + "faulty-versions-" + filesCounter + ".xml";
		}
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
