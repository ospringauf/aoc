package aoc2025;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.objective.ObjectiveStrategy;

import java.util.Arrays;

public class MinSumIntegerSolution {

    // Solve: minimize sum(x_i) subject to A x = b, x >= 0 (integers)
    public static Result solve(int[][] A, int[] b) {
        int m = A.length;
        int n = (m == 0) ? 0 : A[0].length;

        Model model = new Model("min-sum-integer-solution");
        // Simple upper bound: max(b); tighten per variable if known
        int ub = Arrays.stream(b).max().orElse(0);
        if (ub < 0) ub = 0; // ensure nonnegative UB
        IntVar[] x = new IntVar[n];
        for (int i = 0; i < n; i++) {
            x[i] = model.intVar("x_" + i, 0, ub);
        }

        // Constraints: A x = b
        for (int r = 0; r < m; r++) {
            IntVar rowSum = model.intVar("row_" + r, b[r], b[r]);
            // weighted sum: sum_j A[r][j] * x_j == b[r]
            model.scalar(x, A[r], "=", b[r]).post();
        }

        // Objective: minimize sum(x)
        IntVar sumX = model.intVar("sumX", 0, ub * n);
        model.sum(x, "=", sumX).post();
        model.setObjective(Model.MINIMIZE, sumX);

        Solver solver = model.getSolver();
        // Optional: search strategy to speed up
        // The method minimize(IntVar) is undefined for the type ObjectiveStrategy
//        solver.setSearch(ObjectiveStrategy.minimize(sumX));

        if (solver.solve()) {
            int[] xv = Arrays.stream(x).mapToInt(IntVar::getValue).toArray();
            return new Result(xv, sumX.getValue(), true);
        }
        return new Result(null, -1, false);
    }

    public static class Result {
        public final int[] x;
        public final int sum;
        public final boolean feasible;
        public Result(int[] x, int sum, boolean feasible) {
            this.x = x;
            this.sum = sum;
            this.feasible = feasible;
        }
    }

    public static void main(String[] args) {
        int[][] A = { 
                { 0, 0, 0, 0, 1, 1 }, 
                { 0, 1, 0, 0, 0, 1 }, 
                { 0, 0, 1, 1, 1, 0 },
                { 1, 1, 0, 1, 0, 0 }
                };

        int[] b = { 3, 5, 4, 7 };

        Result r = solve(A, b);
        if (r.feasible) {
            System.out.println("x = " + Arrays.toString(r.x));
            System.out.println("sum(x) = " + r.sum);
        } else {
            System.out.println("No feasible solution");
        }
    }
}