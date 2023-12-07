package aoc2023;

import java.util.Comparator;

import common.AocPuzzle;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;

//--- Day 7: Camel Cards ---
// https://adventofcode.com/2023/day/7

class Day07 extends AocPuzzle {

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 251216224
        timed(() -> new Day07().part1());
        System.out.println("=== part 2"); // 250825971
        timed(() -> new Day07().part2());
    }

    List<String> data = file2lines("input07.txt");
//    List<String> data = Util.splitLines(example);

    
    static List<Character> VALUES_1 = List.ofAll("AKQJT98765432".toCharArray()).reverse();
    static List<Character> VALUES_2 = List.ofAll("AKQT98765432J".toCharArray()).reverse();

    record Hand(List<Character> cards, int bid) {

        static Hand parse(String s) {
            String[] f = s.split(" ");
            return new Hand(List.ofAll(f[0].toCharArray()), Integer.parseInt(f[1]));
        }

        int typePart1() {
            Map<Character, Integer> cc = cards.distinct().toMap(c -> new Tuple2(c, cards.count(x -> x == c)));
            return type0(cc);
        }
        
        int typePart2() {
            int jokers = cards.count(x -> x == 'J');
            Map<Character, Integer> cc = cards.removeAll('J').distinct().toMap(c -> new Tuple2(c, cards.count(x -> x == c)));
            
            return switch (jokers) {
            case 1 -> type1(cc);
            case 2 -> type2(cc);
            case 3 -> type3(cc);
            case 4 -> 7;
            case 5 -> 7;
            default -> type0(cc);
            };
        }

        // 5 cards, no joker
        static int type0(Map<Character, Integer> cc) {
            // five (7)
            if (cc.exists(t -> t._2 == 5))
                return 7;

            // four (6)
            if (cc.exists(t -> t._2 == 4))
                return 6;

            // full house (5)
            if (cc.exists(t -> t._2 == 3) && cc.exists(t -> t._2 == 2))
                return 5;

            // three (4)
            if (cc.exists(t -> t._2 == 3))
                return 4;

            // two pair (3)
            if (cc.count(t -> t._2 == 2) == 2)
                return 3;

            // one pair (2)
            if (cc.exists(t -> t._2 == 2))
                return 2;

            return 1;
        }

        // 4 cards, 1 joker
        static int type1(Map<Character,Integer> cc) {
            
            // five (7)
            if (cc.exists(t -> t._2 == 4))
                return 7;

            // four (6)
            if (cc.exists(t -> t._2 == 3))
                return 6;

            // full house (5)
            if (cc.count(t -> t._2 == 2) == 2)
                return 5;

            // three (4)
            if (cc.exists(t -> t._2 == 2))
                return 4;

            return 2;
        }

        // 3 cards, 2 jokers
        static int type2(Map<Character,Integer> cc) {

            // five (7)
            if (cc.exists(t -> t._2 == 3))
                return 7;

            // four (6)
            if (cc.exists(t -> t._2 == 2))
                return 6;

            return 4;
        }

        // 2 cards, 3 jokers
        static int type3(Map<Character,Integer> cc) {

            // five (7)
            if (cc.exists(t -> t._2 == 2))
                return 7;

            return 6;
        }

        int compareSameType(Hand h, List<Character> values) {
            for (int i = 0; i < 5; ++i) {
                var c0 = cards.get(i);
                var c1 = h.cards.get(i);
                var v = values.indexOf(c0) - values.indexOf(c1);
                if (v < 0)
                    return -1;
                if (v > 0)
                    return 1;
            }
            return 0;
        }

        public String toString() {
            return cards.mkString();
        }
    }

    void part1() {
        Comparator<Hand> c2 = (h1,h2) -> h1.compareSameType(h2, VALUES_1);
        Comparator<Hand> c = Comparator.comparing(Hand::typePart1).thenComparing(c2);
        var hands = data.map(Hand::parse).sorted(c);
        var total = List.range(0, hands.length()).map(i -> (i + 1) * hands.get(i).bid);
        System.out.println(total.sum());
    }
    
    void part2() {
        Comparator<Hand> c2 = (h1,h2) -> h1.compareSameType(h2, VALUES_2);
        Comparator<Hand> c = Comparator.comparing(Hand::typePart2).thenComparing(c2);
        var hands = data.map(Hand::parse).sorted(c);
        var total = List.range(0, hands.length()).map(i -> (i + 1) * hands.get(i).bid);
        System.out.println(total.sum());
    }


    static String example = """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
            """;
}
