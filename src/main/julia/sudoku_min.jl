using Test 

puzzle = [5 3 0 0 7 0 0 0 0;
          6 0 0 1 9 5 0 0 0;
          0 9 8 0 0 0 0 6 0;
          8 0 0 0 6 0 0 0 3;
          4 0 0 8 0 3 0 0 1;
          7 0 0 0 2 0 0 0 6;
          0 6 0 0 0 0 2 8 0;
          0 0 0 4 1 9 0 0 5;
          0 0 0 0 8 0 0 7 9]

puzzle2 = [5 3 0 0 7 0 0 0 0;
          6 0 0 0 0 0 0 0 0;
          0 9 8 0 0 0 0 6 0;
          8 0 0 0 6 0 0 0 3;
          4 0 0 8 0 3 0 0 1;
          7 0 0 0 2 0 0 0 6;
          0 6 0 0 0 0 2 8 0;
          0 0 0 4 1 9 0 0 5;
          0 0 0 0 8 0 0 7 9]

puzzle3 = zeros(Int,9,9)


function block(P, row, col)
    idx(n) = 3 * ((n - 1) ÷ 3) + 1
    P[idx(row):idx(row)+2, idx(col):idx(col)+2]
end

function candidates(P, row, col)
    setdiff([1:9;], P[row,:] ∪ P[:,col] ∪ block(P,row, col))
end

candidates(puzzle, 1, 1)
candidates(puzzle,1,6)

@test candidates(puzzle, 2, 2) == [2, 4, 7]
@test candidates(puzzle, 1, 6) == [2, 4, 6, 8]



function solve(P)
    R = copy(P)
    while 0 ∈ R 
        unknown = [(row,col) for row=1:9 for col=1:9 if R[row,col]==0] 
        unique = false
        for (row,col) = unknown
            cand = candidates(R, row, col)
            if (length(cand) == 1)
                # println("solve ", x, "/", y, " = ", cand)
                R[row,col] = cand[1]
                unique = true
            end
        end

        # try different candidates at first ambiguous position
        if !unique
            (row,col) = unknown[1]
            cand = candidates(R, row, col)
            for n=cand
                R[row,col] = n
                r = solve(R)
                if r !== nothing
                    return r
                end
            end
            return nothing
        end
    end
    return R
end


p = puzzle

@time p = solve(puzzle)

@time p = solve(puzzle2)

@time p = solve(puzzle3)


@testset "solution valid" begin
    n = 9
    s = sum(1:n;)
    @test all(sum(p[:,i]) == s for i=1:n)
    @test all(sum(p[i,:]) == s for i=1:n)
    @test all(length(Set(p[:,i])) == n for i=1:n)
    @test all(length(Set(p[i,:])) == n for i=1:n)
end
