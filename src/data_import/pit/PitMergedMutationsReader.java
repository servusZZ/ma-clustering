package data_import.pit;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import data_export.PitMergedMutationsWriter;
import data_import.pit.data_objects.PitMethod;
import data_import.pit.data_objects.PitTestCase;

public class PitMergedMutationsReader {

	/**	
	 * Reads all objects and returns a List of PitMethods.
	 * The PitTestCase and PitMutation objects are referenced by the methods.
	 */
	public static List<PitMethod> readPitMergedMethods(String dir) throws IOException{
		String outputFile = dir + PitMergedMutationsWriter.MERGED_METHODS_FILE_NAME;
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<PitMethod> methods = (List<PitMethod>) decoder.readObject();
		decoder.close();
		fis.close();
		return methods;
	}
	
	/*public static List<PitTestCase> readPitAllTests(String dir) throws IOException{
		String outputFile = dir + PitMergedMutationsWriter.MERGED_TESTS_FILE_NAME;
		FileInputStream fis = new FileInputStream(new File(outputFile));
		XMLDecoder decoder = new XMLDecoder(fis);
		@SuppressWarnings("unchecked")
		List<PitTestCase> tests = (List<PitTestCase>) decoder.readObject();
		decoder.close();
		fis.close();
		return tests;
	}	*/
}
