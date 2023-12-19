package aoc2023;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.*;
import io.vavr.control.Option;

//--- Day 19:  ---
// https://adventofcode.com/2023/day/19

class Day19wip extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1");
//		timed(() -> new Day19().part1());
		System.out.println("=== part 2");
		timed(() -> new Day19wip().part2());
	}

//	String data = example;
	String data = file2string("input19.txt");

	Map<String, Workflow> wfm;

	record Part(int x, int m, int a, int s) {
		static Part parse(String l) {
			var f = split(l, "[{}xmas=,]");
			return new Part(f.i(3), f.i(6), f.i(9), f.i(12));
		}

		int xmas() {
			return x + m + a + s;
		}

		int val(String n) {
			return switch (n) {
			case "x" -> x;
			case "m" -> m;
			case "a" -> a;
			case "s" -> s;

			default -> throw new IllegalArgumentException("Unexpected value: " + n);
			};
		}
	}

	enum Result {
		A, R, und, Jump
	};

	record Instr(String cond, String tgt) {
		static Instr parse(String s) {
			if (s.indexOf(':') > 0) {
				var c = s.split(":")[0];
				var t = s.split(":")[1];
				return new Instr(c, t);
			} else {
				return new Instr(null, s);
			}
		}

		Result eval(Part p) {
			if (cond == null || checkCond(p)) {
				return tgt.equals("A") ? Result.A : tgt.equals("R") ? Result.R : Result.Jump;
			} else {
				return Result.und;
			}
		}

		boolean checkCond(Part p) {
			var f = Util.splitWithDelimiters(cond, "<>");
			int v = p.val(f.get(0));
			var c = Integer.parseInt(f.get(2));
			return switch (f.get(1)) {
			case "<" -> v < c;
			case ">" -> v > c;
			default -> throw new IllegalArgumentException("Unexpected value: " + f.get(1));
			};
		}

		Integer pivot(String att) {
			if (cond == null) return 0;
			var f = Util.splitWithDelimiters(cond, "<>");
			if (att.equals(f.get(0)))
					return Integer.parseInt(f.get(2));
			else
				return 0;
		}

		Instr replaceTgt(String n, String r) {
			if (tgt.equals(n)) 
				return new Instr(null, r);
			else
				return this;
		}
	}

	record Workflow(String name, List<Instr> instr) {
		// px{a<2006:qkq,m>2090:A,rfg}
		static Workflow parse(String l) {
			var f = l.split("[{}]");
			return new Workflow(f[0], List.of(f[1].split(",")).map(Instr::parse));
		}
		
		List<Integer> pivots(String att) {
			return instr.map(i -> i.pivot(att));
		}
		
		Workflow simplify() {
			var r = instr.reverse();
			while (r.size() > 1 && r.head().cond == null && r.tail().head().tgt.equals(r.head().tgt)) {
				r = List.of(r.head()).appendAll(r.tail().tail());
			}
			return new Workflow(name, r.reverse());
		}
		
		List<String> calls() {
			return instr.map(i -> i.tgt).removeAll("R").removeAll("A");
		}
		
		Option<String> singleOutcome() {
			var s = instr.map(i -> i.tgt).distinct();
			return s.singleOption();
		}

		Workflow replace(String n, String r) {
			var i2 = instr.map(i -> i.replaceTgt(n,r));
			return new Workflow(name, i2);
		}
	}

	boolean evalWf(Workflow w, Part p) {
		System.out.println("  " + w.name);
		for (var i : w.instr) {
			var r = i.eval(p);
			if (r == Result.A) return true;
			if (r == Result.R) return false;
			if (r == Result.Jump) return evalWf(wfm.get(i.tgt).get(), p);
		}
		throw new RuntimeException("out of instructions");
	}

	void part1() {
//    	System.out.println(Part.parse("{x=787,m=2655,a=1222,s=2876}"));
//    	System.out.println(Workflow.parse("px{a<2006:qkq,m>2090:A,rfg}"));

		var block = data.split("\n\n");
		var wf = List.of(block[0].split("\n")).map(Workflow::parse);
		wfm = wf.toMap(w -> w.name, w -> w);
		var parts = List.of(block[1].split("\n")).map(Part::parse);

		List<Part> accepted = List.empty();

		for (var p : parts) {

			System.out.println(p);
			if (evalWf(wfm.get("in").get(), p))
				accepted = accepted.append(p);
		}

		var r = accepted.map(p -> p.xmas()).sum();
		System.out.println(r);
	}

	void part2() {
		// pivots
		// 6658743168
		// 2939055336
		// 2206495800
		// 2176843200
		var block = data.split("\n\n");
		var wf = List.of(block[0].split("\n")).map(Workflow::parse).map(w -> w.simplify());
		wfm = wf.toMap(w -> w.name, w -> w);
		
		System.out.println("workflows: " + wf.size());
		
		
		var so = wf.filter(w -> w.singleOutcome().isDefined());
		
		for (var wso :so)
			wf = wf.map(w -> w.replace(wso.name, wso.singleOutcome().get()));
		wf = wf.map(Workflow::simplify);
		
			
		so = wf.filter(w -> w.singleOutcome().isDefined());
		for (var wso :so)
			wf = wf.map(w -> w.replace(wso.name, wso.singleOutcome().get()));
		wf = wf.map(Workflow::simplify);
		

		so = wf.filter(w -> w.singleOutcome().isDefined());
		for (var wso :so)
			wf = wf.map(w -> w.replace(wso.name, wso.singleOutcome().get()));
		wf = wf.map(Workflow::simplify);

		
		// call tree
		List<Workflow> w0 = List.empty();
		var c0 = List.of("in");
		while (! c0.isEmpty()) {
			List<Workflow> w1 = c0.map(n -> wfm.get(n).get());
			w1 = w1.removeAll(w0);
			c0 = w1.flatMap(w -> w.calls());
			w0 = w0.appendAll(w1);
		}
		System.out.println("called workflows: " + w0.size());
		
		
		
		
		var px = wf.flatMap(w -> w.pivots("x")).distinct().sorted();
		var pm = wf.flatMap(w -> w.pivots("m")).distinct().sorted();
		var pa = wf.flatMap(w -> w.pivots("a")).distinct().sorted();
		var ps = wf.flatMap(w -> w.pivots("s")).distinct().sorted();
		System.out.println(px);
		System.out.println(pm);
		System.out.println(pa);
		System.out.println(ps);
		System.out.println((long)px.size()*pm.size()*pa.size()*ps.size());
	}

	static String example = """
			px{a<2006:qkq,m>2090:A,rfg}
			pv{a>1716:R,A}
			lnx{m>1548:A,A}
			rfg{s<537:gd,x>2440:R,A}
			qs{s>3448:A,lnx}
			qkq{x<1416:A,crn}
			crn{x>2662:A,R}
			in{s<1351:px,qqz}
			qqz{s>2770:qs,m<1801:hdj,R}
			gd{a>3333:R,R}
			hdj{m>838:A,pv}

			{x=787,m=2655,a=1222,s=2876}
			{x=1679,m=44,a=2067,s=496}
			{x=2036,m=264,a=79,s=2244}
			{x=2461,m=1339,a=466,s=291}
			{x=2127,m=1623,a=2188,s=1013}
			""";
}
