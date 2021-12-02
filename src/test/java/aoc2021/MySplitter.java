package aoc2021;

import common.AocPuzzle;
import io.vavr.collection.List;

public class MySplitter extends AocPuzzle {
    
    record Cmd(String dir, int x) {}
    record Rule(int low, int high, char letter, String pw) {}
    
    record Bags(int amount, String color) {}
    record BagRule(String color, List<Bags> inner) {}

//    record SplitResult(String[] fields) {
//
//        public String s(int i) {
//            return fields[i];
//        }
//
//        public int i(int i) {
//            return Integer.valueOf(fields[i]);
//        }
//        
//        public char c(int i) {
//            return fields[i].charAt(0);
//        }
//
//        public <T> List<T> map(Function<String, T> f) {
//            return List.of(fields).map(f);
//        }
//        
//        public List<SplitResult> split(String pat) {
//            return map(s -> MySplitter.split(s, pat)); 
//        }
//    }
//    
//    static SplitResult split(String s, String pat) {
//        return new SplitResult(s.split(pat));
//    }
//    
//    static SplitResult lines(String s) {
//        return new SplitResult(s.split("\n"));
//    }
//
//    static Function<String, SplitResult> split(String pat) {
//        return s -> split(s, pat);
//    }

    public static void main(String[] args) {

        var t1 = lines(test1).map(split(" ").andThen(r -> new Cmd(r.s(0), r.i(1))));
        System.out.println(t1);

        var t2 = lines(test2).map(split("[- :]+").andThen(r -> new Rule(r.i(0), r.i(1), r.c(2), r.s(3))));
        System.out.println(t2);     
        
//        var t3 = lines(test3)
//            .map(split(" bags contain "))
//            .map(r -> new BagRule(r.s(0), split(r.s(1), ", ").map(split(" ").andThen(r2 -> new Bags(r2.i(0), r2.s(1) + " " + r2.s(2))))));
//        System.out.println(t3);

        var t3 = lines(test3)
            .map(split(" bags contain "))
            .map(r -> new BagRule(r.s(0), split(r.s(1), ", ").split(" ").map(r2 -> new Bags(r2.i(0), r2.s(1) + " " + r2.s(2)))));
        System.out.println(t3);   
   
    }
    
    static String test1 = """
forward 3
down 4
forward 3
up 4
""";
    
    static String test2 = """
1-5 k: kkkkhkkkkkkkkkk
5-7 k: blkqhtxfgktdkxzkksk
15-16 x: xxxxxxxxxxxxxxlf
""";            
    
    static String test3 = """
faded plum bags contain 5 wavy cyan bags.
dull aqua bags contain 4 dark fuchsia bags, 1 shiny purple bag.
dotted olive bags contain 1 striped gray bag.
vibrant brown bags contain 4 dark tan bags, 4 mirrored gray bags.
            """;

    static String test3a = """
faded plum bags contain 5 wavy cyan bags.
dull aqua bags contain 4 dark fuchsia bags, 1 shiny purple bag.
dotted olive bags contain 1 striped gray bag.
vibrant brown bags contain 4 dark tan bags, 4 mirrored gray bags.
posh magenta bags contain no other bags.            
            """;

    
}
