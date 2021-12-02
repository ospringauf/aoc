package aoc2020;

import static common.Direction.EAST;
import static common.Direction.NORTH;
import static common.Direction.SOUTH;
import static common.Direction.WEST;

import java.util.function.Function;

import common.AocPuzzle;
import common.Direction;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 20: Jurassic Jigsaw ---
// https://adventofcode.com/2020/day/20

// - arrange image tiles into a final image and find "sea monster" pattern in that image
// - basically a puzzle style problem where tiles can be mirrored and rotated
// - analyze the problem first - it turns out that all border-matches are unambiguous!
// - rotate, flip, shift with point-transformation functions

class Day20 extends AocPuzzle {
	
	class Tile extends PointMap<Character> {
		int number;
		int max; // length-1 of each side, works only for square tiles!
		List<String> edges;
		List<Tile> variants;
		
		// construct a transformed variant 
		Tile(Tile base, Function<Point, Point> f) {
			this.number = base.number;
			this.max = base.max;
			base.keySet().forEach(p -> put(f.apply(p), base.get(p)));			
		}

		// parse input block
		Tile(String[] block) {
			number = Integer.valueOf(block[0].split("[: ]")[1]);
			read(List.of(block).drop(1));
			max = block[1].length()-1;
			
			Function<Point, Point> id = p -> p;
			Function<Point, Point> rot1 = p -> new Point(p.y(), max-p.x());
			Function<Point, Point> flip = p -> new Point(p.x(), max-p.y());
			Function<Point, Point> rot2 = rot1.andThen(rot1); 
			Function<Point, Point> rot3 = rot2.andThen(rot1);
			Function<Point, Point> r1f = rot1.andThen(flip);
			Function<Point, Point> r2f = rot2.andThen(flip);
			Function<Point, Point> r3f = rot3.andThen(flip);
			
			var transformations = List.of(id, rot1, rot2, rot3, flip, r1f, r2f, r3f);
			variants = transformations.map(tf -> new Tile(this, tf));			
			edges = variants.map(v -> v.edge(NORTH));
		}
		
		String edge(Direction h) {
			List<Integer> xrange = List.rangeClosed(0, max);			
			List<Integer> yrange = xrange;
			
			return switch (h) {
			case NORTH -> xrange.map(n -> getOrDefault(new Point(n, 0), '?')).mkString();
			case SOUTH -> xrange.map(n -> getOrDefault(new Point(n, max), '?')).mkString();
			case EAST  -> yrange.map(n -> getOrDefault(new Point(max, n), '?')).mkString();
			case WEST  -> yrange.map(n -> getOrDefault(new Point(0, n), '?')).mkString();
			default -> null;
			};
		}
		
		String innerLine(int y) {
			return List.rangeClosed(1, max-1).map(x -> get(new Point(x,y))).mkString();
		}
	}
	
	
	class Puzzle extends PointMap<Tile> {
		
		void update(Point p, List<Tile> remaining) {
			if (containsKey(p))
				// already solved
				return;
			
			if (! p.neighbors().exists(x -> containsKey(x)))
				// no neighbors to check
				return;
			
			var cand = remaining.flatMap(c -> c.variants);
			
			cand = matchNeighbor(p, NORTH, cand);
			cand = matchNeighbor(p, SOUTH, cand);
			cand = matchNeighbor(p, EAST, cand);
			cand = matchNeighbor(p, WEST, cand);
			
			if (cand.size() == 1)
				put(p, cand.single());
		}

		List<Tile> matchNeighbor(Point p, Direction dir, List<Tile> cand) {
			Point nei = p.translate(dir);
			if (containsKey(nei)) {
				var nedge = get(nei).edge(dir.opposite()); 
				return cand.filter(c -> nedge.equals(c.edge(dir)));
			}
			else
				return cand;
		}
		
		Set<Integer> placedTileNumbers() {
			var points = List.ofAll(keySet());
			return points.map(p -> get(p).number).toSet();
		}
		
	}

