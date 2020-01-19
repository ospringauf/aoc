include("intcomp.jl")
include("point.jl")

prog = map(x->parse(Int64, x), split(readlines("input11.txt")[1], ","))

robo = Pose()
hull = Dict{Point, Int}()
buf = []

function paintAndMove(color,dir)
    hull[robo.pos] = color
    global robo = forward(dir == 0 ? turnleft(robo) : turnright(robo))
end


c = IntComputer(prog)
c.input = () -> get(hull, robo.pos, 0)
c.output = bufferAndSplat(2, paintAndMove)
run!(c)

# print(hull, x->x)
part1 = length(hull)

# part 2

robo = Pose()
hull = Dict(robo.pos => 1)
buf = []

c = IntComputer(prog)
c.input = () -> get(hull, robo.pos, 0)
c.output = bufferAndSplat(2, paintAndMove)
run!(c)

print(hull, x->" #"[x+1])