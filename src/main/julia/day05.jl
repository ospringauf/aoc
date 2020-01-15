include("intcomp.jl")

prog = map(x->parse(Int64, x), split(readlines("input05.txt")[1], ","))

# part 1

c = IntComputer(prog)
c.output = println
c.input = ()->1
@time run!(c)

# part 2

# c = IntComputer([3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31, 1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104, 999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99])
# c.output = println
# c.input = ()->9
# run(c)

c = IntComputer(prog)
c.output = println
c.input = ()->5
@time run!(c)

