
package aoc2018;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3Claim {

	int id;
	int x, y, w, h;

	public Day3Claim(String s) {
		String rex = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)";

		Pattern pat = Pattern.compile(rex);
		Matcher m = pat.matcher(s);
		if (m.find()) {

			id = Integer.parseInt(m.group(1));
			x = Integer.parseInt(m.group(2));
			y = Integer.parseInt(m.group(3));
			w = Integer.parseInt(m.group(4));
			h = Integer.parseInt(m.group(5));
		}
	}
	
	public boolean contains(int px, int py) {
		return (px >= x) && (px < x+w) && (py >= y) && (py < y+h);
	}
	
	public boolean containEdge(Day3Claim c) {
		return contains(c.x, c.y) 
				|| contains(c.x+c.w-1, c.y) 
				|| contains(c.x, c.y+c.h-1)
				|| contains(c.x+c.w-1, c.y+c.h-1)
				;
	}
	
	public boolean overlap(Day3Claim c) {
		return containEdge(c) || c.containEdge(this);
	}

	
	@Override
	public String toString() {
		return String.format("#%d @ %d,%d: %dx%d", id, x, y, w, h);
	}

	@Override
	public boolean equals(Object obj) {		
		return id == ((Day3Claim)obj).id;
	}
	
}
