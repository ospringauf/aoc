package aoc2019;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Util {

    static List<String> lines(String fname) throws IOException {
        return Files.readAllLines(Paths.get("src/main/java/aoc2019", fname));
    }
    
    static Stream<String> stringStreamOf(String fname) throws IOException {
        return Files.lines(Paths.get("src/main/java/aoc2019", fname));
    }

    static List<Integer> intListOf(String fname) throws IOException {
        return lines(fname).stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
    }
    
    static IntStream intStreamOf(String fname) throws IOException {
        return lines(fname).stream().mapToInt(s -> Integer.parseInt(s));
    }

    static List<String> splitLine(String s) {
        return Arrays.asList(s.split("\\s+"));
    }
}
