# part 1

mono(a) = all(i -> a[i]<=a[i+1], [1:5;])

repeated(a) = any(i -> a[i]==a[i+1], [1:5;])

function passwd1(n) 
    a = reverse(digits(n))
    mono(a) && repeated(a)
end    

@time length(filter(passwd1, [156218:652527;]))

# part 2

# pair(a) = any(i -> count(x -> x==a[i], a) == 2, [1:6;])
pair(a) = 2 ∈ map(d -> count(x -> x==d, a), a)

passwd2(n) = ( a = reverse(digits(n)) ; mono(a) && pair(a) )

@time [156218:652527;] |> x->passwd2.(x) |> count


