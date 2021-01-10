package common;

import java.util.function.Function;

import io.vavr.collection.List;

public record Pose(Direction heading, Point pos) {

	public Pose(Direction h, int x, int y) {
		this(h, new Point(x, y));
	}
	
	public Pose(Direction heading, Point pos) {
		this.heading = heading;
		this.pos = pos;
	}

	public Pose(int x, int y) {
		this(Direction.NORTH, new Point(x, y));
	}

	public Pose() {
		this(Direction.NORTH, new Point(0, 0));
	}

	public Pose turn(boolean right) {
		return new Pose(heading.turn(right), pos);
	}

	public Pose turnRight() {
		return new Pose(heading.right(), pos);
	}

	public Pose turnLeft() {
		return new Pose(heading.left(), pos);
	}
	
	public Pose left() {
		return new Pose(heading, pos.translate(heading.left()));
	}

	public Pose right() {
		return new Pose(heading, pos.translate(heading.right()));
	}

	public Pose ahead() {
		return ahead(1);
	}

	public Pose ahead(int steps) {
		return new Pose(heading, pos.translate(heading, steps));
	}

	public List<Pose> aheads(int steps) {
		return (steps == 0) ? List.empty() : List.of(ahead()).appendAll(ahead().aheads(steps-1));
	}

	public Pose behind() {
		return new Pose(heading, pos.translate(heading, -1));
	}
	
	public boolean equalsPos(Pose p) {
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
