package aoc2015;

/**
 * Look and Say 
 * https://adventofcode.com/2015/day/10
 */
public class Day10ElvesLookElvesSay {

	public static void main(String[] args) {

		System.out.println(lookSay("1", 0));
		System.out.println(lookSay("1", 1));
		System.out.println(lookSay("1", 2));
		System.out.println(lookSay("1", 3));
		System.out.println(lookSay("1", 4));
		System.out.println(lookSay("1", 5));
		System.out.println(lookSay("1", 6));
		System.out.println(lookSay("1", 7));

		System.out.println(lookSay("1113122113", 40).length());
		System.out.println(lookSay("1113122113", 50).length());

//		311311222113
//		13211321322113
//		1113122113121113222113
//		360154
//		5103798		
	}

	private static String lookSay(String s, int ntimes) {
		for (int k = 0; k < ntimes; ++k) {
			StringBuffer b = new StringBuffer();

			int i = 0;
			while (i < s.length()) {
				int j = i;
				while (j < s.length() && s.charAt(i) == s.charAt(j)) j++;
				b.append(j - i);
				b.append(s.charAt(i));
				i = j;
			}

			s = b.toString();
		}
		return s;
	}
}
