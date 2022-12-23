package common;

public enum Direction {

	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0),

	UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0);

	int dx;
	int dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public Direction turn(boolean right) {
		return right ? right() : left();
	}

	public Direction left() {
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
	
	public Direction opposite() {
		return switch (this) {
		case NORTH -> SOUTH;
		case WEST -> EAST;
		case SOUTH -> NORTH;
		case EAST -> WEST;

		case UP -> DOWN;
		case LEFT -> RIGHT;
		case DOWN -> UP;
		case RIGHT -> LEFT;
		};
	}


	public Direction right() {
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
	
	public static Direction parse(String s) {
		return parse(s.trim().charAt(0));
	}
	
	public static Direction parse(Character c) {
		return switch (Character.toUpperCase(c)) {
		case 'N' -> NORTH;
		case 'S' -> SOUTH;
		case 'E' -> EAST;
		case 'W' -> WEST;
		case 'U' -> UP;
		case 'D' -> DOWN;
		case 'L' -> LEFT;
		case 'R' -> RIGHT;
		default -> throw new IllegalArgumentException("Unexpected value: " + c);
		};
	}
	
	public char symbol() {
	    return switch (this) {
        case RIGHT, EAST -> '>';
        case LEFT, WEST -> '<';
        case UP, NORTH -> '^';
        case DOWN, SOUTH -> 'v';
        default -> throw new IllegalArgumentException("Unexpected value: " + this);
        };
	}
}