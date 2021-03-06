package aoc2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
  * Day 10: Monitoring Station
  * https://adventofcode.com/2019/day/10
  */
public class Day10 {

    static final String[] MAP1 = { ".#..#", ".....", "#####", "....#", "...##" };

    static final String[] MAP2 = { "......#.#.", "#..#.#....", "..#######.", ".#.#.###..", ".#..#.....", "..#....#.#",
            "#..#....#.", ".##.#..###", "##...#..#.", ".#....####" };
    static final String[] MAP3 =
            { ".#....#####...#..", "##...##.#####..##", "##...#...#.#####.", "..#.....X...###..", "..#.#.....#....##" };
    

    static class Asteroid extends Point {

        static final int MAX_ANGLE = (int) ((2 * Math.PI) * 1000);
        
        Asteroid(int x, int y) {
            super(x,y);
        }

        /**
         * angle from this to a in milli-rads, counted clockwise, North=0.
         * this function is as f***ed up as its name indicates.
         */
        int mradClockwiseFromNorth(Asteroid a) {
        	// standard atan2 counts counter-clockwise from East=0
            var rad = Math.atan2(-(a.x - x), a.y - y) + Math.PI; // clockwise, rotate 90° ccw
            return (int) (1000 * rad) % MAX_ANGLE;
        }
    }
    
    public static void main(String[] args) throws Exception {
        var map = Util.linesArray("input10.txt");
        List<Asteroid> asteroids = readMap(map, '#');
        
        System.out.println("=== part1 ===");
        Asteroid loc = new Day10().part1(asteroids);

        System.out.println("=== part2 ===");
        new Day10().part2(asteroids, loc);
    }
    

    static List<Asteroid> readMap(String[] map, char marker) {
        ArrayList<Asteroid> asteroids = new ArrayList<>();
        for (int y = 0; y < map.length; ++y)
            for (int x = 0; x < map[y].length(); ++x)
                if (map[y].charAt(x) == marker)
                    asteroids.add(new Asteroid(x, y));
        return asteroids;
    }


    Asteroid part1(List<Asteroid> asteroids) {        
        Map<Asteroid, Long> result = new HashMap<>();

        for (Asteroid station : asteroids) {         
        	// for all other asteroids ...
            var targets = asteroids.stream().filter(a -> a != station);
            // ... determine the angle to station, for each angle value only one asteroid is visible.
            var visible = targets.map(station::mradClockwiseFromNorth).distinct().count();
            result.put(station, visible);
        }

        // find the station with max visible targets
        var bestLocation = result.entrySet().stream().max(Comparator.comparing(e -> e.getValue())).orElse(null);
        System.out.println(bestLocation);
        return bestLocation.getKey();
    }

    
    void part2(List<Asteroid> asteroids, Asteroid station) {
        asteroids.remove(station);
        var vaporized = new ArrayList<Asteroid>();

        while (!asteroids.isEmpty()) {
        	// calculate and sort the angles, starting from north=0 (clockwise)
            int[] angles = asteroids.stream().mapToInt(station::mradClockwiseFromNorth).distinct().sorted().toArray();
            for (int angle : angles) {
            	// for each angle, zap the nearest asteroid
                Asteroid firstHit = asteroids.stream()
                        .filter(a -> station.mradClockwiseFromNorth(a) == angle)
                        .min(Comparator.comparing(station::manhattan))
                        .orElse(null);
                
                vaporized.add(firstHit);
            }
            asteroids.removeAll(vaporized);
        } 

        System.out.println(vaporized.get(199));
    }

}
