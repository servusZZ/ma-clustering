package prioritization.data_objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class FaultTest {

	@Test
	void testGetClassName() {
		Fault fault = new Fault("org.apache.commons.geometry.euclidean.twod.PolygonsSet:getUnprocessed:(Ljava/util/List;)Lorg/apache/commons/geometry/euclidean/twod/PolygonsSet$ConnectableSegment;:null", new ArrayList<String>());
		assertEquals("org.apache.commons.geometry.euclidean.twod.PolygonsSet", fault.getClassName());
	}
	
	@Test
	void testGetPackageName() {
		Fault fault = new Fault("org.apache.commons.geometry.core.partitioning.AbstractRegion$1:compare:(Lorg/apache/commons/geometry/core/partitioning/SubHyperplane;Lorg/apache/commons/geometry/core/partitioning/SubHyperplane;)I:0", new ArrayList<String>());
		assertEquals("org.apache.commons.geometry.core.partitioning", fault.getPackageName());
	}

}
