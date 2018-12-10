package hac.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.AverageLinkage;
import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.experiment.Experiment;
import hac.data_objects.Cluster;
import hac.evaluation.ClusteringEvaluation;
import hac.evaluation.KNNToCenterSelection;
import hac.experiment.custom.AverageCenterCalculation;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.experiment.custom.FailureClusteringExperiment;
import hac.experiment.custom.ICenterCalculation;
import hac.experiment.custom.JaccardDistance;
import prioritization.data_objects.Fault;
import prioritization.data_objects.TestCase;
import prioritization.strategies.HACPrioritizationBase;

public class HierarchicalAgglomerativeClustering extends HACPrioritizationBase{
	
	
	public HierarchicalAgglomerativeClustering(TestCase[] failures,
			TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
	}
	private Dendrogram performHAC(CustomDissimilarityMeasure dissimilarityMeasure) {
		Experiment experiment = new FailureClusteringExperiment(failures);
		AgglomerationMethod agglomerationMethod = new AverageLinkage();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		return dendrogramBuilder.getDendrogram();
	}
	@Override
	public void prioritizeFailures() {
		ICenterCalculation centerCalc = new AverageCenterCalculation();
		CustomDissimilarityMeasure dissimilarityMeasure = new JaccardDistance(centerCalc);
		Dendrogram dendrogram = performHAC(dissimilarityMeasure);
		
		ClusterBuilder cb = new ClusterBuilder(dendrogram.getRoot(), passedTCs, failures, sbflConfig);
		List<Cluster> clusters = cb.getClustersOfCuttingLevel();
		Refinement refinement = new Refinement(cb.getPassedTCsCluster(), sbflConfig);
		clusters = refinement.refineClusters(clusters);
		
		ClusteringEvaluation clusteringEvaluation = new ClusteringEvaluation(dissimilarityMeasure, new KNNToCenterSelection(), clusters, faults);
		clusteringEvaluation.evaluateClustering();
		//EDIT: _FF, maybe only calculate f1-score, precision, recall and fault entropy
		//		if only a single fault for each failure is allowed.
		clusteringMetrics = clusteringEvaluation.getClusteringMetrics();
		clusteringMetrics.setComparisonValues(sbflConfig.getClusterComparisonEvaluation());
		clusters = clusterPrioritization.prioritizeClusters(clusters);
		prioritizedFailures = getRepresentativesOfClusters(clusters);
	}
	/**
	 * Returns the representatives of the passed prioritized clusters.
	 * The representatives of the clusters must have been already computed before.
	 */
	private List<TestCase> getRepresentativesOfClusters(List<Cluster> clusters){
		List<TestCase> prioritizedFailures = new ArrayList<TestCase>();
		for (Cluster c: clusters) {
			prioritizedFailures.add(c.getRepresentative());
		}
		return prioritizedFailures;
	}
}
