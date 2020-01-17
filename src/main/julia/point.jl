struct Point <: Number
    x
    y    
end

hash(p::Point) = p.x + 10000*p.y
# isequal(p1::Point, p2::Point) = (p1.x==p2.x) && (p1.y==p2.y)

manhattan(x,y) = abs(x) + abs(y)
manhattan(p::Point) = manhattan(p.x, p.y)
manhattan(p1::Point, p2::Point) = manhattan(p2.x-p1.x, p2.y-p1.y)

north(p::Point) = Point(p.x, p.y-1)
south(p::Point) = Point(p.x, p.y+1)
east(p::Point) = Point(p.x+1, p.y)
west(p::Point) = Point(p.x-1, p.y)



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

function pmprint(m::Dict, pixel::Function)
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
