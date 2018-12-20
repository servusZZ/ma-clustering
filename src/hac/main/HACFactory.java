package hac.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import hac.cluster.prioritization.ClusterPrioritizationDissimilarGreatestFirst;
import hac.cluster.prioritization.ClusterPrioritizationGreatestFirst;
import hac.cluster.prioritization.ClusterPrioritizationRandomOrder;
import hac.sbfl.JaccardSBFLConfiguration;
import hac.sbfl.OverlapConfiguration;
import hac.sbfl.SBFLConfiguration;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import prioritization.strategies.HACPrioritizationBase;

public class HACFactory {
	public static List<HACPrioritizationBase> createHACStrategies(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults){
		List<HACPrioritizationBase> strategies = new ArrayList<HACPrioritizationBase>();
		
		strategies.addAll(createStrategiesWithOverlapConfig(failures, passedTCs, faults));
		strategies.addAll(createStrategiesWithJaccardConfig(failures, passedTCs, faults));
		
		/**	 Varianten bilden nach: MetricBerechnung * Thresholds
		 * 		+2 pro MetricBerechnung einmal ohne dissimilarityGreatestFirst, stattdessen Greatest und Random
		 * 		(+ pro MetricBerechnung einmal ohne Refinement?)
		 * 		RepSelection scheint gut zu funktionieren --> daher nicht variieren bzw. Fälle in denen es nicht zu 100% funktioniert vorerst manuell inspizieren
		 * 
		 * MetricBerechnung = {OverlapConfig, JaccardDistance}
		 * Parameter = {medium, biggerSuspSet, soft} evtl noch eine "verySoft" o.Ä. hinzufügen
		 * 
		 * 2 * 3 + 2 + 2 = 10 Varianten
		 */
		return strategies;
	}
	
	private static List<HACPrioritizationBase> createStrategiesWithJaccardConfig(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults){
		List<HACPrioritizationBase> jaccardStrategies = new ArrayList<HACPrioritizationBase>();
		
		// medium, 
		HACPrioritizationBase strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		SBFLConfiguration sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		/**	a greater value means that 2 clusters are similar.<br>
		 * 3 of 5 overlap elements are not considered as similar, 6 of 9 are similar */
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard medium");
		jaccardStrategies.add(strategy);
		
		// soft, clusters are more easier seen as similar
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.30;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard soft");
		jaccardStrategies.add(strategy);
		
		// very soft, clusters are more easier seen as similar
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.20;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard very soft");
		jaccardStrategies.add(strategy);
		
		// bigger suspicious set, no max value
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = Integer.MAX_VALUE;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard SuspSetNoUpperbound");
		jaccardStrategies.add(strategy);
		
		// bigger suspicious set, no max value
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = Integer.MAX_VALUE;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.30;
		sbflConfig.SIMILARITY_THRESHOLD = 0.30;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard SuspSetNoUpperbound biggerSuspSet soft");
		jaccardStrategies.add(strategy);
		
		// medium ordering of clusters only by size but not by dissimilarity
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Jaccard medium GreatestFirst");
		jaccardStrategies.add(strategy);
		
		// medium ordering of clusters by random
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new JaccardSBFLConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationRandomOrder(sbflConfig));
		strategy.setStrategyName("HAC Jaccard medium RandomClusterOrder");
		jaccardStrategies.add(strategy);
		return jaccardStrategies;
	}
	private static List<HACPrioritizationBase> createStrategiesWithOverlapConfig(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults){
		List<HACPrioritizationBase> overlapStrategies = new ArrayList<HACPrioritizationBase>();
		
		// medium
		HACPrioritizationBase strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		SBFLConfiguration sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		/**	a greater value means that 2 clusters are similar.<br>
		 * 3 of 5 overlap elements are not considered as similar, 6 of 9 are similar */
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap medium");
		overlapStrategies.add(strategy);
		
		// soft, clusters are more easier seen as similar
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.30;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap soft");
		overlapStrategies.add(strategy);
		
		// soft, clusters are more easier seen as similar
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.20;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap very soft");
		overlapStrategies.add(strategy);
		
		// bigger suspicious set, no max value
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = Integer.MAX_VALUE;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.20;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap SuspSetNoUpperbound");
		overlapStrategies.add(strategy);
		
		// bigger suspicious set, no max value
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = Integer.MAX_VALUE;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.30;
		sbflConfig.SIMILARITY_THRESHOLD = 0.30;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationDissimilarGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap SuspSetNoUpperbound biggerSuspSet soft");
		overlapStrategies.add(strategy);
		
		// medium ordering of clusters only by size but not by dissimilarity
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.2;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationGreatestFirst(sbflConfig));
		strategy.setStrategyName("HAC Overlap medium GreatestFirst");
		overlapStrategies.add(strategy);
		
		// medium ordering of clusters by random
		strategy = new HierarchicalAgglomerativeClustering(failures, passedTCs, faults);
		sbflConfig = new OverlapConfiguration();
		sbflConfig.MOST_SUSP_MAX_COUNT = 12;
		sbflConfig.MOST_SUSP_MIN_COUNT = 4;
		sbflConfig.MOST_SUSP_THRESHOLD = 0.2;
		sbflConfig.SIMILARITY_THRESHOLD = 0.60;
		strategy.setSbflConfig(sbflConfig);
		strategy.setClusterPrioritization(new ClusterPrioritizationRandomOrder(sbflConfig));
		strategy.setStrategyName("HAC Overlap medium RandomClusterOrder");
		overlapStrategies.add(strategy);
		return overlapStrategies;
	}
}
