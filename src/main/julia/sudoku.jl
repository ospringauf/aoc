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

puzzle[4:6,4:6]

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

numInRow(P, row) = filter(n->n != 0, P[row,:])
numInCol(P, col) = filter(n->n != 0, P[:,col])

numInRow(puzzle, 2)

@test numInRow(puzzle, 2) == [6, 1 ,9, 5]
@test numInCol(puzzle, 2) == [3, 9, 6]


function area(P, row, col)
    start(n) = 1 + 3 * ((n - 1) ÷ 3)
    P[start(row):start(row)+2, start(col):start(col)+2]
end

area(puzzle, 3, 4)

numInArea(P, row, col) = filter(n->n != 0, area(P, row, col)) 

@test Set(numInArea(puzzle, 2, 2)) == Set([5,3,6,9,8])

function validNums(P, row, col)
    setdiff([1:9;], numInRow(P,row) ∪ numInCol(P,col) ∪ numInArea(P,row, col))
end

validNums(puzzle, 1, 1)
validNums(puzzle,1,6)

@test validNums(puzzle, 2, 2) == [2, 4, 7]
@test validNums(puzzle, 1, 6) == [2, 4, 6, 8]



function solve(P)
    R = copy(P)
    while 0 ∈ R
        unknown = [(row,col) for row=1:9 for col=1:9 if R[row,col]==0] 
        found = false
        for (row,col) = unknown
            cand = validNums(R, row, col)
            if (length(cand) == 1)
                # println("solve ", x, "/", y, " = ", cand)
                R[row,col] = cand[1]
                found = true
            end
        end
        if !found
            return nothing
        end
    end
    R
end

function solveSmart(P)
    R = copy(P)
    while 0 ∈ R 
        unknown = [(row,col) for row=1:9 for col=1:9 if R[row,col]==0] 
        found = false
        for (row,col) = unknown
            cand = validNums(R, row, col)
            if (length(cand) == 1)
                # println("solve ", x, "/", y, " = ", cand)
                R[row,col] = cand[1]
                found = true
            end
        end
        if !found
            (row,col) = unknown[1]
            cand = validNums(R, row, col)
            for n=cand
                R[row,col] = n
                r = solveSmart(R)
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

@time p = solveSmart(puzzle2)

@time p = solveSmart(puzzle3)

@testset "solution valid" begin
    n = 9
    s = sum(1:n;)
    @test all(sum(numInCol(p,i)) == s for i=1:n)
    @test all(sum(numInRow(p,i)) == s for i=1:n)
    @test all(length(Set(p[:,i])) == n for i=1:n)
    @test all(length(Set(p[i,:])) == n for i=1:n)
end
