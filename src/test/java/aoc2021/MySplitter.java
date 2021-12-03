package aoc2021;

import java.util.function.Predicate;

import common.AocPuzzle;
import io.vavr.Function1;
import io.vavr.collection.List;

public class MySplitter extends AocPuzzle {
    
    record Cmd(String dir, int x) {}
    record Rule(int low, int high, char letter, String pw) {}
    
    record BagRule(String color, List<Bags> inner) {}
    record Bags(int amount, String color) {}
    
    public static <T> Function1<String,T> nullIf(Predicate<String> p, Function1<String,T> f) {
    	return s -> p.test(s) ? null : f.apply(s);
    }

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

        Function1<String, Bags> parseBags = nullIf(s -> s.startsWith("no"),  split(" ").andThen(r -> new Bags(r.i(0), r.s(1) + " " + r.s(2))));
        var t3a = lines(test3a)
                .map(split(" bags contain "))
                .map(r -> new BagRule(r.s(0), split(r.s(1), ", ").map(parseBags)));
            System.out.println(t3a);   

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
