package aoc2020;

import java.util.function.Function;

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
		return new Pose(heading.right(), pos);
	}

	Pose turnLeft() {
		return new Pose(heading.left(), pos);
	}
	
	
	Pose left() {
		return new Pose(heading, pos.translate(heading.left()));
	}

	Pose right() {
		return new Pose(heading, pos.translate(heading.right()));
	}

	Pose ahead() {
		return ahead(1);
	}

	Pose ahead(int steps) {
		return new Pose(heading, pos.translate(heading, steps));
	}

	List<Pose> aheads(int steps) {
		return (steps == 0) ? List.empty() : List.of(ahead()).appendAll(ahead().aheads(steps-1));
	}

	Pose behind() {
		return new Pose(heading, pos.translate(heading, -1));
	}
	
	boolean equalsPos(Pose p) {
		return pos.equals(p.pos);
	}

	public String toString() {
		return String.format("%s@%s", heading, pos);
	}

	public Pose go(Point newp) {
		return new Pose(heading, newp);
	}

    public Pose repeat(int n, Function<Pose, Pose> f) {
    	var p = this;
    	for (int i=0; i<n; ++i) {
    		p = f.apply(p);
    	}
    	return p;
    }

}
