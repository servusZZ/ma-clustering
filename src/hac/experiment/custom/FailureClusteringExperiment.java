package hac.experiment.custom;

import ch.usi.inf.sape.hac.experiment.Experiment;
import data_objects.TestCase;

public class FailureClusteringExperiment implements Experiment {
	private TestCase[] failures;
	
	public FailureClusteringExperiment(TestCase[] failures) {
		this.failures = failures;
	}

	public int getNumberOfObservations() {
		return failures.length;
	}
	
	public Object getObservation(int i) {
		return failures[i];
	}

}
