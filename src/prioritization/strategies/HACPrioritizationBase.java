package prioritization.strategies;

import java.util.Set;

import hac.cluster.prioritization.ClusterPrioritizationBase;
import hac.sbfl.SBFLConfiguration;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import priorization.main.PrioritizationStrategyBase;

public abstract class HACPrioritizationBase extends PrioritizationStrategyBase{
	protected SBFLConfiguration sbflConfig;
	protected ClusterPrioritizationBase clusterPrioritization;
	
	public HACPrioritizationBase(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
	}

	public void setSbflConfig(SBFLConfiguration sbflConfig) {
		this.sbflConfig = sbflConfig;
	}
	public void setClusterPrioritization(ClusterPrioritizationBase clusterPrioritization) {
		this.clusterPrioritization = clusterPrioritization;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
}
