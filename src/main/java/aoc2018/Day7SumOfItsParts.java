package aoc2018;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day7SumOfItsParts {

    public static void main(String[] args) throws Exception {

        List<String> lines = Files.readAllLines(Paths.get("src/main/java/aoc2018/day7.txt"));

        // Step F must be finished before step Z can begin.
        Function<String, Character[]> parseLine = l -> new Character[] { l.charAt(5), l.charAt(36) };

        List<Character[]> rules = lines.stream()
                                       .map(parseLine)
                                       .collect(Collectors.toList());

        Set<Character> todo = rules.stream()
                                   .flatMap(b -> Stream.of(b[0], b[1]))
                                   .collect(Collectors.toSet());

        List<Character> done = new ArrayList<Character>();

        while (!todo.isEmpty()) {

            Predicate<Character> cando = c -> rules.stream()
                                                   .filter(b -> b[1] == c)
                                                   .map(b -> b[0])
                                                   .allMatch(done::contains);
            Character next = todo.stream()
                                 .filter(cando)
                                 .sorted()
                                 .findFirst()
                                 .get();

            todo.remove(next);
            done.add(next);
        }

        System.out.println(done);
    }
}
