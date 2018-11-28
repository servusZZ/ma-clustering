package priorization.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hac.main.HierarchicalAgglomerativeClustering;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import prioritization.strategies.OptimalPrioritization;
import prioritization.strategies.RandomPrioritization;

public class PrioritizationStrategyFactory {

	public static List<PrioritizationStrategyBase> createStrategies(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults){
		List<PrioritizationStrategyBase> strategies = new ArrayList<PrioritizationStrategyBase>();
		strategies.add(new HierarchicalAgglomerativeClustering(failures, passedTCs, faults));
		strategies.add(new RandomPrioritization(failures, passedTCs, faults));
		strategies.add(new OptimalPrioritization(failures, passedTCs, faults));
		return strategies;
	}
}
