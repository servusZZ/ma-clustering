package prioritization.strategies;

import java.util.Set;

import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;

public class ClassNameClusteringPrioritization extends PackageNameClusteringPrioritization{
	public ClassNameClusteringPrioritization(TestCase[] failures, TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.strategyName = "ClassName Clustering";
	}
	
	/**
	 * Returns the class name of a test case name. The class name is defined as the string before the last dot
	 */
	@Override
	protected String getPackageName(String tcName) {
		return tcName.substring(0, tcName.lastIndexOf('.'));
	}

}
