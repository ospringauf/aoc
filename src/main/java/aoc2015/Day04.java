package aoc2015;

import java.security.MessageDigest;

import aoc2017.Util;

public class Day04 {

	public static void main(String[] args) throws Exception {
		String key = "yzbqklnj";
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		int i=0;
		String myHash;
		do {
			i++;
			md.update((key + Integer.toString(i)).getBytes());
			//myHash = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
			myHash = Util.encodeHexString(md.digest()).toUpperCase();
		} while (!myHash.startsWith("000000"));
		
		System.out.println(i);
		System.out.println(myHash);
	}
}
