# https://github.com/JuliaMath/Combinatorics.jl
using Combinatorics

include("intcomp.jl")

prog = map(x->parse(Int64, x), split(readlines("input07.txt")[1], ","))

# part 1

function part1(settings)
    val = 0
    for i = 1:5
        data = [val, settings[i]]
        c = IntComputer(prog)
        c.output = x->val = x
        c.input = ()->pop!(data)
        run!(c)
    end
    return val
end

part1() = max(map(x->part1(x), permutations([0,1,2,3,4]))...)
# part1([1,3,2,4,0])

@time part1()

# part 2

function part2(settings)
    val = 0
    amp = [IntComputer(prog) for _ = 1:5]
    for i = 1:5
        c = amp[i]
        c.output = x -> val=x
        c.input = () -> settings[i]
        run1!(c)
        c.input = () -> val
    end
    i = 0
    while !halted(amp[5])
        runToOutput!(amp[i+1])
        i = (i+1)%5
    end
    return val
end

part2() = max(map(p->part2(p), permutations([5,6,7,8,9]))...)

@time part2()

# prog = [3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5]
# part2([9,8,7,6,5])
