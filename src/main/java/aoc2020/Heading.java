package aoc2020;

enum Heading {

	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0),

	UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

	int dx;
	int dy;

	Heading(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	Heading turn(boolean right) {
		return right ? right() : left();
	}

	Heading left() {
		return switch (this) {
		case NORTH -> WEST;
		case WEST -> SOUTH;
		case SOUTH -> EAST;
		case EAST -> NORTH;

		case UP -> LEFT;
		case LEFT -> DOWN;
		case DOWN -> RIGHT;
		case RIGHT -> UP;
		};
	}

	Heading right() {
		return switch (this) {
		case NORTH -> EAST;
		case EAST -> SOUTH;
		case SOUTH -> WEST;
		case WEST -> NORTH;

		case UP -> RIGHT;
		case RIGHT -> DOWN;
		case DOWN -> LEFT;
		case LEFT -> UP;
		};
	}
}