package data_import.pit;

import java.util.List;
import java.util.Set;

import data_import.pit.data_objects.PitMutation;
import data_import.pit.data_objects.PitTestCase;

/**
 * Builds faulty version for a pit project that should be analyzed.
 * A faulty version means to select a subset of faults out of all faults of the project.
 */
public abstract class PitFaultSelectionStrategyBase {
	private List<PitMutation> pitFaults;
	private Set<PitTestCase> pitTests;
	public abstract List<PitMutation> selectFaultyVersions();
}
