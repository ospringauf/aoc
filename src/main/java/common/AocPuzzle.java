package common;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Try;

public class AocPuzzle {
	
	static long t0 = System.currentTimeMillis();
	
	// ------------------------------------------------------------------------
	// input file ingest 
	
	private Path path(String fname) throws URISyntaxException {
		var path = Paths.get(getClass().getResource(fname).toURI());
		return path;
	}
	
	public List<String> file2lines(String fname)  {
		// turn checked exception into RuntimeException
		return Try.of(() -> List.ofAll(Files.lines(path(fname)))).get();
	}
	
	public String file2string(String fname) {
		// turn checked exception into RuntimeException
		return Try.of(() -> Files.readString(path(fname))).get();
	}

	public List<Integer> file2ints(String fname) {
		return file2lines(fname).map(Integer::valueOf);
	}

	public List<Long> file2longs(String fname) {
		return file2lines(fname).map(Long::valueOf);
	}
	
    // ------------------------------------------------------------------------
    // input splitting 
	
	public static record SplitResult(String[] fields) {

        public String s(int i) {
            return fields[i];
        }

        public int i(int i) {
            return Integer.valueOf(fields[i]);
        }
        
        public char c(int i) {
            return fields[i].charAt(0);
        }

        public <T> List<T> map(Function<String, T> f) {
        	return toList().map(f);
        }
        
        public List<SplitResult> split(String pat) {
            return map(s -> AocPuzzle.split(s, pat)); 
        }
        
        public List<String> toList() {
            return List.of(fields);
        }
        
        public <T> T to(Function1<SplitResult, T> f) {
        	return f.apply(this);
        }
    }
    
	public static SplitResult split(String s, String pat) {
        return new SplitResult(s.split(pat));
    }
    
    public static SplitResult lines(String s) {
        return new SplitResult(s.split("\n"));
    }

    public static Function1<String, SplitResult> split(String pat) {
        return s -> split(s, pat);
    }

    // ------------------------------------------------------------------------
    // timing 

	
	public static void resetStopwatch() {
		t0 = System.currentTimeMillis();
	}
	
	public static void printLapTime() {
		var t1 = System.currentTimeMillis();
		System.out.format("--- laptime: %d ms ---\n", t1 - t0);
		t0 = t1;
	}
	
	public static void timed(Runnable r) {
		timed("", r);
	}
	
	public static void timed(String tag, Runnable r) {
		var t0 = System.currentTimeMillis();
		r.run();
		var t1 = System.currentTimeMillis();
		System.out.println("--- " + tag + " time: " + (t1-t0) + " ms");
	}

}
