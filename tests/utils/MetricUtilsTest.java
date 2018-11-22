package utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class MetricUtilsTest {

	@Test
	void testOverlapSimilarity() {
		// 3 of 5
		double overlapSim = MetricUtils.overlapSimilarity(new HashSet<Integer>(Arrays.asList(1,2,3,4,5)),
				new HashSet<Integer>(Arrays.asList(1,2,3,6,7)));
		assertEquals(0.6, overlapSim);
		
		// all contained
		overlapSim = MetricUtils.overlapSimilarity(new HashSet<Integer>(Arrays.asList(1,2,3,4)),
				new HashSet<Integer>(Arrays.asList(4,2,3,6,7,8,9,10,586,324,1)));
		assertEquals(1, overlapSim);
		
		// not similar
		overlapSim = MetricUtils.overlapSimilarity(new HashSet<Integer>(Arrays.asList(1,2,3,4)),
				new HashSet<Integer>(Arrays.asList(6,7,8,9,10,586,324)));
		assertEquals(0, overlapSim);
		
		// different sizes, 3 of 4
		overlapSim = MetricUtils.overlapSimilarity(new HashSet<Integer>(Arrays.asList(1,2,3,4)),
				new HashSet<Integer>(Arrays.asList(4,2,3,6,7,8,9,10,586,324,80,81,82,83,84)));
		assertEquals(0.75, overlapSim);
	}

}
