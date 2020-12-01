include("intcomp.jl")

prog = map(x->parse(Int64, x), split(readlines("input09.txt")[1], ","))

c = IntComputer(prog)
c.input = ()->2
c.output = println
@time run!(c)