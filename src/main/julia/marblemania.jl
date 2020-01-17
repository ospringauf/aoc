
mutable struct Marble
    value::Int
    next::Marble
    prev::Marble
    function Marble(v)
        new(v)
    end
end

function appendMarble(m::Marble, x::Marble)
    m.next = x
    x.next = m.next
    x.prev = m
    x.next.prev = x
end

m = Marble(0)
m.next = m.prev = m
m

appendMarble(m, Marble(1))