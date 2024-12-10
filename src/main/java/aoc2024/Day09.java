package aoc2024;

import common.AocPuzzle;
import common.Util;
import io.vavr.collection.List;
import io.vavr.collection.Stream;

//--- Day 9: Disk Fragmenter ---
// https://adventofcode.com/2024/day/9

class Day09 extends AocPuzzle {

	public static void main(String[] args) {
		System.out.println("=== part 1"); // 6337921897505
		timed(() -> new Day09().part1());
		System.out.println("=== part 2"); // 6362722604045
		timed(() -> new Day09().part2());
	}

	String data = file2string("input09.txt");
//	String data = example;

	List<File> files;

	record File(int id, int size, int free) {
		int total() {
			return size + free;
		}

		long checksum(long pos) {
//			long r = 0;
//			for (int x = 0; x < size; ++x) {
//				r += id * (pos + x);
//			}
//			return r;

			return id * (((pos + pos + size - 1) * size) / 2);
		}

		@Override
		public final String toString() {
			return Integer.toString(id).repeat(size) + ".".repeat(free);
		}

		void write(Integer[] map, int pos) {
			for (int i = 0; i < size; ++i)
				map[pos + i] = id;
		}
	}

	Day09() {
		var dense = List.ofAll(data.toCharArray()).map(c -> c - '0').append(0);
		var ids = Stream.iterate(0, id -> id + 1);
		files = ids.zip(dense.sliding(2, 2)).map(t -> new File(t._1, t._2.get(0), t._2.get(1))).toList();
		System.out.println("found " + files.size() + " files");
	}

	void part1() {
		var diskSize = files.map(File::total).sum().intValue();
		var disk = new Integer[diskSize];

		if (diskSize < 100) {
			System.out.println(diskSize);
			System.out.println(files.map(File::toString).toList());
		}

		int pos = 0;
		for (var b : files) {
			b.write(disk, pos);
			pos += b.total();
		}

		if (diskSize < 100)
			print(disk, diskSize);

		int p1 = 0;
		int p2 = diskSize - 1;
		while (p1 < p2) {
			if (disk[p1] != null)
				p1++;
			else if (disk[p2] == null)
				p2--;
			else
				disk[p1++] = disk[p2--];
		}

		diskSize = p1 + 1;

		if (diskSize < 100)
			print(disk, diskSize);

		// calculate checksum
		long checksum = 0L;
		for (int i = 0; i < diskSize; ++i) {
			if (disk[i] != null)
				checksum += i * disk[i];
		}
		System.out.println(checksum);

	}

	void part2() {
		var diskSize = files.map(File::total).sum().intValue();
		var order = files.map(b -> b.id).sorted().reverse();

		// TODO this could be much faster on a real mutable linked list
		
		for (var moveId : order) {
			var im = files.indexWhere(f -> f.id == moveId);
			var move = files.get(im);
			
			int it = files.indexWhere(b -> b.free >= move.size);
			
			if (it >= 0 && it < im) {
				if (diskSize < 100)
					System.out.println("moving " + moveId);
				
				files = files.removeAt(im);
				
				var pred = files.get(im - 1);
				files = files.replace(pred, new File(pred.id, pred.size, pred.free + move.size + move.free));
				
				var target = files.get(it);
				files = files.replace(target, new File(target.id, target.size, 0));
				files = files.insert(it + 1, new File(move.id, move.size, target.free - move.size));
				
				if (diskSize < 100)
					System.out.println(files.map(File::toString).mkString());
			}
		}

		// calculate checksum
		long i = 0;
		long checksum = 0L;
		for (var b : files) {
			checksum += b.checksum(i);
			i += b.size + b.free;
		}

		System.out.println(checksum);
	}	

	void print(Integer[] m, int l) {
		var s = List.of(m).take(l).map(x -> x == null ? '.' : String.valueOf(x)).mkString();
		System.out.println(s);
	}

	static String example = "2333133121414131402";
}
