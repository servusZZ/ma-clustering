package data_import.faulty_versions;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import faulty_project.globals.FaultyProjectGlobals;
import prioritization.data_objects.FaultyVersion;
import prioritization.data_objects.TrainSet;

public class FaultyVersionsReader {
	private static int filesCounter = 1;
	
	public static void resetFilesCounter() {
		filesCounter = 1;
	}
	
	/**
	 * equivalent to the hasNext() method of an Iterator<List<FaultyVersion>>
	 */
	public static boolean hasNextFaultyVersionsFile(String dir) throws IOException{
		String outputFile = getFaultyVersionsFileName(dir, filesCounter);
		return Files.exists(Paths.get(outputFile));
	}
	/**
	 * equivalent to the next() method of an Iterator<List<FaultyVersion>>.
	 * Only returns faulty versions that are contained in the test set.
	 */
	public static List<FaultyVersion> importNextFaultyVersionsFile_TestSet(String dir, TrainSet trainSet) throws IOException{
		String outputFile = getFaultyVersionsFileName(dir, filesCounter);
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<FaultyVersion> faultyVersions = (List<FaultyVersion>) decoder.readObject();
		decoder.close();
		fis.close();
		filesCounter++;
		trainSet.filterOutTrainSetVersions(faultyVersions);
		initTestCases(faultyVersions);
		return faultyVersions;
	}
	/**
	 * Imports all files of the passed dir and searches for the faultyVersionID. Returns the FaultyVersion with the
	 * respective ID.
	 * Returns null, iff the faultyVersionID doesn't exist.
	 */
	public static FaultyVersion importSpecificVersion(String dir, String faultyVersionID) throws IOException{
		while (FaultyVersionsReader.hasNextFaultyVersionsFile(dir)) {
			// import next file
			String outputFile = getFaultyVersionsFileName(dir, filesCounter);
			FileInputStream fis = new FileInputStream(new File(outputFile));
			XMLDecoder decoder = new XMLDecoder(fis);
			@SuppressWarnings("unchecked")
			List<FaultyVersion> faultyVersions = (List<FaultyVersion>) decoder.readObject();
			decoder.close();
			fis.close();
			filesCounter++;
			for (FaultyVersion faultyVersion : faultyVersions) {
				if (faultyVersionID.equals(faultyVersion.getFaultyVersionId())) {
					initTestCases(faultyVersion);
					return faultyVersion;
				}
			}
		}
		return null;
	}
	/**
	 * equivalent to the next() method of an Iterator<List<FaultyVersion>>.
	 */
	public static List<FaultyVersion> importNextFaultyVersionsFile(String dir) throws IOException{
		String outputFile = getFaultyVersionsFileName(dir, filesCounter);
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<FaultyVersion> faultyVersions = (List<FaultyVersion>) decoder.readObject();
		decoder.close();
		fis.close();
		filesCounter++;
		initTestCases(faultyVersions);
		return faultyVersions;
	}
	/**
	 * equivalent to the next() method of an Iterator<List<FaultyVersion>>.
	 * Only returns faulty versions that are contained in the train set.
	 */
	public static List<FaultyVersion> importNextFaultyVersionsFile_TrainSet(String dir, TrainSet trainSet) throws IOException{
		String outputFile = getFaultyVersionsFileName(dir, filesCounter);
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<FaultyVersion> faultyVersions = (List<FaultyVersion>) decoder.readObject();
		decoder.close();
		fis.close();
		filesCounter++;
		trainSet.retainOnlyTrainSetVersions(faultyVersions);
		initTestCases(faultyVersions);
		return faultyVersions;
	}
	
	private static String getFaultyVersionsFileName(String dir, int filesCounter) {
		return dir + "faulty-versions-" + filesCounter + ".xml";
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
	/**
	 * Inits the coverage fields of the Test Case objects.
	 */
	private static void initTestCases(FaultyVersion faultyVersion) {
		FaultyProjectGlobals.init(faultyVersion);
		for (int i = 0; i < FaultyProjectGlobals.failuresCount; i++) {
			faultyVersion.getFailures()[i].initBooleanAndNumericCoverage();
		}
		for (int i = 0; i < faultyVersion.getPassedTCs().length; i++) {
			faultyVersion.getPassedTCs()[i].initBooleanAndNumericCoverage();
		}
	}
}
