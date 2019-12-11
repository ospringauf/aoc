package aoc2019;

public class Pose extends Point {
    Direction heading = Direction.NORTH;
    
    public Pose(int x, int y, Direction h) {        
        super(x, y);
        heading = h;
    }
    
    public Pose(int x, int y) {        
        this(x,y, Direction.NORTH);
    }


    void turnRight() {
        heading = Direction.values()[(heading.ordinal() + 1) % 4];
    }
    
    void turnLeft() {
        heading = Direction.values()[(heading.ordinal() + 3) % 4];
    }

    Pose next() {
        return new Pose(x + heading.dx, y + heading.dy, heading);
    }

}
