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

neighbors(p::Point) = [north(p), east(p), south(p), west(p)]


# pose

@enum Direction NORTH=1 EAST=4 SOUTH=2 WEST=3  # values from day15

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

function ahead(p::Point, d::Direction)::Point
    f = Dict(NORTH=>north, EAST=>east, SOUTH=>south, WEST=>west)
    f[d](p)
end

ahead(p::Pose)::Point = ahead(p.pos, p.dir)

function forward(p::Pose)
    Pose(ahead(p), p.dir)
end





# pointmap functions (Dict{Point,Any})

struct Bbox
    x::UnitRange
    y::UnitRange    
end

center(b::Bbox) = Point((b.x.start+b.x.stop) ÷ 2, (b.y.start+b.y.stop) ÷ 2 )

function pmbox(m::Dict)
    Bbox(
        minimum(p.x for p=keys(M)) : maximum(p.x for p=keys(M)),
        minimum(p.y for p=keys(M)) : maximum(p.y for p=keys(M))
     )    
end

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
    bb = pmbox(m)
    for y = bb.y
        for x = bb.x
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

print(m::Dict) = print(m, identity)

points(m::Dict) = keys(m)


function minDistance(M::Dict, p0::Point, allowed::Function, neigh::Function)
    dst = Dict{Point,Int}(p0 => 0)
    better = true
    allowedPoints = collect(filter(allowed, keys(M)))
    while better
        better = false
        for p = allowedPoints
            dp = get(dst, p, Inf)
            dn = minimum(n -> get(dst, n, Inf), neigh(p))
            if dp > (dn+1)
                dst[p]=dn+1
                better=true
            end
        end
    end
    return dst    
end

minDistance(M::Dict, p0::Point, allowed::Function) = minDistance(M, p0, allowed, neighbors)    
