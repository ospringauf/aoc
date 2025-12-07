package aoc2024;

import common.AocPuzzle;
import common.BronKerbosch;
import common.Util;
import io.vavr.collection.*;

//--- Day 23: LAN Party ---
// https://adventofcode.com/2024/day/23

class Day23 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 1083
        timed(() -> new Day23().part1());
        System.out.println("=== part 2"); // as,bu,cp,dj,ez,fd,hu,it,kj,nx,pp,xh,yu
        timed(() -> new Day23().part2());
    }

    List<String> data = file2lines("input23.txt");
//	List<String> data = Util.splitLines(example);

	record Edge(String a, String b) {
		static Edge parse(String s) {
			var f = s.split("-");
			return new Edge(f[0], f[1]);
		}
		boolean touches(String x) {
			return a.equals(x) || b.equals(x);
		}
	}
	
    void part1() {
    	var E = data.map(s -> HashSet.of(s.split("-")));
    	System.out.println(E);
    		
    	var N = E.flatMap(e -> e).toSet();
    	System.out.println(N);
    	
    	var te = E.filter(e -> e.exists(n -> n.charAt(0) == 't'));
    	
    	var e3 = te.flatMap(e ->  {
    		var n3 = N.removeAll(e);
    		n3 = n3.filter(n -> E.containsAll(e.map(ei -> HashSet.of(ei, n))));
    		return n3.map(n -> e.add(n));
    	});
    	System.out.println(e3);
    	e3 = e3.distinct().filter(e -> e.size() ==3);
    	
    	System.out.println(e3);
    	System.out.println(e3.size());
    }
    
    void part2() {
    	var E = data.map(s -> HashSet.of(s.split("-")));
    	var N = E.flatMap(e -> e).toSet();
    	
    	
    	var nb = N.toMap(
    			n ->n, 
    			n -> E.filter(e -> e.contains(n)).flatMap(e->e).toSet().remove(n));

    	Set<String> none = HashSet.empty();
    	var bk = new BronKerbosch<String>();
    	bk.neighbors = n -> nb.getOrElse(n, none);
    	var c = bk.largestClique(N.toList());
    	
    	System.out.println(c.toList().sorted().mkString(","));
    	
    }

    static String example = """
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn    		
""";
}
