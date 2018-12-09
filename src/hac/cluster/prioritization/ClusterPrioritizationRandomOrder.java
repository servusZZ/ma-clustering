package hac.cluster.prioritization;

import java.util.Collections;
import java.util.List;

import hac.data_objects.Cluster;
import hac.sbfl.SBFLConfiguration;

public class ClusterPrioritizationRandomOrder extends ClusterPrioritizationBase{

	public ClusterPrioritizationRandomOrder(SBFLConfiguration sbflConfig) {
		super(sbflConfig);
	}

	@Override
	public List<Cluster> prioritizeClusters(List<Cluster> clusters) {
		Collections.shuffle(clusters);
		return clusters;
	}

}
