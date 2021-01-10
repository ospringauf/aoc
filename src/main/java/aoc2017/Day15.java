package aoc2017;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jooq.lambda.Seq;

import common.AocPuzzle;

// --- Day 15: Dueling Generators ---
// https://adventofcode.com/2017/day/15

// with a comparison of "classic" and "lambda/streams" approach
// classic: fastest
// Java streams: don't have "zip", still quite fast (2nd)
// JooL streams: 3rd place
// Vavr streams: last, out of memory for the 40M stream!?!

@SuppressWarnings({ "deprecation", "preview", "serial" })
class Day15 extends AocPuzzle {

	static final long M = 2_147_483_647; // Integer.MAX_VALUE
	static final long FACTOR_A = 16807;
	static final long FACTOR_B = 48271;

	// Test: A=65, B=8921
	// input: A=722, B=354

	long next(long last, long fac) {
		return (last * fac) % M;
	}

	void part1(long startA, long startB) {

		var a = startA;
		var b = startB;
		var match = 0;
		for (int i = 0; i < 40_000_000; ++i) {
			if (judge(a, b))
				match++;
			a = next(a, FACTOR_A);
			b = next(b, FACTOR_B);
		}
		System.out.println(match);
	}

	boolean judge(long a, long b) {
		return (a & 0xFFFF) == (b & 0xFFFF);
	}

	void part2(long startA, long startB) {
		var a = startA;
		var b = startB;
		var match = 0;
		for (int i = 0; i < 5_000_000; ++i) {

			do
				a = next(a, FACTOR_A);
			while (a % 4 != 0);
			do
				b = next(b, FACTOR_B);
			while (b % 8 != 0);

			if (judge(a, b))
				match++;
		}
		System.out.println(match);
	}

	public static void main(String[] args) {

		System.out.println("=== test"); // 588, 309
		new Day15().testJavaStream();
		new Day15().testVavrStream();
		new Day15().testJoolStream();

		System.out.println();
		System.out.println("=== part 1"); // 612
		// new Day15().part1(65, 8921);
		timed(() -> new Day15().part1(722, 354));

		System.out.println("=== part 2"); // 285
//		new Day15().part2(65, 8921);
		timed(() -> new Day15().part2(722, 354));
	}

	void testJavaStream() {
		System.out.println("=== Java Streams");
		
		timed(() -> {
			Stream<Long> ga = Stream.iterate(65L, n -> next(n, FACTOR_A));
			Stream<Long> gb = Stream.iterate(8921L, n -> next(n, FACTOR_B));
			var match = zip(ga, gb, this::judge).limit(40_000_000).filter(j -> j).count();
			System.out.println(match);
		});
		
		
		timed(() -> {
			Stream<Long> ga = Stream.iterate(65L, n -> next(n, FACTOR_A)).filter(n -> n%4 == 0);
			Stream<Long> gb = Stream.iterate(8921L, n -> next(n, FACTOR_B)).filter(n -> n%8 == 0);
			var match = zip(ga, gb, this::judge).limit(5_000_000).filter(j -> j).count();
			System.out.println(match);
		});
	}
	
	void testVavrStream() {
		System.out.println("=== Vavr Streams");
		
		timed("vavr1", () -> {
			var ga = io.vavr.collection.Stream.iterate(65L, n -> next(n, FACTOR_A)).toStream();
			var gb = io.vavr.collection.Stream.iterate(8921L, n -> next(n, FACTOR_B)).toStream();
			
//			var match = ga.zipWith(gb, this::judge).take(40_000_000).count(j->j); // out-of-memory!!
//			System.out.println(match);
		});
		
		
		timed("vavr2", () -> {
			var ga = io.vavr.collection.Stream.iterate(65L, n -> next(n, FACTOR_A)).filter(n -> n%4 == 0);
			var gb = io.vavr.collection.Stream.iterate(8921L, n -> next(n, FACTOR_B)).filter(n -> n%8 == 0);

			var match = ga.zipWith(gb, this::judge).take(5_000_000).count(j->j);
			System.out.println(match);
		});
	}
	
	void testJoolStream() {
		System.out.println("=== JOOL Streams");
		
		timed(() -> {
			Seq<Long> ga = Seq.iterate(65L, n -> next(n, FACTOR_A));
			Seq<Long> gb = Seq.iterate(8921L, n -> next(n, FACTOR_B));
			
			var match = ga.zip(gb, this::judge).limit(40_000_000).count(j -> j);
			System.out.println(match);
		});
		
		timed(() -> {
			var ga = Seq.iterate(65L, n -> next(n, FACTOR_A)).filter(n -> n%4==0);
			var gb = Seq.iterate(8921L, n -> next(n, FACTOR_B)).filter(n -> n%8==0);
			
			var match = ga.zip(gb, this::judge).limit(5_000_000).count(j -> j);
			System.out.println(match);
		});
		
	}	

	// Java Streams don't have "zip"
	// from https://stackoverflow.com/questions/17640754/zipping-streams-using-jdk8-with-lambda-java-util-stream-streams-zip
	public static <A, B, C> Stream<C> zip(Stream<? extends A> a, Stream<? extends B> b,
			BiFunction<? super A, ? super B, ? extends C> zipper) {
		Objects.requireNonNull(zipper);
		Spliterator<? extends A> aSpliterator = Objects.requireNonNull(a).spliterator();
		Spliterator<? extends B> bSpliterator = Objects.requireNonNull(b).spliterator();

		// Zipping looses DISTINCT and SORTED characteristics
		int characteristics = aSpliterator.characteristics() & bSpliterator.characteristics()
				& ~(Spliterator.DISTINCT | Spliterator.SORTED);

		long zipSize = ((characteristics & Spliterator.SIZED) != 0)
				? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
				: -1;

		Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
		Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
		Iterator<C> cIterator = new Iterator<C>() {
			@Override
			public boolean hasNext() {
				return aIterator.hasNext() && bIterator.hasNext();
			}

			@Override
			public C next() {
				return zipper.apply(aIterator.next(), bIterator.next());
			}
		};

		Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
		return (a.isParallel() || b.isParallel()) ? StreamSupport.stream(split, true)
				: StreamSupport.stream(split, false);
	}
}
