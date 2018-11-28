package hac.sbfl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import faulty_project.globals.FaultyProjectGlobals;

class OverlapConfiguration1Test {

	@Test
	void testComputeMostSuspiciousSet21Methods() {
		FaultyProjectGlobals.methodsCount = 21;
		Map<Integer, Double> methodDStarSusp = fillDStarSuspMap21Methods();
		SBFLConfiguration sbflConfig = new OverlapConfiguration1();
		Set<Integer> mostSuspSet = sbflConfig.computeMostSuspiciousSet(methodDStarSusp);
		assertTrue(isMostSuspSet21Methods(mostSuspSet));
	}
	@Test
	void testComputeMostSuspiciousSetEqualHighsteNotSuspElement() {
		FaultyProjectGlobals.methodsCount = 25;
		Map<Integer, Double> methodDStarSusp = fillDStarSuspMap21Methods();
		methodDStarSusp.put(21, 2.0);
		methodDStarSusp.put(22, 2.0);
		methodDStarSusp.put(23, 2.0);
		methodDStarSusp.put(24, 2.0);
		SBFLConfiguration sbflConfig = new OverlapConfiguration1();
		Set<Integer> mostSuspSet = sbflConfig.computeMostSuspiciousSet(methodDStarSusp);
		assertTrue(isMostSuspSet24Methods(mostSuspSet));
	}
	@Test
	void testComputeMostSuspiciousSetRelThreshold() {
		FaultyProjectGlobals.methodsCount = 50;
		Map<Integer, Double> methodDStarSusp = fillDStarSuspMap50Methods();
		SBFLConfiguration sbflConfig = new OverlapConfiguration1();
		Set<Integer> mostSuspSet = sbflConfig.computeMostSuspiciousSet(methodDStarSusp);
		assertTrue(isMostSuspSet50Methods(mostSuspSet));
	}
	@Test
	void testComputeMostSuspiciousSetMaxThreshold() {
		FaultyProjectGlobals.methodsCount = 150;
		Map<Integer, Double> methodDStarSusp = fillDStarSuspMap50Methods();
		methodDStarSusp.put(50, SBFLConfiguration.MAX_SUSP_VALUE);
		methodDStarSusp.put(51, SBFLConfiguration.MAX_SUSP_VALUE);
		methodDStarSusp.put(52, SBFLConfiguration.MAX_SUSP_VALUE);
		methodDStarSusp.put(53, SBFLConfiguration.MAX_SUSP_VALUE);
		SBFLConfiguration sbflConfig = new OverlapConfiguration1();
		Set<Integer> mostSuspSet = sbflConfig.computeMostSuspiciousSet(methodDStarSusp);
		assertTrue(isMostSuspSet150Methods(mostSuspSet));
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
	Map<Integer, Double> fillDStarSuspMap50Methods(){
		Map<Integer, Double> methodDStarSusp = new HashMap<Integer, Double>();
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
		methodDStarSusp.put(11, 1.3);
		methodDStarSusp.put(12, 1.3);
		methodDStarSusp.put(13, 1.3);
		methodDStarSusp.put(14, 1.3);
		methodDStarSusp.put(15, 0.0);
		methodDStarSusp.put(16, 2.0);
		methodDStarSusp.put(17, 2.0);
		methodDStarSusp.put(18, 10.0);	// x
		methodDStarSusp.put(19, 20.0);	// x
		methodDStarSusp.put(20, 0.689);

		methodDStarSusp.put(21, 0.2);
		methodDStarSusp.put(22, 0.2);
		methodDStarSusp.put(23, 0.2);
		methodDStarSusp.put(24, 0.2);
		methodDStarSusp.put(25, 0.1);
		methodDStarSusp.put(26, 0.1);
		methodDStarSusp.put(27, 0.1);
		methodDStarSusp.put(28, 0.1);
		methodDStarSusp.put(29, 0.1);
		methodDStarSusp.put(30, 0.689);
		methodDStarSusp.put(31, 1.3);
		methodDStarSusp.put(32, 1.3);
		methodDStarSusp.put(33, 1.3);
		methodDStarSusp.put(34, 1.3);
		methodDStarSusp.put(35, 0.0);
		methodDStarSusp.put(36, 2.0);
		methodDStarSusp.put(37, 2.0);
		methodDStarSusp.put(38, 10.0);	// x
		methodDStarSusp.put(39, 20.0);	// x
		methodDStarSusp.put(40, 20.0);	// x
		
		methodDStarSusp.put(41, 0.2);
		methodDStarSusp.put(42, 0.2);
		methodDStarSusp.put(43, 0.2);
		methodDStarSusp.put(44, 0.2);
		methodDStarSusp.put(45, 0.1);
		methodDStarSusp.put(46, 0.1);
		methodDStarSusp.put(47, 0.1);
		methodDStarSusp.put(48, SBFLConfiguration.MAX_SUSP_VALUE + 1); // x
		methodDStarSusp.put(49, SBFLConfiguration.MAX_SUSP_VALUE + 2); // x
		methodDStarSusp.put(0, SBFLConfiguration.MAX_SUSP_VALUE);	// x
		
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
	boolean isMostSuspSet24Methods(Set<Integer> mostSuspSet) {
		if (mostSuspSet.size() != 8) {
			return false;
		}
		if (mostSuspSet.contains(16) && mostSuspSet.contains(17) &&
				mostSuspSet.contains(18) && mostSuspSet.contains(19) &&
				mostSuspSet.contains(21) && mostSuspSet.contains(22) &&
				mostSuspSet.contains(23) && mostSuspSet.contains(24)) {
			return true;
		}
		return false;
	}
	boolean isMostSuspSet50Methods(Set<Integer> mostSuspSet) {
		if (mostSuspSet.size() != 8) {
			return false;
		}
		if (mostSuspSet.contains(18) && mostSuspSet.contains(19) &&
				mostSuspSet.contains(40) && mostSuspSet.contains(39) &&
				mostSuspSet.contains(38) && mostSuspSet.contains(0) &&
				mostSuspSet.contains(48) && mostSuspSet.contains(49)) {
			return true;
		}
		return false;
	}
	boolean isMostSuspSet150Methods(Set<Integer> mostSuspSet) {
		if (mostSuspSet.size() != 12) {
			return false;
		}
		if (mostSuspSet.contains(18) && mostSuspSet.contains(19) &&
				mostSuspSet.contains(40) && mostSuspSet.contains(39) &&
				mostSuspSet.contains(38) && mostSuspSet.contains(0) &&
				mostSuspSet.contains(48) && mostSuspSet.contains(49) &&
				mostSuspSet.contains(50) && mostSuspSet.contains(51) &&
				mostSuspSet.contains(52) && mostSuspSet.contains(53)) {
			return true;
		}
		return false;
	}
}
