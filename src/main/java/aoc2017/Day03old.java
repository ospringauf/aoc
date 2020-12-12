package aoc2017;

import aoc2017.Day03Grid.Pos;

public class Day03old {

	
    public static void main(String[] args) throws Exception {
        part1();
    }

    private static void part1test() throws Exception {
    	Day03Grid grid = new Day03Grid(10, 10);
    	Pos p = new Pos(0,0);
    	grid.set(p, 1);
    	for (int i=2; i<=23; ++i) {
    		p = grid.next(p);
    		grid.set(p, i);
    	}
    	grid.print();
    	System.out.println("pos: " + p.x + "," + p.y + " ==> " + (Math.abs(p.x) + Math.abs(p.y)));
    }

    private static void part1() throws Exception {
    	Day03Grid grid =new Day03Grid(1000, 1000);
    	Pos p = new Pos(0,0);
    	grid.set(p, 1);
    	for (int i=2; i<=325489; ++i) {
    		p = grid.next(p);
    		grid.set(p, i);
    	}
    	//grid.print();
    	System.out.println("pos: " + p.x + "," + p.y + " ==> " + (Math.abs(p.x) + Math.abs(p.y)));
    }

    private static void part2() throws Exception {
    	Day03Grid grid =new Day03Grid(20, 20);
    	Pos p = new Pos(0,0);
    	
    	int v = 1;
    	while (v < 325489) {
    		grid.set(p, v);
    		p = grid.next(p);
    		v = grid.sumAdjacent(p);
    	}
    	
    	grid.print();
    	System.out.println(v);

    }
    
}
