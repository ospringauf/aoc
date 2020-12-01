using Printf

numbers = map(x->parse(Int, x), readlines("input01.txt"))

prod(sum, arr) = map(x -> x*(sum-x), filter(x -> (sum-x) in arr, arr))[1]

prod(2020, numbers)

filter(x -> prod(2020-x, numbers) in numbers, numbers)