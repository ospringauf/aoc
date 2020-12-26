package aoc2018.revisit;

import java.util.function.Function;

import common.AocPuzzle;
import io.vavr.Function2;
import io.vavr.collection.List;

// https://adventofcode.com/2018/day/4

@SuppressWarnings({ "deprecation", "preview" })
class Day04 extends AocPuzzle {

	static record Shift(int guard, List<Boolean> awake) {
		
		static Shift parse(String g, List<String> lines) {
			int guardNo = Integer.valueOf(g.split("#")[1].split(" ")[0]);
			var awake = true;
			var t = 0;
			List<Boolean> state = List.empty();
			for (var s : lines) {
				int minute = Integer.valueOf(s.split(":")[1].split("]")[0]);
				while (t < minute) {
					state = state.append(awake);
					t++;
				}
				awake = !awake;
			}
			while (t < 60) {
				state = state.append(awake);
				t++;
			}
//			System.out.print(g);
//			System.out.println(guardNo);
//			System.out.println(state.map(x -> x? ".":"#").mkString());
			return new Shift(guardNo, state);
		}
		
		boolean asleep(int m) {
			return ! awake.get(m);
		}
		
		int asleep() {
			return awake.count(a -> !a);
		}	
	}
	
	
	public static void main(String[] args) throws Exception {

		System.out.println("=== part 1"); // 71748
		new Day04().part1();

		System.out.println("=== part 2"); // 106850
		new Day04().part2();
	}

	private void part1() throws Exception {
		List<Shift> shifts = parseShifts();
		var guards = shifts.map(s -> s.guard).toSet();
		
		Function<Integer, Integer> sleeping = g -> shifts.filter(s -> s.guard==g).map(Shift::asleep).sum().intValue();
		
		var sleepy = guards.maxBy(sleeping).get();
		System.out.println(sleepy);

		var sleepyShifts = shifts.filter(s -> s.guard == sleepy);
		var minute = List.range(0, 60).maxBy(m -> sleepyShifts.count(s -> s.asleep(m))).get();
		System.out.println(minute);
		System.out.println(sleepy * minute);
		
	}

	private void part2() throws Exception {
		List<Shift> shifts = parseShifts();
		var guards = shifts.map(s -> s.guard).distinct();
		var minutes = List.range(0, 60);

		Function2<Integer, Integer, Integer> guardByMinute = (g,m) -> shifts.count(s -> s.guard==g && s.asleep(m));
		
		var best = guards.crossProduct(minutes).maxBy(guardByMinute.tupled()).get();
		
		System.out.println(best);
		System.out.println(best._1 * best._2);
		
	}
	
	
	List<Shift> parseShifts() throws Exception {		
		var lines = lines("../day4.txt").sorted();
//		var lines = List.of(example.split("\n")).sorted();

		List<Shift> guards = List.empty();
		while (!lines.isEmpty()) {
			var g = lines.head();
			var x = lines.tail().splitAt(s -> s.contains("Guard"));
			guards = guards.append(Shift.parse(g, x._1));
			lines = x._2;
		}
		return guards;
	}



	static String example = """
[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up
		"""; 
}
