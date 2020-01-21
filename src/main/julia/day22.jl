rules = readlines("input22.txt")
L = 10007

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

# A B C = C * ( B * A) 

combine(A,B) = (B*A) .% L

T = foldl(combine, parserule.(rules))

part1 = first(T * [2019; 1]) % L


# part 2

A=[2 1;0 1]
0*A

L = 119315717514047
n = 101741582076661

function powMod(x, N)::Int128
    if N==1
        return x
    end
    p = powMod(x, N÷2) % L
    (N%2==0) ? p*p % L : x*p*p % L
end

# powMod(2,L-2)

function parseruleInverse(s) 
    if startswith(s, "cut")
        # i -> i - c
        c = parse(Int, s[4:end])
        [1 c; 0 1]
    elseif startswith(s, "deal into")
        # i -> (L-1)-i 
        [-1 L-1; 0 1]        
    else
        # i -> (c * i) % L
        c = parse(Int, s[length("deal with increment "):end])
        f = powMod(c, L-2)
        [c 0; 0 1]
    end
end