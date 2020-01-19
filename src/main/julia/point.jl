import Base.+
import Base.isequal
import Base.print

struct Point
    x::Int
    y::Int
end

hash(p::Point) = p.x + 10000*p.y
isequal(p1::Point, p2::Point) = (p1.x==p2.x) && (p1.y==p2.y)

(+)(p1::Point, p2::Point) = Point(p1.x+p2.x, p1.y+p2.y)

manhattan(x,y) = abs(x) + abs(y)
manhattan(p::Point) = manhattan(p.x, p.y)
manhattan(p1::Point, p2::Point) = manhattan(p2.x-p1.x, p2.y-p1.y)

north(p::Point) = Point(p.x, p.y-1)
south(p::Point) = Point(p.x, p.y+1)
east(p::Point) = Point(p.x+1, p.y)
west(p::Point) = Point(p.x-1, p.y)


# pose

@enum Direction NORTH=0 EAST=1 SOUTH=2 WEST=3

struct Pose
    pos::Point
    dir::Direction
    Pose() = new(Point(0,0), NORTH)
    Pose(p,d) = new(p,d)
end

function turnleft(p::Pose)
    r = Dict(NORTH=>WEST, WEST=>SOUTH, SOUTH=>EAST, EAST=>NORTH)
    Pose(p.pos, r[p.dir])
end

function turnright(p::Pose)
    r = Dict(NORTH=>EAST, EAST=>SOUTH, SOUTH=>WEST, WEST=>NORTH)
    Pose(p.pos, r[p.dir])
end

function forward(p::Pose)
    f = Dict(NORTH=>north, EAST=>east, SOUTH=>south, WEST=>west)
    Pose(f[p.dir](p.pos), p.dir)
end



# pointmap functions (Dict{Point,Any})

function pmscan(f::Function, A::Array{<:String})
    # m = Dict{Point,Any}()
    # for y=1:length(A)
    #     for x=1:length(A[y])
    #         m[Point(x-1,y-1)] = f(A[y][x])
    #     end
    # end
    # return m
    Dict(Point(x-1, y-1) => f(A[y][x]) for y = 1:length(A) for x = 1:length(A[y]))
end

function print(m::Dict, pixel::Function)
    k = collect(keys(m))
    xmin = min(map(p->p.x, k)...)
    xmax = max(map(p->p.x, k)...)
    ymin = min(map(p->p.y, k)...)
    ymax = max(map(p->p.y, k)...)
    for y = ymin:ymax
        for x = xmin:xmax
            p = Point(x, y)
            if haskey(m, p)
                print(pixel(m[p]))
            else
                print(' ')
            end
        end
        println()  
    end
end

