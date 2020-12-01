# part 1

# mono(A) = all(i -> A[i]<=A[i+1], [1:5;])
mono(A) = all(A[i] <= A[i + 1] for i = 1:5)

# repeated(A) = any(i -> A[i]==A[i+1], [1:5;])
repeated(A) = any(A[i] == A[i + 1] for i = 1:5)


function passwd1(n) 
    A = reverse(digits(n))
    mono(A) && repeated(A)
end    

@time count(passwd1(x) for x = 156218:652527)

# part 2

# pair(A) = 2 ∈ map(d->count(x->x == d, A), A)
pair(A) = 2 ∈ (count(x==d for x=A) for d=A)

passwd2(n) = ( A = reverse(digits(n)) ; mono(A) && pair(A) )

# @time [156218:652527;] |> n -> passwd2.(n) |> count
@time (passwd2(x) for x = 156218:652527) |> count


