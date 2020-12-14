package aoc2020;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import common.AocPuzzle;
import io.vavr.collection.List;

// --- Day 14: Docking Data ---
// https://adventofcode.com/2020/day/14

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day14 extends AocPuzzle {

    public static void main(String[] args) {

        System.out.println("=== test"); 
        new Day14().test();

        System.out.println("=== part 1"); // 13476250121721
        new Day14().part1();

        System.out.println("=== part 2"); // 4463708436768
        new Day14().part2();
    }

    void test() {
        // part 1 
        assertEquals(73, maskValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", 11));
        assertEquals(101, maskValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", 101));
        assertEquals(64, maskValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X", 0));
        
        // part 2
        var cb1 = maskAdr("000000000000000000000000000000X1001X", 42L);
        assertEquals("000000000000000000000000000000X1101X", cb1);
        assertEquals(List.of(26L, 27L, 58L, 59L).sorted(), resolveAdr(cb1).sorted());
        
        var cb2 = maskAdr("00000000000000000000000000000000X0XX", 26L);
        assertEquals("00000000000000000000000000000001X0XX", cb2);
        assertEquals(List.of(16l,17l,18l,19l,24l,25l,26l,27l).sorted(), resolveAdr(cb2).sorted());
        
        System.out.println("passed");
    }

    List<String> data = lines("input14.txt");

    
    void part1() {
        Map<Long, Long> mem = new HashMap<>();
        String mask = null;

        for (var s : data) {
            if (s.startsWith("mask"))
                mask = s.substring(7);

            if (s.startsWith("mem")) {
                var sa = s.substring(4).split("\\] = ");
                long adr = Long.parseLong(sa[0]);
                long value = Long.parseLong(sa[1]);
                value = maskValue(mask, value);

                mem.put(adr, value);
            }
        }

        var r = List.ofAll(mem.values()).sum().longValue();
        System.out.println("r = " + r);
        
    }
    
    void part2() {
        Map<Long, Long> mem = new HashMap<>();
        String mask = null;

        for (var s : data) {
            if (s.startsWith("mask"))
                mask = s.substring(7);

            if (s.startsWith("mem")) {
                var sa = s.substring(4).split("\\] = ");
                long adr = Long.parseLong(sa[0]);
                long value = Long.parseLong(sa[1]);

                resolveAdr(maskAdr(mask, adr)).forEach(a -> mem.put(a, value));
            }
        }

        var r = List.ofAll(mem.values()).sum().longValue();
        System.out.println("r = " + r);

    }

    long maskValue0(String m, long value) {
        long r = 0;
        for (int i=0; i<36; ++i) {
            long x = switch(m.charAt(35-i)) {            
            case 'X' -> value & (1L << i);
            case '1' -> 1L << i;
            default -> 0L;
            };
            r = r|x;
        }
        return r;
    }

    long maskValue(String m, long value) {
        var m0 = Long.parseLong(m.replaceAll("X", "0"), 2);
        var m1 = Long.parseLong(m.replaceAll("X", "1"), 2);
        
        return value&m1 | ~value&m0;  
    }

    String maskAdr(String mask, long adr) {
        String m = "";
        for (int i = 0; i < 36; ++i) {
            var mi = mask.charAt(35 - i);
    
            Character c = switch (mi) {
            case '0' -> (adr & (1L << i)) > 0 ? '1' : '0';
            default -> mi;
            };
            m = c + m;
        }
    
        return m;
    }

    List<Long> resolveAdr(String mask) {
        if (mask.contains("X")) {
            var a0 = resolveAdr(mask.replaceFirst("X", "0"));
            var a1 = resolveAdr(mask.replaceFirst("X", "1"));
            return a0.appendAll(a1);
        } else {
            return List.of(Long.parseLong(mask, 2));
        }
    }

    static String example = """
            mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
            mem[8] = 11
            mem[7] = 101
            mem[8] = 0
            """;

}
