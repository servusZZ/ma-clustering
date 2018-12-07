package prioritization.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import priorization.main.PrioritizationStrategyBase;

public class PackageNameClusteringPrioritization  extends PrioritizationStrategyBase{
	
	public PackageNameClusteringPrioritization(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.strategyName = "PackageName Clustering";
	}

	@Override
	public void prioritizeFailures() {
		Map<String, List<TestCase>> packageNameClusterMapping = new HashMap<String, List<TestCase>>();
		for (TestCase failure: failures) {
			List<TestCase> cluster = packageNameClusterMapping.get(getPackageName(failure.name));
			if (cluster == null) {
				cluster = new ArrayList<TestCase>();
				packageNameClusterMapping.put(getPackageName(failure.name), cluster);
			}
			cluster.add(failure);
		}
		List<List<TestCase>> clusters = new ArrayList<List<TestCase>>(packageNameClusterMapping.values());
		Collections.sort(clusters, new Comparator<List<TestCase>>(){
			public int compare(List<TestCase> cluster1, List<TestCase> cluster2) {
				return Integer.valueOf(cluster1.size()).compareTo(Integer.valueOf(cluster2.size()));
			}
		});
		prioritizedFailures = new ArrayList<TestCase>();
		for (List<TestCase> cluster: clusters) {
			prioritizedFailures.add(cluster.get(0));
		}
	}
	
	/**
	 * Returns the package name of a test case name.
	 */
	protected String getPackageName(String tcName) {
		String className = tcName.substring(0, tcName.lastIndexOf('.'));
		if (className.lastIndexOf('.') == -1) {
			return className;
		}
		return className.substring(0, className.lastIndexOf('.'));
	}

}