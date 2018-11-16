package data_import.pit;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import data_import.pit.data_objects.EPitMutationStatus;
import data_import.pit.data_objects.PitMethod;
import data_import.pit.data_objects.PitMutation;
import data_import.pit.data_objects.PitTestCase;
/**
 * 1. Import all PitMethods and Tests
 * 		K Times
 * 		2. wähle Set aus Faults
 * 		3. --> hieraus ergibt sich set aus Failures
 * 		4. --> nur Methoden berücksichtigen, welche in Failures vorkommen
 * 		5. --> nur passing TestCases berücksichtigen, welche mind. eine dieser Methoden covern
 */
public class PitAnalysisPreparation {
	private List<PitMethod> pitMethods;
	private List<PitMutation> pitFaults;
	private Set<PitTestCase> pitTests;
	

	public PitAnalysisPreparation(String projectDir) throws IOException {
		pitMethods = PitMergedMutationsReader.readPitMergedMethods(projectDir);
		initPitTests();
		initPitFaults();
	}
	private void initPitTests() {
		for (PitMethod method: pitMethods) {
			pitTests.addAll(method.getCoveringTests());
		}
	}
	private void initPitFaults() {
		for (PitMethod method: pitMethods) {
			for (PitMutation mutation: method.getMutations()) {
				if (EPitMutationStatus.isFault(mutation)) {
					pitFaults.add(mutation);
				}
			}
		}
	}
}
