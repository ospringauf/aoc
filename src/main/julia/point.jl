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