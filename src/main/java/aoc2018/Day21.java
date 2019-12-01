package aoc2018;

import java.util.HashSet;
import java.util.Set;

public class Day21 {
	
	static long start = System.currentTimeMillis();

//	static void program(int x) {
//		int r0=x, r1=0, r2=0, r3=0, r4=0, r5=0; 
//		
//		do {
//		//	00	seti 123 0 3
//		r3 = 123;
//		//	01	bani 3 456 3
//		r3 = r3 & 456;
//		//	02	eqri 3 72 3		
//		//	03	addr 3 5 5
//		//	04	seti 0 0 5
//		} while (r3 != 72);
//		
//		//	05	seti 0 9 3
//		r3 = 0;
//		
//		//	06	bori 3 65536 1
//		r1 = r3 | 65536; // 1<<16
//		//	07	seti 9450265 6 3
//		r3 = 9450265;
//		
//		//	08	bani 1 255 4
//		r4 = r1 & 255; // lower 8 bits
//		//	09	addr 3 4 3
//		r3 += r4;
//		//	10	bani 3 16777215 3
//		r3 = r3 & 16777215; // lower 24 bits
//		//	11	muli 3 65899 3
//		r3 *= 65899;
//		//	12	bani 3 16777215 3
//		r3 = r3 & 16777215; // lower 24 bits
//		//	13	gtir 256 1 4
//		r4 = (256 > r1)? 1 : 0;
//		//	14	addr 4 5 5 
//		//	15	addi 5 1 5
//		//	16	seti 27 1 5
//		if (r1 < 256) goto 28
//		
//		//	17	seti 0 9 4
//		r4 = 0;
//		
//		//	18	addi 4 1 2
//		r2 = r4+1; 
//		//	19	muli 2 256 2
//		r2 *= 256; // r2<<8
//		//	20	gtrr 2 1 2
//		r2 = (r2>r1)? 1 : 0;
//		//	21	addr 2 5 5
//		//	22	addi 5 1 5
//		//	23	seti 25 7 5
//		if (r2>r1) goto 26
//		//	24	addi 4 1 4
//		r4++;
//		//	25	seti 17 5 5
//		goto 18
//		//	26	setr 4 6 1
//		r1 = r4;
//		//	27	seti 7 8 5
//		goto 8
//		
//		//	28	eqrr 3 0 4
//		r4 = (r3==r0)? 1 : 0;
//		//	29	addr 4 5 5
//		goto 31 exit
//		//	30	seti 5 8 5	
//		goto 6
//	}
	
	
//	static void program2(int x) {
//		int r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;
//
//		while (true) {
//			// 06 bori 3 65536 1
//			r1 = r3 | 65536; // 1<<16
//			// 07 seti 9450265 6 3
//			r3 = 9450265;
//
//			while (true) {
//				// 08 bani 1 255 4
//				// 09 addr 3 4 3
//				r3 += r1 & 255; // lower 8 bits
//				// 10 bani 3 16777215 3
//				r3 = r3 & 16777215; // lower 24 bits
//				// 11 muli 3 65899 3
//				r3 *= 65899;
//				// 12 bani 3 16777215 3
//				r3 = r3 & 16777215; // lower 24 bits
//
//				if (r1 < 256) {
//					if (r3 == x)
//						return;
//					break; // goto 6
//				}
//
//				// 17 seti 0 9 4
//				r4 = 0;
//
//				do {
//					r2 = (r4 + 1) << 8;
//					if (r2 > r1)
//						break;
//					r4++;
//				} while (true);
//
//				r1 = r4;
//
//			} // goto 8
//		}
//	}
	
	
	static int program3() {
		int r1 = 0, r3 = 0;
		
		while (true) {
			r1 = r3 | 65536; // 1<<16
			r3 = 9450265;

			while (true) {
				r3 += r1 & 255; // lower 8 bits
				r3 = ((r3 & 16777215) * 65899) & 16777215; // lower 24 bits  

				if (r1 < 256) {
					// first matching number --> least steps
					return r3;
				}

				r1 = r1 >> 8;
			} 
		}
	}
	
	
	static int program4() {
		int r1 = 0, r3 = 0;

		// remember produced numbers
		Set<Integer> seen = new HashSet<>();
		int last = 0;
		
		// 6
		while (true) {
			r1 = r3 | 65536; // 1<<16
			r3 = 9450265;

			// 8
			while (true) {
				r3 += r1 & 255; // lower 8 bits
				r3 = ((r3 & 16777215) * 65899) & 16777215; // lower 24 bits  

				if (r1 < 256) {
					// print last number that has not been produced before
					if (!seen.contains(r3)) {
						seen.add(r3);
						last = r3;
					} else {
						return last;
					}
					break; // goto 6
				}

				r1 = r1 >> 8;
			} // goto 8
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("=== part 1 ===");
		System.out.println(program3());
		
		System.out.println("=== part 2 ===");
		System.out.println(program4());
		
		System.out.println("time: " + (System.currentTimeMillis() - start + "ms"));

	}
}
