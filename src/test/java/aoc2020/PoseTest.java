package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import common.Direction;
import common.Pose;

public class PoseTest {

	@Test
	public void testPoseOps() {
		
		assertEquals(new Pose(Direction.NORTH, -1, 0), new Pose(Direction.NORTH, 0, 0).left());

		assertEquals(new Pose(Direction.WEST, 1, 0), new Pose(Direction.WEST, 1, 1).right());
		
		assertEquals(new Pose(Direction.NORTH, 0, 1), new Pose(Direction.NORTH, 0, 0).behind());
		
		assertEquals(new Pose(Direction.SOUTH, 0, 5), new Pose(Direction.SOUTH, 0, 0).ahead(5));
	}
	
}
