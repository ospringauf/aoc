package aoc2020;

enum Heading {
	NORTH, EAST, SOUTH, WEST;

	Heading turn(boolean right) {
		if (right)
			return switch (this) {
			case NORTH -> EAST;
			case EAST -> SOUTH;
			case SOUTH -> WEST;
			case WEST -> NORTH;
			};
		return switch (this) {
		case NORTH -> WEST;
		case WEST -> SOUTH;
		case SOUTH -> EAST;
		case EAST -> NORTH;
		};

	}
}