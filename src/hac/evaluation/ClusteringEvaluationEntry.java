package hac.evaluation;

import org.apache.commons.lang3.StringUtils;

public class ClusteringEvaluationEntry {
	private int clustersCount;
	/**	the number of clusters for which the representative selection was successful */
	private int representativeSelectionSuccessful;
	/**	the number of clusters for which the representative selection wasn't successful */
	private int representativeSelectionFailed;
	private double purity;
	private double precision;
	private double recall;
	private double f1score;
	private double faultEntropy;
	
	public ClusteringEvaluationEntry(int clustersCount, int representativeSelectionSuccessful,
			int representativeSelectionFailed, double purity, double precision,
			double recall, double f1score, double faultEntropy) {
		this.clustersCount = clustersCount;
		this.representativeSelectionSuccessful = representativeSelectionSuccessful;
		this.representativeSelectionFailed = representativeSelectionFailed;
		this.purity = purity;
		this.precision = precision;
		this.recall = recall;
		this.f1score = f1score;
		this.faultEntropy = faultEntropy;
	}
	public String getValues() {
		return clustersCount + ";" + representativeSelectionSuccessful + ";" + representativeSelectionFailed +
				 ";" + getGermanRepresentation(purity) +
				 ";" + getGermanRepresentation(precision) +
				 ";" + getGermanRepresentation(recall) +
				 ";" + getGermanRepresentation(f1score) +
				 ";" + getGermanRepresentation(faultEntropy);
	}
	private String getGermanRepresentation(double val) {
		return StringUtils.replaceChars("" + val, '.', ',');
	}
}
