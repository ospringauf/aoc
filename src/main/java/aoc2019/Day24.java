package aoc2019;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/*
 * Day 24: Planet of Discord
 * https://adventofcode.com/2019/day/24
 * 
 */
public class Day24 {

	final static String[] EX1 = ("....#\n" + "#..#.\n" + "#..##\n" + "..#..\n" + "#....").split("\n");

	final static String[] EX2 = ("....#\n" + "#..#.\n" + "#.?##\n" + "..#..\n" + "#....").split("\n");

	final static String[] MY_INPUT = ("#..#.\n" + "..#..\n" + "...##\n" + "...#.\n" + "#.###").split("\n");

	class Eris extends PointMap<Character> {
		static final char EMPTY = '.';
		static final char BUG = '#';

		int level = 0;

		Eris(int level) {
			this.level = level;
		}

		public Eris(int level, Eris old) {
			this(level);
			old.entrySet().forEach(e -> put(e.getKey(), e.getValue()));
		}

		long biodiversity() {
			var r = findPoints(BUG).mapToLong(p -> 1 << (p.x + 5 * p.y)).reduce((a, b) -> a | b).orElse(0);
			return r;
		}

		long countBugs() {
			return findPoints(BUG).count();
		}
		
		Character get(Point p) {
			return getOrDefault(p, EMPTY);
		}
		
		Point point(int n) {
			return Point.of((n-1)%5, (n-1)/5);
		}
		
		boolean isBug(Point p) {
			return get(p) == BUG;
		}
		
		int no(Point p) {
			return 1 + p.x + 5*p.y;
		}
		
		Stream<Character> neighborValues(Point p) {
			var n = no(p);
			var upper = fetchLevel(level-1);
			var lower = fetchLevel(level+1);
			switch (n) {
			case 1: 
				return Stream.of(get(p.south()), get(p.east()), upper.get(point(8)), upper.get(point(12)));
			case 2:
			case 3:
			case 4:
				return Stream.of(get(p.south()), get(p.west()), get(p.east()), upper.get(point(8)));
			case 5:
				return Stream.of(get(p.south()), get(p.west()), upper.get(point(8)), upper.get(point(14)));
			case 6:
			case 11:
			case 16:
				return Stream.of(get(p.north()), get(p.south()), get(p.east()), upper.get(point(12)));
			case 8:
				return Stream.of(get(p.north()), get(p.west()), get(p.east()), lower.get(point(1)), lower.get(point(2)), lower.get(point(3)), lower.get(point(4)), lower.get(point(5)));
			case 10:
			case 15:
			case 20:
				return Stream.of(get(p.north()), get(p.south()), get(p.west()), upper.get(point(14)));
			case 12:
				return Stream.of(get(p.north()), get(p.south()), get(p.west()), lower.get(point(1)), lower.get(point(6)), lower.get(point(11)), lower.get(point(16)), lower.get(point(21)));
			case 13: 
				return Stream.of(EMPTY);
			case 14: 
				return Stream.of(get(p.north()), get(p.south()), get(p.east()), lower.get(point(5)), lower.get(point(10)), lower.get(point(15)), lower.get(point(20)), lower.get(point(25)));
			case 18:
				return Stream.of(get(p.south()), get(p.west()), get(p.east()), lower.get(point(21)), lower.get(point(22)), lower.get(point(23)), lower.get(point(24)), lower.get(point(25)));
			case 21:
				return Stream.of(get(p.north()), get(p.east()), upper.get(point(12)), upper.get(point(18)));
			case 22:
			case 23:
			case 24:
				return Stream.of(get(p.north()), get(p.west()), get(p.east()), upper.get(point(18)));
			case 25:
				return Stream.of(get(p.north()), get(p.west()), upper.get(point(14)), upper.get(point(18)));
			
			default:
				return p.neighbors().map(x -> get(x));
			}
		}

		Eris fetchLevel(int i) {
			if (! levels.containsKey(i)) {
				var eris = new Eris(i);
				keySet().forEach(p -> eris.put(p, EMPTY));
				levels.put(i, eris);
			}
			return levels.get(i);
		}

		Eris next1() {
			Predicate<Point> die = p -> p.neighbors().filter(this::isBug).count() != 1;
			Predicate<Point> infest = p -> {
				var c = p.neighbors().filter(this::isBug).count();
				return c == 1 || c == 2;
			};

			var next = new Eris(level, this);
			
			findPoints(BUG).filter(die).forEach(p -> next.put(p, EMPTY));
			findPoints(EMPTY).filter(infest).forEach(p -> next.put(p, BUG));
			return next;
		}
		
		Eris next2() {
			Predicate<Point> die = p -> neighborValues(p).filter(c -> c==BUG).count() != 1;
			Predicate<Point> infest = p -> {
				var bugs = neighborValues(p).filter(c -> c==BUG).count();
				return bugs == 1 || bugs == 2;
			};

			var next = new Eris(level, this);
			
			findPoints(BUG).filter(die).forEach(p -> next.put(p, EMPTY));
			findPoints(EMPTY).filter(infest).forEach(p -> next.put(p, BUG));
			return next;
		}
	}
	
	Map<Integer, Eris> levels = new HashMap<>();

	public static void main(String[] args) {
		long t0 = System.currentTimeMillis();

		// 24662545
		System.out.println("=== part 1 ===");
		new Day24().part1(MY_INPUT);

		System.out.println("=== part 2 test ===");
		new Day24().part2(EX2, 10);
		
		System.out.println("=== part 2 ===");
		new Day24().part2(MY_INPUT, 200);

		
		System.out.printf("=== end (%d ms) ===%n", System.currentTimeMillis() - t0);
	}

	void part1(String[] a) {
		var m = new Eris(0);
		m.read(a, c -> c);

		var seen = new HashSet<Long>();
		
		while (true) {
			var b = m.biodiversity();
			if (seen.contains(b)) {
				m.print();
				System.out.println(b);
				break;
			}
			seen.add(b);
			m = m.next1();
		}		
	}
	

	void part2(String[] a, int minutes) {
		var m = new Eris(0);
		m.read(a, c -> c);
		levels.put(0, m);

		m.print();
		
		for (int step = 0; step < minutes; ++step) {
			
			var min = levels.values().stream().min(Comparator.comparing((Eris e) -> e.level)).get();
			var max = levels.values().stream().max(Comparator.comparing((Eris e) -> e.level)).get();
			min.fetchLevel(min.level - 1);
			max.fetchLevel(max.level + 1);
			
			Map<Integer, Eris> nextLevels = new HashMap<>();
			for (int l=min.level-1; l<=max.level+1; ++l) {
				var map = levels.get(l);
				nextLevels.put(l, map.next2());
			}
			levels = nextLevels;
		}
		
//		levels.keySet().stream().sorted().forEach(l -> {
//			System.out.println("Depth " + l);
//			levels.get(l).print();
//			System.out.println();
//		});
		
		var bugs = levels.values().stream().mapToLong(x -> x.countBugs()).sum();
		System.out.println("bugs: " + bugs);
	}

}
