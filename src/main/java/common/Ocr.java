package common;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;

public class Ocr extends AocPuzzle {

    static Map<Integer, Character> bits = HashMap.empty();
    static final int W = 4;
    static final int H = 6;
    static final char PIXEL = '#';

    public static void main(String[] args) {
        var ocr = new Ocr();
        ocr.info();
        ocr.testPrint();
        ocr.testScan();
    }

    public Ocr() {
        train();
    }

    void testPrint() {

        System.out.println("=== test print ===");
        char c = 'K';
        System.out.println(c);
        var m = new PointMap<Character>();
        print(m, Point.of(0, 0), c);
        m.print();

        String s = "GURPFEL";
        System.out.println(s);
        m = new PointMap<Character>();
        print(m, Point.of(0, 0), s);
        m.print();
    }

    void testScan() {
        System.out.println("=== test scan ===");
        var data = file2string("ocr_test.txt");
        var samples = List.of(data.split("\n\n"));

        samples.forEach(s -> scanGrid(s.split("\n")));
    }

    public String scanGrid(String[] a) {
        var m = new PointMap<Character>();
        m.read(a, c -> c);
        m.print();

        var s = scanLine(m, 0, 0);
        System.out.println(s);
        return s;
    }

    public String scanLine(PointMap<Character> m, int xmin, int ymin) {
        var xmax = m.boundingBox().xMax();

        var chars = List.rangeBy(xmin, xmax, W + 1).map(x -> scanChar(m, Point.of(x, ymin)));
        return chars.mkString();
    }

    public char scanChar(PointMap<Character> m, Point p) {
        var v = scanBits(m, p);
        return bits.getOrElse(v, '?');
    }

    public void print(PointMap<Character> m, Point p, String s) {
        for (var c : s.toCharArray()) {
            print(m, p, c);
            p = p.translate(W + 1, 0);
        }
    }

    public void print(PointMap<Character> m, Point p, char c) {
        var v = bits.toMap(t -> t._2, t -> t._1).getOrElse(c, 0);
        for (int x = 0; x < W; ++x) {
            for (int y = 0; y < H; ++y) {
                var bit = v & (1 << (H * x + y));
                if (bit != 0)
                    m.put(p.translate(x, y), PIXEL);
            }
        }
    }

    void train() {
        var data = file2string("ocr_input.txt");
        var samples = List.of(data.split("\n\n"));

        samples.forEach(s -> train(s));
    }

    void info() {
        System.out.println("known characters: " + bits.values().toList().sorted().mkString());
    }

    void train(String s) {
        var lines = Util.splitLines(s);
        var chars = lines.get(0);
        var m = new PointMap<Character>();
        m.read(lines.drop(1));

        for (int i = 0; i < 8; i++) {
            train(m, Point.of(i * (W + 1), 0), chars.charAt(i));
        }
    }

    void train(PointMap<Character> m, Point s, char c) {
        var v = scanBits(m, s);
        bits = bits.put(v, c);
    }

    int scanBits(PointMap<Character> m, Point s) {
        var v = 0;
        for (int x = 0; x < W; ++x) {
            for (int y = 0; y < H; ++y) {
                if (m.getOrDefault(s.translate(x, y), '.') == PIXEL)
                    v = v | (1 << (H * x + y));
            }
        }
        return v;
    }

}
