package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PoseTest {

	@Test
	public void testPoseOps() {
		
		assertEquals(new Pose(Heading.NORTH, -1, 0), new Pose(Heading.NORTH, 0, 0).left());

		assertEquals(new Pose(Heading.WEST, 1, 0), new Pose(Heading.WEST, 1, 1).right());
		
		assertEquals(new Pose(Heading.NORTH, 0, 1), new Pose(Heading.NORTH, 0, 0).behind());
		
		assertEquals(new Pose(Heading.SOUTH, 0, 5), new Pose(Heading.SOUTH, 0, 0).ahead(5));
	}
	
}
