package aoc2020;

import common.AocPuzzle;
import common.Point;
import common.PointMap;
import common.Util;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 24: Lobby Layout ---
// https://adventofcode.com/2020/day/24

// Game of Life on a floor of hexagonal black/white tiles
// - hex tiles - similar to https://adventofcode.com/2017/day/11 (but different orientation)
// - game of life, again

// hex coord systems: https://www.redblobgames.com/grids/hexagons/
// used here: "doubled coordinates"

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day24 extends AocPuzzle {

	static final Boolean BLACK = Boolean.TRUE;
	static final Boolean WHITE = Boolean.FALSE;

	static Point hexAdjacent(Point p, String d) {
		return switch (d) {
		case "ne" -> p.north().east();
		case "nw" -> p.north().west();
		case "se" -> p.south().east();
		case "sw" -> p.south().west();
		case "e" -> p.east().east();
		case "w" -> p.west().west();
		default -> null;
		};
	}

	static List<Point> allHexAdjacent(Point p) {
		return List.of("ne", "nw", "se", "sw", "e", "w").map(s -> hexAdjacent(p, s));
	}

	Point walkGrid(String s) {
		var p = new Point(0, 0);

		while (!s.isEmpty()) {
			var len = (s.startsWith("e") || s.startsWith("w")) ? 1 : 2;
			String dir = s.substring(0, len);
			s = s.substring(len);
			p = hexAdjacent(p, dir);
		}
		return p;
	}
	
	static class TileFloor extends PointMap<Boolean> {
		
		// game of life: single cycle
		TileFloor nextDay() {
			
			// "expand" step: make sure that all white neighbors of black tiles are in the map
			Set<Point> blacktiles = findPoints(BLACK).toSet();
			Set<Point> newNeighbors = blacktiles.flatMap(t -> allHexAdjacent(t)).removeAll(this.keySet());
			newNeighbors.forEach(n -> this.put(n, WHITE));
			
			
			var next = new TileFloor();
			
			for (Point p : keySet()) {
				var blackNeighbors = allHexAdjacent(p).count(n -> this.getOrDefault(n, WHITE));			
				Boolean isBlack = this.get(p);
				
				if (isBlack && (blackNeighbors == 0 || blackNeighbors > 2)) {
					next.put(p, WHITE);
				} else if (!isBlack && (blackNeighbors == 2)) {
					next.put(p, BLACK);
				} else {
					next.put(p, isBlack);
				}
			}
			
			return next;
		}

		void flip(Point p) {
			var color = getOrDefault(p, WHITE);
			put(p, ! color);			
		}
	}

	void solve() {
		var lines = file2lines("input24.txt");
//		var lines = Util.splitLines(example);

		System.out.println("=== part 1");
		
		var floor = new TileFloor();
		
		for (var s : lines) {
			Point tile = walkGrid(s);
			floor.flip(tile);
		}

		System.out.println("=> black tiles: " + floor.countValues(BLACK));

		System.out.println("=== part 2");

		for (int i = 0; i < 100; ++i) {
			floor = floor.nextDay();
		}
		
//		floor.print(b -> b? '⬢': '⬡');
		System.out.println("=> after 100 days: " + floor.countValues(BLACK));		
	}


	public static void main(String[] args) {
		new Day24().solve();
	}

	static String example = """
			sesenwnenenewseeswwswswwnenewsewsw
			neeenesenwnwwswnenewnwwsewnenwseswesw
			seswneswswsenwwnwse
			nwnwneseeswswnenewneswwnewseswneseene
			swweswneswnenwsewnwneneseenw
			eesenwseswswnenwswnwnwsewwnwsene
			sewnenenenesenwsewnenwwwse
			wenwwweseeeweswwwnwwe
			wsweesenenewnwwnwsenewsenwwsesesenwne
			neeswseenwwswnwswswnw
			nenwswwsewswnenenewsenwsenwnesesenew
			enewnwewneswsewnwswenweswnenwsenwsw
			sweneswneswneneenwnewenewwneswswnese
			swwesenesewenwneswnwwneseswwne
			enesenwswwswneneswsenwnewswseenwsese
			wnwnesenesenenwwnenwsewesewsesesew
			nenewswnwewswnenesenwnesewesw
			eneswnwswnwsenenwnwnwwseeswneewsenese
			neswnwewnwnwseenwseesewsenwsweewe
			wseweeenwnesenwwwswnew
						""";

}
