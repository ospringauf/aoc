# https://adventofcode.com/2019/day/22
println("--- Day 22: Slam Shuffle ---")

# idea:
# each shuffle is an index transformation i -> ai + b %L 
# that can be written as the matrix T=[ a b; 0 1 ] so that T*[i;1] is the index transformation.
# Multiple shuffles can be written as the product of the corresponding matrices.

rules = readlines("input22.txt")
L = 10007

# transformation matrix for each shuffle
function parserule(s) 
    if startswith(s, "cut")
        # i -> i - c
        c = parse(Int, s[4:end])
        [1 -c; 0 1]
    elseif startswith(s, "deal into")
        # i -> (L-1)-i 
        [-1 L-1; 0 1]        
    else
        # i -> (c * i) % L
        c = parse(Int, s[length("deal with increment "):end])
        [c 0; 0 1]
    end
end

# A B C = C * (B * A) 
combine(A,B) = (B*A) .% L

T = foldl(combine, parserule.(rules))

@show part1 = first(T * [2019; 1]) % L





# part 2
# idea: transform the final index (2020) backwards by
# applying the inverse index transformation multiple times.

L = 119315717514047
n = 101741582076661


# powermod for a matrix
function matpowermod(A::Array, b, m)::Array
    if b==1
        A
    else
        P = matpowermod(A, b÷2, m) .% m
        (b%2 == 0) ? (P*P).%m : ((P*P.%m)*A).%m
    end
end

# inverse transformation
function parseruleInverse(s)::Array{Int128,2} 
    if startswith(s, "cut")
        # i -> i - c  --  inverse: i -> i + c
        c = parse(Int, s[4:end])
        [1 c; 0 1]
    elseif startswith(s, "deal into")
        # i -> (L-1)-i  --  is its own inverse
        [-1 L-1; 0 1]        
    else
        # i -> (c * i) % L  -- what is the inverse ("i -> 1/c * i")?
        # This is the tricky part. Fermat says that 
        #   c^(L-1) = c*c^(L-2) = 1 mod L, 
        # so c^(L-2) is the inverse that we need
        c = parse(Int, s[length("deal with increment "):end])
        f = powermod(c, L-2, L)
        [f 0; 0 1]
    end
end

# reverse 100 shuffle operations
T = foldl(combine, reverse(parseruleInverse.(rules)))
# do the whole thing n times
T = matpowermod(T, n, L)

@show part2 = first(T*[2020; 1]) .%L 
