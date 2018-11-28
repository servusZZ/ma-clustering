package prioritization.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import priorization.main.PrioritizationStrategyBase;

/**
 * Prioritize all failures by a random order.
 */
public class RandomPrioritization extends PrioritizationStrategyBase{

	public RandomPrioritization(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.strategyName = "RANDOM";
	}

	@Override
	public void prioritizeFailures() {
		initPrioritizedFailures();
		Collections.shuffle(prioritizedFailures);
	}
	
	private void initPrioritizedFailures() {
		prioritizedFailures = new ArrayList<TestCase>();
		for (int i = 0; i < failures.length; i++) {
			prioritizedFailures.add(failures[i]);
		}
	}

}
