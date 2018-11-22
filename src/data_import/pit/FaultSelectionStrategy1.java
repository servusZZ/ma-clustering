package data_import.pit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data_import.pit.data_objects.PitMutation;
import data_import.pit.data_objects.PitTestCase;

/**
 * Select faults by random.
 * Used for testing the whole prioritization pipeline.
 */
public class FaultSelectionStrategy1 extends PitFaultSelectionStrategyBase{
	
	public FaultSelectionStrategy1(int minFaultCount, int maxFaultCount, int versionsPerFaultCount) {
		super(minFaultCount, maxFaultCount, versionsPerFaultCount);
	}
	
	
	@Override
	public List<Set<PitMutation>> selectFaultyVersions(List<PitMutation> pitFaults, List<PitTestCase> pitTests) {
		//TODO: Versions per fault count berücksichtigen
		List<Set<PitMutation>> faultyVersions = new ArrayList<Set<PitMutation>>();
		for (int i = minFaultCount; i <= maxFaultCount; i++) {
			Set<PitMutation> faultyVersion = new HashSet<PitMutation>();
			for (int j = 0; j < i; j++) {
				faultyVersion.add(getRandomUniqueFault(faultyVersion, pitFaults));
			}
			faultyVersions.add(faultyVersion);
		}
		return faultyVersions;
	}
	/**
	 * Returns a fault by random which is not already contained in the passed set.
	 */
	private PitMutation getRandomUniqueFault(Set<PitMutation> alreadySelectedFaults, List<PitMutation> pitFaults) {
		PitMutation fault = pitFaults.get(getRandomFaultIndex(pitFaults));
		while (alreadySelectedFaults.contains(fault)) {
			fault = pitFaults.get(getRandomFaultIndex(pitFaults));
		}
		return fault;
	}
	private int getRandomFaultIndex(List<PitMutation> pitFaults) {
		return (int) (Math.random() * pitFaults.size());
	}
}
