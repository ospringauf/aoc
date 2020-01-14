# part 1

mono(A) = all(i -> A[i]<=A[i+1], [1:5;])

repeated(A) = any(i -> A[i]==A[i+1], [1:5;])

function passwd1(n) 
    A = reverse(digits(n))
    mono(A) && repeated(A)
end    

@time length(filter(passwd1, [156218:652527;]))

# part 2

# pair(a) = any(i -> count(x -> x==a[i], a) == 2, [1:6;])
pair(A) = 2 ∈ map(d -> count(x -> x==d, A), A)

passwd2(n) = ( A = reverse(digits(n)) ; mono(A) && pair(A) )

@time [156218:652527;] |> n -> passwd2.(n) |> count


