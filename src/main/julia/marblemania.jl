mutable struct Marble
    value::Int
    next::Marble
    prev::Marble

    function Marble(v)
        m = new(v)
        m.prev = m.next = m
    end
end

function appendMarble(m::Marble, newm::Marble)
    newm.prev = m
    newm.next = m.next
    m.next.prev = newm
    m.next = newm
end


function removeMarble(m::Marble)
    m.prev.next = m.next
    m.next.prev = m.prev
end

function play(nplayers, lastmarble)
    score = [0 for p = 1:nplayers]
    current = Marble(0)

    for val = 1:lastmarble
        player = 1+ val % nplayers

        if val % 23 == 0
            r = current.prev.prev.prev.prev.prev.prev.prev
            removeMarble(r)
            current = r.next
            score[player] += val + r.value
        else
            m = Marble(val)
            appendMarble(current.next, m)
            current = m
        end
    end

    max(score...)
end

play(10, 1618)

@show play(427, 70723)

@show play(427, 7072300)