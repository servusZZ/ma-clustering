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
import hac.evaluation.ClusteringEvaluationEntry;
import hac.evaluation.KNNToCenterSelection;
import hac.experiment.custom.AverageCenterCalculation;
import hac.experiment.custom.CustomDissimilarityMeasure;
import hac.experiment.custom.FailureClusteringExperiment;
import hac.experiment.custom.ICenterCalculation;
import hac.experiment.custom.JaccardDistance;
import priorization.main.PrioritizationStrategyBase;
import utils.PrintUtils;

public class HierarchicalAgglomerativeClustering extends PrioritizationStrategyBase{

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
	public List<TestCase> prioritizeFailures() {
		List<TestCase> prioritizedFailures = new ArrayList<TestCase>();
		ICenterCalculation centerCalc = new AverageCenterCalculation();
		CustomDissimilarityMeasure dissimilarityMeasure = new JaccardDistance(centerCalc);
		
		Dendrogram dendrogram = performHAC(dissimilarityMeasure);
		System.out.println("Finished clustering Failures! Dump Dendrogram: ");
		dendrogram.dump();
		
		System.out.println("Determine cutting point of the Failure Tree...");
		ClusterBuilder cb = new ClusterBuilder(dendrogram.getRoot(), passedTCs, failures);
		System.out.println("DEBUG: Created ClusterBuilder and passedTCs");
		List<Cluster> clusters = cb.getClustersOfCuttingLevel();
		System.out.println("Cutting level of the Failure Tree contains " + clusters.size() + " clusters.");
		PrintUtils.dumpClusters(clusters);
		int countClustersBeforeRefinement = clusters.size();
		Refinement refinement = new Refinement(cb.getPassedTCsCluster());
		clusters = refinement.refineClusters(clusters);
		System.out.println("Number of refined clusters: " + clusters.size());
		
		System.out.println("Created " + clusters.size() + " clusters");
		PrintUtils.dumpClusters(clusters);
		if (countClustersBeforeRefinement != clusters.size()) {
			System.out.println("WARNING: The Refinement step changed the number of final clusters!");
			System.out.println("WARNING: This wasn't tested before, so check the result once manually.");
		}
		System.out.println("Evaluate the Clustering...");
		//TODO: ClusterEvaluation überarbeiten
		ClusteringEvaluation em = new ClusteringEvaluation(dissimilarityMeasure, new KNNToCenterSelection(), clusters, faults);
		em.evaluateClustering();
		//this.clusteringMetrics = new ClusteringEvaluationEntry(clusters.size(),
		//		representativeSelectionSuccessful, representativeSelectionFailed, purity, precision, recall, f1score, faultEntropy);
		prioritizedFailures = performPrioritization(clusters);
		return prioritizedFailures;
	}
	
	private List<TestCase> performPrioritization(List<Cluster> clusters){
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
