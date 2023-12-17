package aoc2023;

import common.Direction;
import common.Point;
import io.vavr.collection.List;

public record Xpose(Direction heading, Point pos, int steps) {

    public Xpose(Direction h, int x, int y) {
        this(h, new Point(x, y), 0);
    }

    public Xpose(Direction heading, Point pos, int steps) {
        this.heading = heading;
        this.pos = pos;
        this.steps = steps;
    }

    public Xpose turn(boolean right) {
        return new Xpose(heading.turn(right), pos, 0);
    }

    public Xpose turnRight() {
        return new Xpose(heading.right(), pos, 0);
    }

    public Xpose turnLeft() {
        return new Xpose(heading.left(), pos, 0);
    }
    
    public List<Xpose> next() {
        if (steps == 10)
            return List.of(turnLeft().ahead(), turnRight().ahead());
        else if (steps < 4)
            return List.of(ahead());
        else 
            return List.of(ahead(), turnLeft().ahead(), turnRight().ahead());
    }

    public Xpose ahead() {
        return new Xpose(heading, pos.translate(heading, 1), steps+1);
    }

    public boolean equalsPos(Xpose p) {
        return pos.equals(p.pos);
    }

    public String toString() {
        return String.format("%s-%d@%s", heading, steps, pos);
    }


}
