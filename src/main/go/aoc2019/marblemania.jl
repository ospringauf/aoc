mutable struct Marble
    value::Int
    next::Marble
    prev::Marble

    function Marble(v)
        m = new(v)
        m.prev = m.next = m
    end
end

function insertMarble(a, m, b ::Marble)
    a.next = m
    m.next = b
    b.prev = m
    m.prev = a
end

function removeMarble(m::Marble)
    m.prev.next = m.next
    m.next.prev = m.prev
end

function play(players, lastmarble)
    score = [0 for p = 1:players]
    current = Marble(0)

    for val = 1:lastmarble
        player = 1+ val % players

        if val % 23 == 0
            m = current.prev.prev.prev.prev.prev.prev.prev
            removeMarble(m)
            current = m.next
            score[player] += val + m.value
        else
            m = Marble(val)
            insertMarble(current.next, m, current.next.next)
            current = m
        end
    end

    max(score...)
end

play(10, 1618)

@show play(427, 70723)

@show play(427, 7072300)