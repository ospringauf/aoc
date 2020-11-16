package main

// Marble is the structure for our game
type Marble struct {
	value int
	next  *Marble
	prev  *Marble
}

// NewMarble is the factory function for Marble
func NewMarble(value int) *Marble {
	m := &Marble{value, nil, nil}
	return m
}

func (m *Marble) insertBetween(a, b *Marble) {
	m.prev = a
	m.next = b
	a.next = m
	b.prev = m
}

func (m *Marble) remove() {
	m.prev.next = m.next
	m.next.prev = m.prev
}

func play(players, marbles int) int {
	score := make([]int, players)

	current := NewMarble(0)
	current.next = current
	current.prev = current

	for i := 1; i < marbles; i++ {
		if i%23 == 0 {
			m := current.prev.prev.prev.prev.prev.prev.prev
			m.remove()
			score[i%players] += i
			score[i%players] += m.value
			current = m.next

		} else {
			m := NewMarble(i)
			m.insertBetween(current.next, current.next.next)
			current = m
		}

	}
	return max(score)
}

func max(score []int) int {
	m := 0
	for _, v := range score {
		if v > m {
			m = v
		}
	}
	return m
}

func main() {
	println(play(10, 1618))
	println(play(427, 70723))
}
