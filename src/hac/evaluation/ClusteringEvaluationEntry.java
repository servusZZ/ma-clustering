package hac.evaluation;

import org.apache.commons.lang3.StringUtils;

public class ClusteringEvaluationEntry {
	private int clustersCount;
	/**	the number of clusters for which the representative selection was successful */
	private int representativeSelectionSuccessful;
	/**	the number of clusters for which the representative selection wasn't successful */
	private int representativeSelectionFailed;
	/**	contains the class name of the representative selection */
	private String representativeSelectionStrategy;
	/**	contains the class name of the distance measurement */
	private String dissimilarityMeasure;
	private double purity;
	private double precision;
	private double recall;
	private double f1score;
	private double faultEntropy;
	
	private int comparisonTP;
	private int comparisonTN;
	private int comparisonFP;
	private int comparisonFN;
	
	public ClusteringEvaluationEntry(String dissimilarityMeasure, int clustersCount,
			String representativeSelectionStrategy, int representativeSelectionSuccessful,
			int representativeSelectionFailed, double purity, double precision,
			double recall, double f1score, double faultEntropy) {
		this.dissimilarityMeasure = dissimilarityMeasure;
		this.clustersCount = clustersCount;
		this.representativeSelectionStrategy = representativeSelectionStrategy;
		this.representativeSelectionSuccessful = representativeSelectionSuccessful;
		this.representativeSelectionFailed = representativeSelectionFailed;
		this.purity = purity;
		this.precision = precision;
		this.recall = recall;
		this.f1score = f1score;
		this.faultEntropy = faultEntropy;
	}
	public String getValues() {
		return dissimilarityMeasure + ";" + clustersCount + ";" + representativeSelectionStrategy +
				 ";" + representativeSelectionSuccessful + ";" + representativeSelectionFailed +
				 ";" + getGermanRepresentation(purity) +
				 ";" + getGermanRepresentation(precision) +
				 ";" + getGermanRepresentation(recall) +
				 ";" + getGermanRepresentation(f1score) +
				 ";" + getGermanRepresentation(faultEntropy) +
				 ";" + comparisonTP +
				 ";" + comparisonTN +
				 ";" + comparisonFP +
				 ";" + comparisonFN;
	}
	private String getGermanRepresentation(double val) {
		return StringUtils.replaceChars("" + val, '.', ',');
	}
	public void setComparisonValues(ClusterComparingEvaluation cce) {
		comparisonFN = cce.getFN();
		comparisonFP = cce.getFP();
		comparisonTN = cce.getTN();
		comparisonTP = cce.getTP();
	}
}
