package aoc2020;

import io.vavr.collection.List;

public record Pose(Heading heading, Point pos) {

	public Pose(Heading h, int x, int y) {
		this(h, new Point(x, y));
	}

	public Pose(int x, int y) {
		this(Heading.NORTH, new Point(x, y));
	}

	public Pose() {
		this(Heading.NORTH, new Point(0, 0));
	}

	Pose turn(boolean right) {
		return new Pose(heading.turn(right), pos);
	}

	Pose turnRight() {
		return turn(true);
	}

	Pose turnLeft() {
		return turn(false);
	}

	Pose left() {
		return new Pose(heading, pos.translate(heading.dy, -heading.dx));
	}

	Pose right() {
		return new Pose(heading, pos.translate(-heading.dy, heading.dx));
	}

	Pose ahead() {
		return ahead(1);
	}

	Pose ahead(int steps) {
		return new Pose(heading, pos.translate(steps * heading.dx, steps * heading.dy));
	}

	List<Pose> aheads(int steps) {
		return (steps == 0) ? List.empty() : List.of(ahead()).appendAll(ahead().aheads(steps-1));
	}

	Pose behind() {
		return new Pose(heading, pos.translate(-heading.dx, -heading.dy));
	}
	
	boolean equalsPos(Pose p) {
		return pos.equals(p.pos);
	}

	public String toString() {
		return String.format("%s@%s", heading, pos);
	}

}
