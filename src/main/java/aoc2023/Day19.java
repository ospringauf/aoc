package aoc2023;

import common.AocPuzzle;
import common.Range;
import common.Util;
import io.vavr.collection.List;
import io.vavr.collection.Map;

//--- Day 19: Aplenty ---
// https://adventofcode.com/2023/day/19

class Day19 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 399284
        timed(() -> new Day19().part1());
        System.out.println("=== part 2"); // 121964982771486
        timed(() -> new Day19().part2());
    }

//    String data = example;
    String data = file2string("input19.txt");

    List<Part> parts;
    static java.util.Map<String, Workflow> workflows;

    record Part(int x, int m, int a, int s) {
        static Part parse(String l) {
            var f = split(l, "[{}xmas=,]");
            return new Part(f.i(3), f.i(6), f.i(9), f.i(12));
        }

        int xmasRating() {
            return x + m + a + s;
        }

        int value(String n) {
            return switch (n) {
            case "x" -> x;
            case "m" -> m;
            case "a" -> a;
            case "s" -> s;

            default -> throw new IllegalArgumentException("Unexpected value: " + n);
            };
        }
    }

    record Rule(String cond, String tgt) {
        enum Result {
            A, R, Jump, NoMatch
        };
        
        static Rule parse(String s) {
            if (s.indexOf(':') > 0) {
                var c = s.split(":")[0];
                var t = s.split(":")[1];
                return new Rule(c, t);
            } else {
                return new Rule(null, s);
            }
        }

        Result eval(Part p) {
            if (cond == null || checkCond(p)) {
                return tgt.equals("A") ? Result.A : tgt.equals("R") ? Result.R : Result.Jump;
            } else {
                return Result.NoMatch;
            }
        }

        boolean checkCond(Part p) {
            var f = Util.splitWithDelimiters(cond, "<>");
            int v = p.value(f.get(0));
            var c = Integer.parseInt(f.get(2));
            return switch (f.get(1)) {
            case "<" -> v < c;
            case ">" -> v > c;
            default -> throw new IllegalArgumentException("Unexpected value: " + f.get(1));
            };
        }
    }
    

    record XmasRange(Map<String, Range> r) {
        static XmasRange all() {
            var r4000 = new Range(1, 4000);
            return new XmasRange(List.of("x", "m", "a", "s").toMap(c -> c, c -> r4000));
        }

        long size() {
            return r.values().map(Range::size).product().longValue();
        }

        Ranges splitWithCondition(String cond) {
            // format "att op c" (eg "a<2007")
            var f = Util.splitWithDelimiters(cond, "<>");
            String att = f.get(0);
            boolean less = f.get(1).equals("<");
            var c = Integer.parseInt(f.get(2));

            var ra = r.get(att).get();

            if (less) {
                var t = ra.splitAt(c);
                var match = new XmasRange(r.put(att, t._1));
                var remain = new XmasRange(r.put(att, t._2));
                return new Ranges(match, remain);
            } else {
                var t = ra.splitAt(c + 1);
                var remain = new XmasRange(r.put(att, t._1));
                var match = new XmasRange(r.put(att, t._2));
                return new Ranges(match, remain);
            }
        }
    }

    record Ranges(XmasRange match, XmasRange remain) {
    }


    record Workflow(String name, List<Rule> rules) {
        static Workflow parse(String l) {
            // px{a<2006:qkq,m>2090:A,rfg}
            var f = l.split("[{}]");
            return new Workflow(f[0], List.of(f[1].split(",")).map(Rule::parse));
        }

        boolean eval(Part p) {
            for (var rule : rules) {
                var r = rule.eval(p);
                if (r == Rule.Result.A)
                    return true;
                if (r == Rule.Result.R)
                    return false;
                if (r == Rule.Result.Jump)
                    return workflows.get(rule.tgt).eval(p);
            }
            throw new RuntimeException("out of instructions");
        }

        public List<XmasRange> eval(XmasRange range) {

            List<XmasRange> acc = List.empty();
            for (var i : rules) {

                if (i.cond == null) {
                    if (i.tgt.equals("A")) {
                        acc = acc.append(range);
                        return acc;
                    } else if (i.tgt.equals("R")) {
                        return acc;
                    } else {
                        // execute tgt workflow
                        var wf = workflows.get(i.tgt);
                        acc = acc.appendAll(wf.eval(range));
                        return acc;
                    }
                }

                // cond:A
                var t = range.splitWithCondition(i.cond);
                
                if (i.tgt.equals("A")) {
                    acc = acc.append(t.match);
                    range = t.remain;
                }
                // cond:R
                else if (i.tgt.equals("R")) {
                    range = t.remain;
                }
                // cond:wf
                // execute tgt workflow
                else {
                    var wf = workflows.get(i.tgt);
                    acc = acc.appendAll(wf.eval(t.match));
                    range = t.remain;
                }
            }
            return acc;
        }
    }

    Day19() {
        var block = data.split("\n\n");
        var wf = List.of(block[0].split("\n")).map(Workflow::parse);
        workflows = wf.toMap(w -> w.name, w -> w).toJavaMap();
        parts = List.of(block[1].split("\n")).map(Part::parse);
    }

    void part1() {
        Workflow in = workflows.get("in");
        var acceptedParts = parts.filter(in::eval);
        System.err.println(acceptedParts.map(Part::xmasRating).sum());
    }

    void part2() {
        var in = workflows.get("in");
        var acceptedRanges = in.eval(XmasRange.all());
        System.err.println(acceptedRanges.map(XmasRange::size).sum());
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
