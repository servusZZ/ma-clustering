package hac.sbfl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class OverlapConfiguration1Test {

	@Test
	void testComputeMostSuspiciousSet() {
		Map<Integer, Double> methodDStarSusp = fillDStarSuspMap21Methods();
		SBFLConfiguration sbflConfig = new OverlapConfiguration1();
		Set<Integer> mostSuspSet = sbflConfig.computeMostSuspiciousSet(methodDStarSusp);
		assertTrue(isMostSuspSet21Methods(mostSuspSet));
	}
	
	Map<Integer, Double> fillDStarSuspMap21Methods(){
		Map<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
		methodDStarSusp.put(0, 0.2);
		methodDStarSusp.put(1, 0.2);
		methodDStarSusp.put(2, 0.2);
		methodDStarSusp.put(3, 0.2);
		methodDStarSusp.put(4, 0.2);
		methodDStarSusp.put(5, 0.1);
		methodDStarSusp.put(6, 0.1);
		methodDStarSusp.put(7, 0.1);
		methodDStarSusp.put(8, 0.1);
		methodDStarSusp.put(9, 0.1);
		methodDStarSusp.put(10, 0.689);
		methodDStarSusp.put(20, 0.689);
		methodDStarSusp.put(11, 1.3);
		methodDStarSusp.put(12, 1.3);
		methodDStarSusp.put(13, 1.3);
		methodDStarSusp.put(14, 1.3);
		methodDStarSusp.put(15, 0.0);
		methodDStarSusp.put(16, 2.0);	// these 4 entries should be contained
		methodDStarSusp.put(17, 2.0);	//	x
		methodDStarSusp.put(18, 10.0);	//	x
		methodDStarSusp.put(19, 20.0);	//	x
		return methodDStarSusp;
	}
	boolean isMostSuspSet21Methods(Set<Integer> mostSuspSet) {
		if (mostSuspSet.size() != 4) {
			return false;
		}
		if (mostSuspSet.contains(16) && mostSuspSet.contains(17) &&
				mostSuspSet.contains(18) && mostSuspSet.contains(19)) {
			return true;
		}
		return false;
	}

}
