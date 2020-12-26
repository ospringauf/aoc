package common;

import java.util.function.Function;

import io.vavr.collection.HashSet;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;

/**
 * Bron-Kerbosch algorithm to find max clique
 * 
 * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
 * https://todd.ginsberg.com/post/advent-of-code/2018/day23/
 *
 */
public class BronKerbosch<T> {

	public Function<T, Set<T>> neighbors;
	
	Set<T> bestR = HashSet.empty();
	Set<Set<T>> allBest = HashSet.empty();
	
	public Set<T> largestClique(Seq<T> nodes) {
		execute(nodes.toSet(), HashSet.empty(), HashSet.empty());
		
		return bestR;
	}

	void execute(Set<T> p, Set<T> r, Set<T> x) {
		
		if (p.isEmpty() && x.isEmpty()) {
			if (r.size() > bestR.size()) {
				bestR = r;
				allBest = HashSet.of(r);
			} else if (r.size() == bestR.size()) {
				allBest = allBest.add(r);
			}
		} else {
			T pivot = p.addAll(x).maxBy(it -> neighbors.apply(it).size()).get(); //!!
			Set<T> pWithoutNeighbors = p.removeAll(neighbors.apply(pivot));
			
			for (var v : pWithoutNeighbors) {
				var neighborsOfV = neighbors.apply(v);
				execute(p.intersect(neighborsOfV), r.add(v), x.intersect(neighborsOfV));
				p = p.remove(v);
				x = x.add(v);
			}
			
		}
		
	}
	
}
