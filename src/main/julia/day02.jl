using Printf

prog = map(x->parse(Int, x), split(readlines("input02.txt")[1], ","))

mutable struct IntComputer
    iptr::Int
    mem::Array{Int64}
end

c = IntComputer(0, prog)
#c.mem = append!(c.mem, [0:1000])

