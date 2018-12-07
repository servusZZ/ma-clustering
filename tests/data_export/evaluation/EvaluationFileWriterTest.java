package data_export.evaluation;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import evaluation.EvaluationEntry;
import hac.evaluation.ClusteringEvaluationEntry;
import prioritization.evaluation.ProjectEvaluationEntry;

class EvaluationFileWriterTest {

	@Test
	void testWriteAndAppendToEvaluationFile() throws IOException {
		EvaluationFileWriter writer = new EvaluationFileWriter("C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\test\\", "prioritization-evaluation-test.csv");
		ClusteringEvaluationEntry clusterMetrics = new ClusteringEvaluationEntry("jaccardDistance",5,"KNNToCenterSelection",4, 1, 0.95, 0.9, 0.5, 0.83, (double)19/27);
		ProjectEvaluationEntry projectMetrics = new ProjectEvaluationEntry(1, "biojavaTest", 5, 38, 1, 120,
				50, 20, 30, 50, 0, 1);
		EvaluationEntry evaluationEntry = new EvaluationEntry("HAC", 3, 25, 1, 3, 3, projectMetrics, clusterMetrics);
		writer.writeEvaluationEntry(evaluationEntry);
		writer.writeEvaluationEntry(evaluationEntry);
		evaluationEntry = new EvaluationEntry("RANDOM", 2, 10, 1, 3, 3, projectMetrics, null);
		writer.writeEvaluationEntry(evaluationEntry);
	}
	@Test
	void testWriteNewFileWithNullClusterMetrics() throws IOException {
		EvaluationFileWriter writer = new EvaluationFileWriter("C:\\study\\workspace_master-thesis-java\\ma-clustering\\output\\test\\", "prioritization-evaluation-test2.csv");
		ProjectEvaluationEntry projectMetrics = new ProjectEvaluationEntry(15, "biojavaTest", 10, 90, 0, 20,
				50, 20, 20.5, 35.7, 0, 0);
		EvaluationEntry evaluationEntry = new EvaluationEntry("RANDOM", 3, 25, 0, 3, 3, projectMetrics, null);
		
		writer.writeEvaluationEntry(evaluationEntry);
		
		ClusteringEvaluationEntry clusterMetrics = new ClusteringEvaluationEntry("jaccardDistance",5,"KNNToCenterSelection",4, 1, (double)17/137,(double)2/3, 0.5, (double)17/18, 2.13);
		evaluationEntry = new EvaluationEntry("HAC", 3, 25, 0, 3, 5, projectMetrics, clusterMetrics);
		
		writer.writeEvaluationEntry(evaluationEntry);
	}
}
