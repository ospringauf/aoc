package aoc2018;

import java.util.Stack;

public class Day20 {
	
	static String re = "^ENWWW(NEEE|SSE(EE|N))W$";
//	static String re = "^ENNWSWW(NEWS|)SSSEEN(WNSE|)EE(SWEN|)NNN$";

	
	static class Pos {
		int x, y;
		Pos(int x0, int y0) {
			x = x0;
			y = y0;
		}
	}
	
	static Stack<Pos> branch = new Stack<>(); 
	
	public static void main(String[] args) {
		walk(1, "X");
	}

	private static void walk(int i, String w) {
		String p = w;
		while (true) {
			switch(re.charAt(i)) {
			case 'E':
			case 'W':
			case 'N':
			case 'S':
				p += re.charAt(i);
				System.out.println(p);
				break;
			case '|':
				p = w;
				break;
			case '(':
				walk(i+1, p);
				break;
			case ')':
			case '$':
				
				return;
			}
			i++;
		}
	}
	
}
