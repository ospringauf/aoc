package aoc2025;

import common.AocPuzzle;
import common.Util;
import io.vavr.Function2;
import io.vavr.collection.*;

//--- Day 11: Reactor  ---
// https://adventofcode.com/2025/day/11

class Day11 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1");
		timed(() -> new Day11().part1()); // 534
		System.out.println("=== part 2");
		timed(() -> new Day11().part2()); // 499645520864100
	}

    List<String> data = file2lines("input11.txt");
//	List<String> data = Util.splitLines(example);

    Map<String, Dev> devs;
    
    {
		var d = data.map(Dev::parse);
		devs = d.toMap(x -> x.name, x -> x);
    }
    
	record Dev(String name, List<String> next) {
		static Dev parse(String s) {
			var a = List.of(s.replace(":", "").split(" "));
			return new Dev(a.head(), a.tail());
		}
	}
	
	Function2<String, String, Long> mpaths = Function2.of(this::paths).memoized();
	
	long paths(String from, String to) {
		if (from.equals(to))
			return 1;
		if (! devs.containsKey(from))
			return 0; 
		var f = devs.get(from).get();
		return f.next.map(n -> mpaths.apply(n, to)).sum().longValue();
	}
	
	void part1() {		
		System.out.println(paths("you", "out"));
	}
	
	void part2() {
		System.out.println(paths("svr", "fft") * paths("fft", "dac") * paths("dac", "out"));
		System.out.println(paths("svr", "dac") * paths("dac", "fft") * paths("fft", "out"));
	}

	static String example = """
			aaa: you hhh
			you: bbb ccc
			bbb: ddd eee
			ccc: ddd eee fff
			ddd: ggg
			eee: out
			fff: out
			ggg: out
			hhh: ccc fff iii
			iii: out""";
	
	static String example2 = """
svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out""";			
}
