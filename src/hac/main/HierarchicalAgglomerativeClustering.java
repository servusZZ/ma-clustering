package hac.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.AverageLinkage;
import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.Fault;
import data_objects.TestCase;
import hac.evaluation.ClusteringEvaluation;
import hac.evaluation.KNNToCenterSelection;
import hac.experiment.custom.AverageCenterCalculation;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.experiment.custom.FailureClusteringExperiment;
import hac.experiment.custom.ICenterCalculation;
import hac.experiment.custom.JaccardDistance;
import hac.sbfl.OverlapConfiguration1;
import hac.sbfl.SBFLConfiguration;
import priorization.main.PrioritizationStrategyBase;
import utils.PrintUtils;

public class HierarchicalAgglomerativeClustering extends PrioritizationStrategyBase{
	private SBFLConfiguration sbflConfig;
	
	public HierarchicalAgglomerativeClustering(TestCase[] failures,
			TestCase[] passedTCs, Set<Fault> faults) {
		super(failures, passedTCs, faults);
		this.sbflConfig = new OverlapConfiguration1();
		this.strategyName = "HAC";
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
		System.out.println("Finished clustering Failures! Dump Dendrogram: ");
		dendrogram.dump();
		
		System.out.println("Determine cutting point of the Failure Tree...");
		ClusterBuilder cb = new ClusterBuilder(dendrogram.getRoot(), passedTCs, failures, sbflConfig);
		System.out.println("DEBUG: Created ClusterBuilder and passedTCs");
		List<Cluster> clusters = cb.getClustersOfCuttingLevel();
		System.out.println("Cutting level of the Failure Tree contains " + clusters.size() + " clusters.");
		PrintUtils.dumpClusters(clusters);
		int countClustersBeforeRefinement = clusters.size();
		Refinement refinement = new Refinement(cb.getPassedTCsCluster(), sbflConfig);
		clusters = refinement.refineClusters(clusters);
		System.out.println("Number of refined clusters: " + clusters.size());
		
		System.out.println("Created " + clusters.size() + " clusters");
		PrintUtils.dumpClusters(clusters);
		if (countClustersBeforeRefinement != clusters.size()) {
			System.out.println("WARNING: The Refinement step changed the number of final clusters!");
			System.out.println("WARNING: This wasn't tested before, so check the result once manually.");
		}
		System.out.println("Evaluate the Clustering...");
		ClusteringEvaluation clusteringEvaluation = new ClusteringEvaluation(dissimilarityMeasure, new KNNToCenterSelection(), clusters, faults);
		clusteringEvaluation.evaluateClustering();
		clusteringMetrics = clusteringEvaluation.getClusteringMetrics();
		prioritizedFailures = performPrioritization(clusters);
	}
	/**
	 * Clusters and representatives are already known. Sorts the clusters by size to
	 * possibly enhance the #fixedFailures metric.
	 */
	private List<TestCase> performPrioritization(List<Cluster> clusters){
		//TODO: prioritization enhancement
		//			unähnlichste Cluster (nach D* metric) zuerst um zu vermeiden, dass der gleiche Fault gefunden wird
		//			als eigene Strategie aufnehmen: einmal nach größe und einmal nach Ähnlichkeit sortieren
		//		und: berücksichtigen, dass lediglich durch die Cluster nicht alle TC priorisiert werden
		List<TestCase> prioritizedFailures = new ArrayList<TestCase>();
		Collections.sort(clusters);
		System.out.println("DEBUG: Sorted Clusters");
		System.out.println(clusters);
		for (Cluster c: clusters) {
			prioritizedFailures.add(0, c.getRepresentative());
		}
		return prioritizedFailures;
	}
}
