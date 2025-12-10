package aoc2025;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;

/**
 * Solves a system of linear equations of the form Ax = b
 * where A is a matrix of coefficients, x is the vector of unknowns,
 * and b is the vector of constants.
 * 
 * Supports both continuous and integer solutions.
 */
public class LinearEquationSolver {
    
    /**
     * Solves the system Ax = b for continuous (real) solutions.
     * 
     * @param A coefficient matrix (m x n)
     * @param b constant vector (m x 1)
     * @return solution vector x
     * @throws IllegalArgumentException if the system cannot be solved
     */
    public static double[] solve(int[][] A, int[] b) {
        return solveInternal(A, b, false);
    }
    
    /**
     * Solves the system Ax = b for integer solutions using Integer Linear Programming.
     * 
     * @param A coefficient matrix (m x n)
     * @param b constant vector (m x 1)
     * @return solution vector x (integer values)
     * @throws IllegalArgumentException if the system cannot be solved
     */
    public static int[] solveInteger(int[][] A, int[] b) {
        double[] solution = solveInternal(A, b, true);
        int[] intSolution = new int[solution.length];
        for (int i = 0; i < solution.length; i++) {
            intSolution[i] = (int) Math.round(solution[i]);
        }
        return intSolution;
    }
    
    private static double[] solveInternal(int[][] A, int[] b, boolean integerConstraints) {
        int numEquations = A.length;
        int numVariables = A[0].length;
        
        // Create optimization model
        ExpressionsBasedModel model = new ExpressionsBasedModel();
        
        // Create variables
        Variable[] variables = new Variable[numVariables];
        for (int i = 0; i < numVariables; i++) {
            variables[i] = model.addVariable("x" + i);
            if (integerConstraints) {
                variables[i].integer(true);
            }
        }
        
        // Add equality constraints for each equation
        for (int i = 0; i < numEquations; i++) {
            Expression constraint = model.addExpression("eq" + i);
            for (int j = 0; j < numVariables; j++) {
                constraint.set(variables[j], A[i][j]);
            }
            constraint.level(b[i]);
        }
        
        // Minimize arbitrary objective (we just want feasibility)
        Expression objective = model.addExpression("objective").weight(1);
        objective.set(variables[0], 0);
        
        // Solve
        Optimisation.Result result = model.minimise();
        
        if (!result.getState().isFeasible()) {
            throw new IllegalArgumentException("System has no solution or is infeasible");
        }
        
        // Extract solution
        double[] solution = new double[numVariables];
        for (int i = 0; i < numVariables; i++) {
            solution[i] = result.doubleValue(i);
        }
        
        return solution;
    }
    
    /**
     * Example usage demonstrating how to solve a system of linear equations.
     */
    public static void main(String[] args) {
        // Example 1: Solve the system with continuous solutions:
        // 2x + 3y - z = 1
        // 4x + y + 2z = 12
        // -x + 2y + 3z = 5
	
//        int[][] A = { 
//        		{ 0, 0, 0, 1 }, 
//        		{ 0, 1, 0, 1 }, 
//        		{ 0, 0, 1, 0 },
//    			{ 0, 0, 1, 1 }, 
//    			{ 1, 0, 1, 0 }, 
//    			{ 1, 1, 0, 0 } 
//    			};

        int[][] A = { 
        		{ 0, 0, 0, 0, 1, 1 }, 
        		{ 0, 1, 0, 0, 0, 1 }, 
        		{ 0, 0, 1, 1, 1, 0 },
    			{ 1, 1, 0, 1, 0, 0 }
    			};

        int[] b = { 3, 5, 4, 7 };
        
        try {
            System.out.println("=== Continuous Solution ===");
            double[] solution = solve(A, b);
            
            System.out.println("Solution:");
            for (int i = 0; i < solution.length; i++) {
                System.out.printf("x[%d] = %.4f%n", i, solution[i]);
            }
            
            // Verify the solution
            System.out.println("\nVerification:");
            for (int i = 0; i < A.length; i++) {
                double sum = 0;
                for (int j = 0; j < A[i].length; j++) {
                    sum += A[i][j] * solution[j];
                }
                System.out.printf("Equation %d: %.4f (expected: %d)%n", i + 1, sum, b[i]);
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
        
        // Example 2: Solve with integer constraints
        // 3x + 2y = 14
        // x + 4y = 13
        
        int[][] A2 = { 
        		{ 0, 0, 0, 0, 1, 1 }, 
        		{ 0, 1, 0, 0, 0, 1 }, 
        		{ 0, 0, 1, 1, 1, 0 },
    			{ 1, 1, 0, 1, 0, 0 }
    			};

        int[] b2 = { 3, 5, 4, 7 };
        
        try {
            System.out.println("\n=== Integer Solution ===");
            int[] intSolution = solveInteger(A2, b2);
            
            System.out.println("Solution:");
            for (int i = 0; i < intSolution.length; i++) {
                System.out.printf("x[%d] = %d%n", i, intSolution[i]);
            }
            
            // Verify the solution
            System.out.println("\nVerification:");
            for (int i = 0; i < A2.length; i++) {
                int sum = 0;
                for (int j = 0; j < A2[i].length; j++) {
                    sum += A2[i][j] * intSolution[j];
                }
                System.out.printf("Equation %d: %d (expected: %d)%n", i + 1, sum, b2[i]);
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
