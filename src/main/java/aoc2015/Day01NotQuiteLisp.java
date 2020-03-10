package aoc2015;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class Day01NotQuiteLisp {

    public static void main(String[] args) throws IOException {

        List<String> l = Files.readAllLines(Paths.get("src/main/java/aoc2015/day01.txt"));
        String input = l.get(0);

        System.out.println("=== part 1");

        IntUnaryOperator value = c -> (c == '(') ? 1 : -1;
        int floor = input.chars().map(value).sum();

        System.out.println(floor);

        System.out.println("=== part 2");

        // stream approach (ugly)

        IntUnaryOperator sumto = n -> input.chars().limit(n).map(value).sum();
        OptionalInt r = IntStream.range(0, input.length()).filter(n -> sumto.applyAsInt(n) < 0).findFirst();
        System.out.println(r);

        // loop approach (ugly)

        int i = 0;
        for (int f = 0; i < input.length() && f >= 0; f += value.applyAsInt(input.charAt(i++)))
            ;
        System.out.println(i);

        // loop approach

        floor = 0;
        int pos = 0;
        while (floor >= 0) {
            floor += value.applyAsInt(input.charAt(pos));
            pos++;
        }
        System.out.println(pos);

    }
}
