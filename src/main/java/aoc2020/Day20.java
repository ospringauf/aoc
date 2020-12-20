package aoc2020;

import static common.Direction.EAST;
import static common.Direction.NORTH;
import static common.Direction.SOUTH;
import static common.Direction.WEST;

import java.util.function.Function;

import common.AocPuzzle;
import common.BoundingBox;
import common.Direction;
import common.Point;
import common.PointMap;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 20: Jurassic Jigsaw ---
// https://adventofcode.com/2020/day/20

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day20 extends AocPuzzle {
	
	class Tile extends PointMap<Character> {
		int number;
		List<String> edges;
		List<Tile> variants;
		BoundingBox bb;
		
		Tile(Tile base, Function<Point, Point> f) {
			this.number = base.number;
			base.keySet().forEach(p -> put(f.apply(p), base.get(p)));
			shiftToOrigin();
			bb = boundingBox();
		}

		Tile(String[] block) {
			// parse input block
			number = Integer.valueOf(block[0].split("[: ]")[1]);
			read(List.of(block).drop(1));
			
			bb = boundingBox();
			makeVariants();
		}
		
		void makeVariants() {
			Function<Point, Point> id = p -> p;
			Function<Point, Point> rot = p -> p.rotLeft();
			Function<Point, Point> flip = p -> p.flipY();
			Function<Point, Point> rot2 = rot.andThen(rot); 
			Function<Point, Point> rot3 = rot2.andThen(rot);
			Function<Point, Point> r1f = rot.andThen(flip);
			Function<Point, Point> r2f = rot2.andThen(flip);
			Function<Point, Point> r3f = rot3.andThen(flip);
			
			var transformations = List.of(id, rot, rot2, rot3, flip, r1f, r2f, r3f);
			variants = transformations.map(tf -> new Tile(this, tf));			
			edges = variants.map(v -> v.edge(NORTH));
		}
	
		
		String edge(Direction h) {
			List<Integer> xrange = List.rangeClosed(0, bb.xMax());			
			List<Integer> yrange = xrange;
			
			return switch (h) {
			case NORTH -> xrange.map(n -> getOrDefault(new Point(n, 0), '?')).mkString();
			case SOUTH -> xrange.map(n -> getOrDefault(new Point(n, bb.yMax()), '?')).mkString();
			case EAST  -> yrange.map(n -> getOrDefault(new Point(bb.xMax(),n), '?')).mkString();
			case WEST  -> yrange.map(n -> getOrDefault(new Point(0,n), '?')).mkString();
			default -> null;
			};
		}
		
		String innerLine(int y) {
			return List.rangeClosed(1, bb.xMax()-1).map(x -> get(new Point(x,y))).mkString();
		}
	}
	
	
	class CandMap extends PointMap<List<Tile>> {

		List<Tile> candidates(Point p) {
			return get(p);
		}
		
		void update(Point p, List<Tile> allcand) {
			if (containsKey(p) && candidates(p).size() == 1)
				// already unique
				return;
			
			if (! p.neighbors().exists(x -> containsKey(x)))
				// no neighbors to check
				return;
			
			var cand = getOrDefault(p, allcand);
			cand = allcand.retainAll(cand);
			
			cand = matchNeighbor(p, NORTH, cand);
			cand = matchNeighbor(p, SOUTH, cand);
			cand = matchNeighbor(p, EAST, cand);
			cand = matchNeighbor(p, WEST, cand);
			
			put(p, cand);
		}

		List<Tile> matchNeighbor(Point p, Direction dir, List<Tile> pcand) {
			Point nei = p.translate(dir);
			if (containsKey(nei)) {
				var nedges = candidates(nei).map(x -> x.edge(dir.opposite())); 
				return pcand.filter(c -> nedges.contains(c.edge(dir)));
			}
			else
				return pcand;
		}
		
		List<Integer> distinctTileNumbers(Point p) {
			return candidates(p).map(t -> t.number).distinct();
		}
		
		Set<Integer> placedTiles() {
			// points where only one tile fits (modulo variants)
			List<Point> points = List.ofAll(keySet());
			List<Point> uniqp = points.filter(p -> distinctTileNumbers(p).size() == 1);
			return uniqp.map(p -> distinctTileNumbers(p).single()).toSet();
		}
		
	}

	List<Tile> tiles;
	String[] blocks = readString("input20.txt").split("\n\n");
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
		
		System.out.println("=== verify unique fit");
		
		for (var t : tiles.sortBy(x -> x.number))
			System.out.println(t.number + " --> " + t.edges.map(e -> otherEdges(t).count(oe -> oe.equals(e))).mkString(" "));

		// TODO use unique fit to work with remaining tiles instead of variants
		
		System.out.println("=== part 2");
		
		// choose north-west corner tile
		// TODO 0,2,3 work but not 1 ???!??! test data: choose 2
		var tile00 = corners.get(0);
		var tile00variant = tile00.variants.filterNot(v -> otherEdges(v).contains(v.edge(NORTH)) || otherEdges(v).contains(v.edge(WEST)));
		
		if (tile00variant.size() == 0)
			throw new RuntimeException("wrong tile in 0,0");
		
		
		var candidateMap = new CandMap();
		candidateMap.put(Point.of(0, 0), tile00variant);
		
		var allPositions = List.range(0, width).crossProduct().map(x -> new Point(x._1, x._2)).toList();
		var remaining = tiles.flatMap(t -> t.variants);
		
		System.out.println("=== part 2 find tile arrangement");
		
		boolean found = false;
		while (! found) {
			for (var p : allPositions) {
				candidateMap.update(p, remaining);
			}
			var placed = candidateMap.placedTiles();
			remaining = remaining.filter(v -> ! placed.contains(v.number));
			
			System.out.println("remaining candidates: " + remaining.size());
//			candidateMap.printS(c -> String.format("%4d", c.size()));
			
			// single tile variant at every position?
			found = allPositions.forAll(p -> candidateMap.candidates(p).size() == 1);
		}
		
		System.out.println("=== part 2 compose final image");
		
		var bitmap = List.of("Tile 0:");
		for (int yo : List.range(0, width))
			for (int yi : List.range(1, 9)) {				
				var line = 
				List.range(0, width)
					.map(x -> new Point(x, yo))
					.map(p -> candidateMap.get(p).single())
					.map(t -> t.innerLine(yi))
					.mkString();
				bitmap = bitmap.append(line);
			}
		
		Tile img = new Tile(bitmap.toJavaArray(String.class));
		
		System.out.println("=== part 2 find monsters [2129]");
		for (var v : img.variants)
			searchForMonsters(v);
	}
	
	void searchForMonsters(Tile map) {
		var points = List.ofAll(map.keySet());
		var hits = points.filter(p -> isMonsterStart(map, p));
		if (hits.isEmpty())
			return;
			
		for (Point p : hits)
			monster(p).forEach(x -> map.put(x, 'O'));
		
		map.print();
		System.out.println("monsters: " + hits.size());
		System.out.println("roughness: " + map.countValues('#'));
	}

	boolean isMonsterStart(Tile map, Point p) {
		return monster(p).forAll(x -> map.getOrDefault(x, '?') == '#');
	}

	
	List<Point> monster(Point p) {
		List<Point> r = List.of(p);
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