	List<Tile> tiles;
	String[] blocks = file2string("input20.txt").split("\n\n");
//	String[] blocks = readString("input20_test.txt").split("\n\n");
	
	List<String> otherEdges(Tile t) {
		return tiles.remove(t).flatMap(x -> x.edges);
	}

	void solve() {
		tiles = List.of(blocks).map(b -> b.split("\n")).map(Tile::new);
		int width = (int) Math.sqrt(blocks.length);
		
		
		System.out.println("=== part 1 [108603771107737]"); // 108603771107737
		
		List<Tile> corners = List.empty();
		for (var t : tiles) {
			var unique = t.edges.removeAll(otherEdges(t));
			if (unique.size() == 4) {
				corners = corners.append(t);
			}
		}
		
		System.out.println("corner tiles: " + corners.map(t -> t.number).mkString(", ")); 
		System.out.println("result: " + corners.map(t -> t.number).product());
		
//		System.out.println("=== verify unique fit");
//		
//		for (var t : tiles.sortBy(x -> x.number))
//			System.out.println(t.number + " --> " + t.edges.map(e -> otherEdges(t).count(oe -> oe.equals(e))).mkString(" "));

		System.out.println("=== part 2");
		
		// choose north-west corner tile
		var tile00 = corners
//				.flatMap(t -> t.variants)
				.filter(v -> ! otherEdges(v).contains(v.edge(NORTH)) && ! otherEdges(v).contains(v.edge(WEST)))
				.head();
		
		var puzzle = new Puzzle();
		puzzle.put(new Point(0, 0), tile00);
		
		var allPositions = List.range(0, width).crossProduct().map(x -> Point.of(x)).toList();
		var remainingTiles = tiles;
		
		System.out.println("=== part 2 find tile arrangement");

		// neighbors are unambiguous --> place tiles in single pass
		for (var p : allPositions) {
			puzzle.update(p, remainingTiles);
			var placed = puzzle.placedTileNumbers();
			remainingTiles = remainingTiles.filter(v -> ! placed.contains(v.number));
		}
		
		System.out.println("=== part 2 compose final image");
		
		var bitmap = List.of("Tile 0:");
		for (int yo : List.range(0, width))
			for (int yi : List.range(1, 9)) {				
				var line = 
				List.range(0, width)
					.map(x -> new Point(x, yo))
					.map(p -> puzzle.get(p))
					.map(tile -> tile.innerLine(yi))
					.mkString();
				bitmap = bitmap.append(line);
			}
		
		Tile image = new Tile(bitmap.toJavaArray(String.class));
		
		System.out.println("=== part 2 find monsters [2129]");
		for (var v : image.variants)
			searchForMonsters(v);
	}
	
	void searchForMonsters(Tile image) {
		var points = List.ofAll(image.keySet());
		var hits = points.filter(p -> isMonsterStart(image, p));
		if (hits.isEmpty())
			return;
			
		for (Point p : hits)
			monster(p).forEach(x -> image.put(x, 'O'));
		
		image.print();
		System.out.println("monsters: " + hits.size());
		System.out.println("roughness: " + image.countValues('#'));
	}

	boolean isMonsterStart(Tile image, Point p) {
		return monster(p).forAll(x -> image.getOrDefault(x, '?') == '#');
	}

	
	List<Point> monster(Point p) {
		var r = List.of(p);
		r = r.append(r.last().south().east());
		r = r.append(r.last().east(3));
		r = r.append(r.last().north().east());
		r = r.append(r.last().east());
		r = r.append(r.last().south().east());
		r = r.append(r.last().east(3));
		r = r.append(r.last().north().east());
		r = r.append(r.last().east());
		r = r.append(r.last().south().east());
		r = r.append(r.last().east(3));
		r = r.append(r.last().north().east());
		r = r.append(r.last().east());
		r = r.append(r.last().east());
		r = r.append(r.last().north().west());		
		return r;
	}

	public static void main(String[] args) {
		new Day20().solve();
	}
}
