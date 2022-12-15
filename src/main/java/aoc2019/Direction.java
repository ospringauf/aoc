package aoc2019;

enum Direction {

        NORTH(0, -1),
        EAST(1, 0),
        SOUTH(0, 1),
        WEST(-1, 0);

        int dx;
        int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;

        }
}
