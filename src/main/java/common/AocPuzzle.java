package common;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.vavr.collection.List;
import io.vavr.control.Try;

public class AocPuzzle {
	
	static long t0 = System.currentTimeMillis();
	
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
	
	public static void resetStopwatch() {
		t0 = System.currentTimeMillis();
	}
	
	public static void printLapTime() {
		var t1 = System.currentTimeMillis();
		System.out.format("--- laptime: %d ms ---\n", t1 - t0);
		t0 = t1;
	}
	
}
