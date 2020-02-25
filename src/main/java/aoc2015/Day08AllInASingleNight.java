package aoc2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day08AllInASingleNight {

	static String[] input0 = {
		"London to Dublin = 464", 
	"London to Belfast = 518", 
	"Dublin to Belfast = 141" 
	};

	static String[] input1 = {
	"AlphaCentauri to Snowdin = 66", 
	"AlphaCentauri to Tambi = 28", 
	"AlphaCentauri to Faerun = 60", 
	"AlphaCentauri to Norrath = 34", 
	"AlphaCentauri to Straylight = 34", 
	"AlphaCentauri to Tristram = 3", 
	"AlphaCentauri to Arbre = 108", 
	"Snowdin to Tambi = 22", 
	"Snowdin to Faerun = 12", 
	"Snowdin to Norrath = 91", 
	"Snowdin to Straylight = 121", 
	"Snowdin to Tristram = 111", 
	"Snowdin to Arbre = 71", 
	"Tambi to Faerun = 39", 
	"Tambi to Norrath = 113", 
	"Tambi to Straylight = 130", 
	"Tambi to Tristram = 35", 
	"Tambi to Arbre = 40", 
	"Faerun to Norrath = 63", 
	"Faerun to Straylight = 21", 
	"Faerun to Tristram = 57", 
	"Faerun to Arbre = 83", 
	"Norrath to Straylight = 9", 
	"Norrath to Tristram = 50", 
	"Norrath to Arbre = 60", 
	"Straylight to Tristram = 27", 
	"Straylight to Arbre = 81", 
	"Tristram to Arbre = 90"
	};
	
	static class Dist {
		public String from;
		public String to;
		public int dist;

		Dist(String s) {
			from = s.split(" = ")[0].split(" to ")[0];
			to = s.split(" = ")[0].split(" to ")[1];
			dist = Integer.parseInt(s.split(" = ")[1]);
		}
		
		int value(String c1, String c2) {
			if ((c1.equals(from) && c2.equals(to)) || (c1.equals(to) && c2.equals(from))) {
				return dist;
			} else {
				return -10000;
			}
		}
	}
	
	static int bestDist = 0;
	static List<String> bestRoute = null;
	
	
	public static void main(String[] args) {
		List<Dist> distances = Arrays.stream(input1).map(Dist::new).collect(Collectors.toList());
		Set<String> cities = distances.stream().flatMap(d -> Stream.of(d.from, d.to)).collect(Collectors.toSet());
		
		List<String> route = new ArrayList<String>();
		//route.add(cities.stream().findFirst().get());
		
		bestRoute(route, cities, distances);
		System.out.println(bestDist);
		System.out.println(bestRoute);
	}

	private static void bestRoute(List<String> route, Set<String> cities, List<Dist> distances) {
		if (route.size() == cities.size()) {
			checkRoute(route, distances);
		} else {
			for (String c: cities) {
				if (!route.contains(c)) {
					List<String> r2 = new ArrayList<String>(route);
					r2.add(c);
					bestRoute(r2, cities, distances);
				}
			}
		}
	}

	private static void checkRoute(List<String> route, List<Dist> distances) {
		int dist = 0;
		for (int i=0; i<route.size()-1; ++i) {
			final int n = i;
			dist += distances.stream().mapToInt(d -> d.value(route.get(n), route.get(n+1))).max().getAsInt();
		}
		if (dist > bestDist) {
			bestDist = dist;
			bestRoute = route;
		}
	}
	
}
