package aoc2023;

import common.Direction;
import common.Point;
import io.vavr.collection.List;

public record Mpose(Direction heading, Point pos, int steps) {

    public Mpose(Direction h, int x, int y) {
        this(h, new Point(x, y), 0);
    }

    public Mpose(Direction heading, Point pos, int steps) {
        this.heading = heading;
        this.pos = pos;
        this.steps = steps;
    }

    public Mpose turn(boolean right) {
        return new Mpose(heading.turn(right), pos, 0);
    }

    public Mpose turnRight() {
        return new Mpose(heading.right(), pos, 0);
    }

    public Mpose turnLeft() {
        return new Mpose(heading.left(), pos, 0);
    }
    
    public List<Mpose> next() {
        if (steps == 3)
            return List.of(turnLeft().ahead(), turnRight().ahead());
        else
            return List.of(ahead(), turnLeft().ahead(), turnRight().ahead());
    }

    public Mpose ahead() {
        return new Mpose(heading, pos.translate(heading, 1), steps+1);
    }

    public boolean equalsPos(Mpose p) {
        return pos.equals(p.pos);
    }

    public String toString() {
        return String.format("%s-%d@%s", heading, steps, pos);
    }


}
