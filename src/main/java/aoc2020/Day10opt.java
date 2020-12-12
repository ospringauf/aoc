package aoc2020;

import java.util.HashMap;
import java.util.Map;

import io.vavr.collection.List;

// --- Day 10: Adapter Array ---
// https://adventofcode.com/2020/day/10

// rethought, optimized version

@SuppressWarnings({ "deprecation", "preview" })
class Day10opt extends AocPuzzle {

    public static void main(String[] args) {

        System.out.println("=== part 1"); // 1984
        new Day10opt().part1();

        System.out.println("=== part 2"); // 3543369523456
        new Day10opt().part2();
}

//    final List<Integer> adapters = List.of(example2.split("\n")).map(Integer::valueOf);
    final List<Integer> adapters = ints("input10.txt");

    void part1() {
        var chain = adapters.append(0).sorted();

        var diffs = chain.sliding(2).map(w -> w.get(1) - w.get(0)).toList();
        var d1 = diffs.count(x -> x == 1);
        var d3 = diffs.count(x -> x == 3) + 1;
        System.out.println(d1 * d3);
    }
    
    void part2() {
    	Map<Integer, Long> options = new HashMap<>();
    	List<Integer> jolts = adapters.sorted().reverse().append(0);
    	
    	options.put(jolts.head() + 3, 1L);
    	
    	for (int j : jolts) {
    		options.put(j, 
    				options.getOrDefault(j+1, 0L) + 
    				options.getOrDefault(j+2, 0L) + 
    				options.getOrDefault(j+3, 0L));
    	}
    	System.out.println(options.get(0));
    }
    

    static String example2 = """
            28
            33
            18
            42
            31
            14
            46
            20
            48
            47
            24
            23
            49
            45
            19
            38
            39
            11
            1
            32
            25
            35
            8
            17
            7
            9
            4
            2
            34
            10
            3
            			""";

    static String example1 = """
            16
            10
            15
            5
            1
            11
            7
            19
            6
            12
            4
            		""";

}