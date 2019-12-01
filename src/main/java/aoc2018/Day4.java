package aoc2018;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 {
    static List<Day4Data> events;

	static class Day4Data {
		static int currentGuard;
		
		int guard;
        int day;
		int minute = -1;
		boolean awake;
		
		public Day4Data(int g, int d, int m) {
		    guard = g;
		    day = d;
		    minute = m;	    
		}
		
		public Day4Data(String s) {
            day = Integer.parseInt(s.substring(6, 8) + s.substring(9, 11));

            if (s.contains("Guard")) {
				Matcher m = Pattern.compile("#(\\d+) ").matcher(s);
				m.find();
				currentGuard = Integer.parseInt(m.group(1));
				minute = 0;
				int hour = Integer.parseInt(s.substring(12, 14));
				if (hour == 23)
				    day ++;
			}

			minute = (minute == -1) ? Integer.parseInt(s.substring(15, 17)) : minute;
			guard = currentGuard;
			awake = !s.contains("asleep");
		}
	}
	
	static class Sleep {
	    Sleep(int i, long d) {
	        id=i;
	        duration=d;
	    }
	    int id;
	    long duration;
	}
	

   private static boolean isAsleep(int guard, int day, int minute) {
        // find last "event" less/equal given minute --> awake/asleep
        return  events.stream()
                .filter(e -> e.day == day && e.guard == guard && e.minute <= minute)
                .reduce((a, b) -> b) // last element
                .map(e -> !e.awake).orElse(false);
    }

	public static void main(String[] args) throws Exception {
		part1();
	}

	private static void part1() throws Exception {
		 events = 
//				Util.lines("aoc2018/day4-test.txt")
		         Util.lines("aoc2018/day4.txt")
				.stream()
				.sorted()
				.map(Day4Data::new)
				.collect(Collectors.toList());
		 
		List<Integer> days = events.stream().map(e -> e.day).distinct().collect(Collectors.toList());
		List<Integer> guards = events.stream().map(e -> e.guard).distinct().collect(Collectors.toList());
		
		
		int xguard = guards.stream()
	            .map(g ->
	                    // sum sleep time of guard g over all days/minutes
	                    new Sleep(g, days
	                           .stream()
	                           .flatMapToInt(d -> IntStream.range(0, 60).filter(m -> isAsleep(g, d, m)))
	                           .count())
	                )
	            .peek(x -> System.out.println("guard " + x.id + " sleeps " + x.duration))
	            .sorted((a,b) -> (int)(b.duration - a.duration)) // descending by sleep time
	            .peek(x -> System.out.println("pick guard " + x.id))
	            .findFirst().get().id;
		
		// sleep 1993: 513

		
	      IntStream.range(0, 60)
	        .mapToObj(m -> new Sleep(m, days.stream().filter(d -> isAsleep(xguard, d, m)).count()))
	        .peek(x -> System.out.println("in minute " + x.id + " sleeps " + x.duration))
	        .sorted((s1, s2) -> (int)(s2.duration-s1.duration)) // descending by sleep time
	        .limit(1)
	        .forEach(x -> System.out.println("--> minute " + x.id + " ==> " + (xguard*x.id)))
	        ;

		// minute 36: 15
	}

	private static void part2() throws Exception {
        events = 
//              Util.lines("aoc2018/day4-test.txt")
                 Util.lines("aoc2018/day4.txt")
                .stream()
                .sorted()
                .map(x -> new Day4Data(x))
                .collect(Collectors.toList());
         
        List<Integer> days = events.stream().map(e -> e.day).distinct().collect(Collectors.toList());
        List<Integer> guards = events.stream().map(e -> e.guard).distinct().collect(Collectors.toList());
        
        List<Sleep> x = new ArrayList<Sleep>();
        
        for (int m=0; m<60; ++m) {
            final int min = m;
            for (int g : guards) {
                
                long sleeping = days.stream().filter(d -> isAsleep(g, d, min)).count();
                Sleep e = new Sleep(g*m, sleeping);
                //Day4Data e = new Day4Data(g, 0, m, (int) sleeping);
                x.add(e);
            }
        }
        
        x.sort((a,b) -> (int)(b.duration-a.duration));
        
        Sleep result = x.get(0);
        //System.out.println("guard: " + result.guard + " / min: " + result.minute + " --> " + result.guard*result.minute);
        System.out.println(result.id);
        
	}

}
