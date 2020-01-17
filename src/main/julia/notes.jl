a = [1:15;]
a

a[ a .> 6 ]
a.>6

a[ a .% 2 .== 0]
a .% 2

@. a[a>6]
@. a[iszero(a%3)]

(x>3 for x in a)

b = [3,5,7,9]
a .∈ b
filter(x->!(x∈b), a)
a[ a .∈ b ]   # does not work?


min(4,5,9, Inf32)
