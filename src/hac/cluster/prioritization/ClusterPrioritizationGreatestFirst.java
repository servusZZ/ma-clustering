package hac.cluster.prioritization;

import java.util.Collections;
import java.util.List;

import hac.data_objects.Cluster;
import hac.sbfl.SBFLConfiguration;

public class ClusterPrioritizationGreatestFirst extends ClusterPrioritizationBase{

	public ClusterPrioritizationGreatestFirst(SBFLConfiguration sbflConfig) {
		super(sbflConfig);
	}

	@Override
	public List<Cluster> prioritizeClusters(List<Cluster> clusters) {
		Collections.sort(clusters, Collections.reverseOrder());
		return clusters;
	}
}
