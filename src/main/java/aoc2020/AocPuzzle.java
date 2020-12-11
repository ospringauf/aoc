package aoc2020;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.vavr.collection.List;
import io.vavr.control.Try;

public class AocPuzzle {
	
	private Path path(String fname) throws URISyntaxException {
		var path = Paths.get(getClass().getResource(fname).toURI());
		return path;
	}
	
	public List<String> lines(String fname)  {
		// turn checked exception into RuntimeException
		return Try.of(() -> List.ofAll(Files.lines(path(fname)))).get();
	}

	public String readString(String fname) {
		// turn checked exception into RuntimeException
		return Try.of(() -> Files.readString(path(fname))).get();
	}

	public List<Integer> ints(String fname) {
		return lines(fname).map(Integer::valueOf);
	}

	public List<Long> longs(String fname) {
		return lines(fname).map(Long::valueOf);
	}
//
//
//	static long[] readIntProg(String filename) {
//		try {
//			String l = lines(filename).head();
//			// return Arrays.stream(l.split(",")).mapToLong(Long::parseLong).toArray();
//			return string2longArray(l);
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
//
//	}	

}