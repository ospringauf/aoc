include("intcomp.jl")
include("point.jl")

prog = map(x->parse(Int64, x), split(readlines("input15.txt")[1], ","))

WALL = 0
CLEAR = 1
OXYGEN = 2

droid = Pose()
M = Dict{Point, Int}()
M[droid.pos] = CLEAR

function updateMapAndPose(output)
    # println("output ", val)
    if (output == WALL)
        M[ahead(droid)] = WALL
    elseif (output == CLEAR)        
        global droid = forward(droid)
        M[droid.pos] = CLEAR
    elseif (output == OXYGEN)       
        global foundOxygen=true        
        global droid = forward(droid)
        M[droid.pos] = OXYGEN
    end
end


function mapcomplete()
    # all neighbors of all waypoints have been explored
    neighborsMapped(p) = all(x->x ∈ keys(M), neighbors(p))
    waypoints = filter(p->M[p]==CLEAR, keys(M))
    all(neighborsMapped, waypoints)
end

function wander()        
    d = get(decisions, droid.pos, 0)

    # visit uncharted neighbors
    dir = findfirst(p->p ∉ keys(M), neighbors(droid.pos))
    if dir !== nothing
        dir=Int.([NORTH,EAST,SOUTH,WEST])[dir]
    else
        before(n) = (d & (1<<n)) != 0
        dir = Int(droid.dir)
        if before(dir)
            # new direction
            dir = findfirst([!before(x) for x=1:4])
            # no new directions -> random direction
            dir = (dir === nothing) ? rand([1:4;]) : dir
        end
    end

    decisions[droid.pos] = d | (1<<dir)
    global droid = Pose(droid.pos, Direction(dir))
    return dir
end

droid = Pose()
foundOxygen = false
M = Dict{Point, Int}()
M[droid.pos] = CLEAR
decisions = Dict{Point,Int}()

c = IntComputer(prog)
c.output = updateMapAndPose
c.input = wander

# find oxygen
@time while !foundOxygen run1!(c); end
oxygen = findfirst(x->x==OXYGEN,M)

# wander through the rest of the maze
@time while !mapcomplete() run1!(c); end

print(M, x->"#.o"[x+1])

# shortest paths from oxygen
D = minDistance(M, oxygen, p->M[p]!=WALL)

part1 = D[Point(0,0)]

part2 = maximum(values(D))
