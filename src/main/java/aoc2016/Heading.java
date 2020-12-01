package aoc2016;

enum Heading {
	
	NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    int dx;
    int dy;

    Heading(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }	

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