package aoc2018.revisit;

import java.util.function.Function;

import common.AocPuzzle;
import common.BronKerbosch;
import common.Util;
import io.vavr.collection.List;
import io.vavr.collection.Set;

// --- Day 23: Experimental Emergency Teleportation ---
// https://adventofcode.com/2018/day/23

// largest-clique solution from
// https://todd.ginsberg.com/post/advent-of-code/2018/day23/

// TODO we're not done yet - the result from this code is off by 1, but only for my input !?!

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day23ok extends AocPuzzle {

	record Point3d(long x, long y, long z) {
		
		long dist(Point3d p) {
			return Math.abs(x - p.x) + Math.abs(y - p.y) + Math.abs(z - p.z);
		}

		long dist() {
			return dist(new Point3d(0,0,0));
		}
	}
	
	record Nanobot(Point3d location, long radius) {
		static Nanobot parse(String s) {
			var a = s.split("[<>]");
			var b = a[1].split(",");
			var x = Long.parseLong(b[0]);
			var y = Long.parseLong(b[1]);
			var z = Long.parseLong(b[2]);
			var r = Long.parseLong(s.split(", r=")[1]);
			return new Nanobot(new Point3d(x, y, z), r);
		}

		long dist(Nanobot other) {
			return location.dist(other.location);
		}

		boolean inRange(Nanobot other) {
			return dist(other) <= radius;
		}

		boolean withinRangeOfSharedPoint(Nanobot other) {
			return dist(other) <= radius + other.radius;
		}
	}

	List<Nanobot> bots = file2lines("../day23.txt").map(Nanobot::parse); // 98565591 (correct) vs 98565590 (this)
//	List<Nanobot> bots = lines("../day23-other1.txt").map(Nanobot::parse); // 105191907
//	List<Nanobot> bots = lines("../day23-other2.txt").map(Nanobot::parse); // 113066145
//	List<Nanobot> bots = lines("../day23-other3.txt").map(Nanobot::parse); // 142473501
//	List<Nanobot> bots = Util.splitLines(example2).map(Nanobot::parse);

	void part1() {
		var maxi = bots.maxBy(b -> b.radius).get();
//		System.out.println(maxi);
		var r = bots.count(b -> maxi.inRange(b));
		System.out.println(r);
	}

	void part2() {
		
		Function<Nanobot, Set<Nanobot>> neighbors = bot -> bots
				.filter(it -> it!=bot)
				.filter(it -> bot.withinRangeOfSharedPoint(it))
				.toSet();
		
		var nmap = bots.toMap(bot -> bot, bot -> neighbors.apply(bot));
		System.out.println("neighbor map ready");
		
		
		var bk = new BronKerbosch<Nanobot>();
		bk.neighbors = bot -> nmap.get(bot).get();
		var clique = bk.largestClique(bots);
		
		System.out.println("clique size: " + clique.size());
		
		var result = clique.map(it -> it.location.dist() - it.radius).max();
		System.out.println(result);
	}

	public static void main(String[] args) {

		System.out.println("=== part 1"); // 659
		new Day23ok().part1();

		// TODO this program calculates 98565590, but the correct answer is 98565591 ?!? 
		System.out.println("=== part 2"); // 98565591
		new Day23ok().part2();
		
		// wrong: 60949105, 89402056, 98565590
	}

	static String example = """
			pos=<0,0,0>, r=4
			pos=<1,0,0>, r=1
			pos=<4,0,0>, r=3
			pos=<0,2,0>, r=1
			pos=<0,5,0>, r=3
			pos=<0,0,3>, r=1
			pos=<1,1,1>, r=1
			pos=<1,1,2>, r=1
			pos=<1,3,1>, r=1
						""";

	static String example2 = """
			pos=<10,12,12>, r=2
			pos=<12,14,12>, r=2
			pos=<16,12,12>, r=4
			pos=<14,14,14>, r=6
			pos=<50,50,50>, r=200
			pos=<10,10,10>, r=5
						""";

}
