package aoc2018;

import static org.jooq.lambda.Seq.rangeClosed;
import static org.jooq.lambda.Seq.seq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.lambda.Seq;


/**
 * regex labyrinth & shortest paths 
 * https://adventofcode.com/2018/day/20
 */
public class Day20 {
	
	static class Room {
		int x, y;
		
		Room(int x0, int y0) {
			x = x0;
			y = y0;
		}
		
		// next room to the E/W/S/N
		Room go(char dir) {
			Room p = null;
			switch (dir) {
				case 'W': p = new Room(x-1, y); break;
				case 'E': p = new Room(x+1, y); break;
				case 'N': p = new Room(x, y-1); break;
				case 'S': p = new Room(x, y+1); break;
			}
			doors.add(new Door(this, p));
			return p;
		}
		
		@Override
		public int hashCode() {
			return (y+1000)*10000 + (x+1000);
		}
		
		@Override
		public boolean equals(Object obj) {
			Room p = (Room)obj;
			return x==p.x && y==p.y;
		}
		
		@Override
		public String toString() {
			return "(" + x +"," + y + ")";
		}
	}
	
	static class Door {
		Room p1, p2;
		
		Door(Room a, Room b) {
			if (a.hashCode() < b.hashCode()) {
				p1 = a;
				p2 = b;
			} else {
				p1 = b;
				p2 = a;
			}
		}
		
		@Override
		public int hashCode() {
			return Math.min(p1.hashCode(), p2.hashCode());
		}
		
		@Override
		public boolean equals(Object obj) {
			Door d = (Door)obj;
			return (p1.equals(d.p1) && p2.equals(d.p2)); 
		}
	}
	
	// found doors
	static Set<Door> doors = new HashSet<>();
	
	// initial regex
	static String regex = "^WSSEESWWWNW(S|NENNEEEENN(ESSSSW(NWSW|SSEN)|WSWWN(E|WWS(E|SS))))$";
	
	
	public static void main(String[] args) throws Exception {
		regex = Util.lines("aoc2018/day20.txt").get(0);
		walk(Seq.of(new Room(0, 0)).toSet());
		
		System.out.println("doors: " + doors.size());
//		print();
		
		Map<Room, Integer> dist = shortestPaths();
		
		System.out.println("=== part 1 ===");
		Integer result1 = seq(dist.values()).max().get();
		System.out.println(result1);
		
		System.out.println("=== part 2 ===");
		long result2 = seq(dist).count(kvp -> kvp.v2 >= 1000);
		System.out.println(result2);
	}
	
	static void print() {
		Set<Room> rooms = seq(doors).flatMap(d -> Seq.of(d.p1, d.p2)).toSet();
		int xmin = seq(rooms).min(p -> p.x).get();
		int xmax = seq(rooms).max(p -> p.x).get()+1;
		int ymin = seq(rooms).min(p -> p.y).get();
		int ymax = seq(rooms).max(p -> p.y).get()+1;
		
		rangeClosed(ymin, ymax).forEach(y -> {
			// doors
			rangeClosed(xmin, xmax).forEach(x -> {
				boolean d = doors.contains(new Door(new Room(x,y), new Room(x,y-1)));
				System.out.print("#" + (d? "-" : "#"));
			});
			System.out.println();
			// rooms & doors
			rangeClosed(xmin, xmax).forEach(x -> {
				boolean d = doors.contains(new Door(new Room(x,y), new Room(x-1,y)));
				System.out.print((d? "|" : "#") + (x==0&&y==0 ? "X" :"."));
			});
			System.out.println();
		});
		System.out.println();
	}
	
	// next rooms reachable through a door
	static Seq<Room> nextRooms(Room p) {
		return seq(doors).filter(d -> d.p1.equals(p)).map(d -> d.p2)
				.concat(seq(doors).filter(d -> d.p2.equals(p)).map(d -> d.p1));
	}
	
	// process the regex, walking from initial set of rooms to 
	private static Set<Room> walk(Set<Room> initial) {
		Set<Room> branch = new HashSet<>(initial);
		Set<Room> alternatives = new HashSet<>();
		
		while (regex.length() > 0) {
			char dir = regex.charAt(0);
			switch (dir) {
				case 'E':
				case 'W':
				case 'N':
				case 'S':
					branch = seq(branch).map(p -> p.go(dir)).toSet();
					break;
				case '|':
					// next branch alternative
					alternatives.addAll(branch); 
					branch = new HashSet<>(initial);
					break;
				case '(':
					regex = regex.substring(1);
					branch = walk(branch);
					break;
				case ')':
					alternatives.addAll(branch);
					return alternatives;
				case '$':				
					return branch;
			}
			regex = regex.substring(1);
		}
		return branch;
	}
	
	// distance map for each room (shortest path) 
	static Map<Room, Integer> shortestPaths() {
		Map<Room, Integer> dist = new HashMap<>();

		List<Room> head = Seq.of(new Room(0, 0)).toList();
		dist.put(head.get(0), 0);		
		
		while (!head.isEmpty()) {
			List<Room> next = new ArrayList<>();
			for (Room r : head) {
				int d = dist.get(r);
				for (Room n : nextRooms(r)) {
					if (!dist.containsKey(n) || (dist.get(n) > (d+1))) {
						dist.put(n, d+1);
						next.add(n);
					}
				}				
			}
			head = next;
		}
		
		return dist;
	}
}
