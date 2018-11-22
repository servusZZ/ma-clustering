package evaluation;

import org.apache.commons.lang3.StringUtils;

public class ProjectEvaluationEntry {
	private String id;
	private String projectName;
	private int faultsCount;
	private int failuresCount;
	private int passedTCsCount;
	/**	all relevant tests for D* calculation */
	private int testsCount;
	// which kind of tests are considered, e.g. coverage > 20 Methods or coverage > 100 Methods
	private int minTestSize;
	private double ddu;
	//TODO: define project types, this refers to the kind
			//	how faults are distributed:
			//		e.g. multiple faults in same class/ package
			//		failures that are caused by multiple faults contained?	
	private String faultDistributionType;
	
	public ProjectEvaluationEntry(int id, String projectName, int faultsCount,
			int failuresCount, int passedTCsCount) {
		this.id = projectName + "-" + id;
		this.projectName = projectName;
		this.faultsCount = faultsCount;
		this.failuresCount = failuresCount;
		this.passedTCsCount = passedTCsCount;
		this.testsCount = failuresCount + passedTCsCount;
		ddu = Double.NaN;
		this.minTestSize = 20;
		this.faultDistributionType = "NA";
	}
	public String getHeader() {
		return "FaultyVersion-ID;ProjectName;#Faults;#Failuers;#PassedTCs;#Tests;DDU;MinTestSize;FaultDistribution";
	}
	public String getValues() {
		return id + ";" + projectName + ";" + faultsCount + ";" + failuresCount +
				 ";" + passedTCsCount + ";" + testsCount +
				 ";" + getGermanRepresentation(ddu) +
				 ";" + minTestSize + ";" + faultDistributionType;
	}
	private String getGermanRepresentation(double val) {
		return StringUtils.replaceChars("" + val, '.', ',');
	}
}
