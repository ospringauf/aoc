package aoc2021;

import java.util.HashMap;

import common.AocPuzzle;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.collection.Map;

// --- Day 21: Dirac Dice ---
// https://adventofcode.com/2021/day/21

class Day21 extends AocPuzzle {

    // example
    // int pos1 = 4, pos2 = 8;

    // input
    int pos1 = 8, pos2 = 5;
    
    static int WINNING_SCORE = 1000;

    record DeterministicDie(int val, int rolls, int points) {
        DeterministicDie roll3() {
            int points = val;
            int v = val;
            v = ((++v - 1) % 100) + 1;
            points += v;
            v = ((++v - 1) % 100) + 1;
            points += v;
            v = ((++v - 1) % 100) + 1;
            return new DeterministicDie(v, rolls + 3, points);
        }
    }

    record Player(int score, int pos) {
        Player move(int points) {
            var p = (((pos - 1) + points) % 10) + 1;
            return new Player(score + p, p);
        }

        boolean won() {
            return score >= WINNING_SCORE;
        }
    }

    // represents a state of the game
    void part1() {
        WINNING_SCORE = 1000;
        var p1 = new Player(0, pos1);
        var p2 = new Player(0, pos2);

        var die = new DeterministicDie(1, 0, 0);

        while (!(p1.won() || p2.won())) {
            die = die.roll3();
            p1 = p1.move(die.points);

            if (p1.won())
                break;

            die = die.roll3();
            p2 = p2.move(die.points);
        }

        var r = Math.min(p1.score, p2.score) * die.rolls;
        System.out.println(r);
    }

    record Game(Player p1, Player p2) {
        boolean won() {
            return p1.won() || p2.won();
        }

        Game move(int player, int points) {
            return (player == 1) ? //
                    new Game(p1.move(points), p2) : //
                    new Game(p1, p2.move(points));
        }
    }

    // count the number of universes for each game state that has been reached so far
    class Multiverse extends HashMap<Game, Long> {
        void add(Game g, long count) {
            put(g, getOrDefault(g, 0L) + count);
        }

        boolean allWon() {
            return keySet().stream().allMatch(Game::won);
        }

        long countWins(Function1<Game, Player> who) {
            return entrySet().stream().filter(e -> who.apply(e.getKey()).won()).mapToLong(e -> e.getValue()).sum();
        }

        Multiverse nextMove(int player) {
            var next = new Multiverse();
            for (var game : keySet()) {
                if (game.won()) {
                    next.add(game, get(game));
                } else {
                    for (int points : dirac.keySet()) {
                        var nextGame = game.move(player, points);
                        next.add(nextGame, get(game) * dirac.get(points).get());
                    }
                }
            }
            return next;
        }
    }

    List<Integer> diracPoints = List.of(1, 2, 3).crossProduct(3).map(t -> t.sum().intValue()).toList();

    // map points -> number of universes with that score
    Map<Integer, Integer> dirac = diracPoints.toMap(x -> x, x -> diracPoints.count(v -> v == x));

    void part2() {
        WINNING_SCORE = 21;
        // System.out.println(dirac);

        var start = new Game(new Player(0, pos1), new Player(0, pos2));
        var games = new Multiverse();
        games.add(start, 1);

        while (!games.allWon()) {
            // System.out.println("turn");

            // Player 1
            games = games.nextMove(1);

            // Player 2
            games = games.nextMove(2);
        }

        var w1 = games.countWins(g -> g.p1);
        var w2 = games.countWins(g -> g.p2);

        System.out.println(Math.max(w1, w2));
    }

    public static void main(String[] args) {
        System.out.println("=== part 1"); // 597600
        new Day21().part1();

        System.out.println("=== part 2"); // 634769613696613
        timed(() -> new Day21().part2());
    }
}
