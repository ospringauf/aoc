package main

// Point is a 2d coord
type Point struct {
	x, y int
}

func (p Point) manhattan() int {
	abs := func(x int) int {
		if x < 0 {
			return -x
		}
		return x
	}
	return abs(p.x) + abs(p.y)
}

func (p Point) mdist(p0 Point) int {
	abs := func(x int) int {
		if x < 0 {
			return -x
		}
		return x
	}
	return abs(p.x-p0.x) + abs(p.y-p0.y)
}

func (p Point) hash() int {
	return p.x + 10000*p.y
}

func (p Point) indexIn(w []Point) int {
	for i, pi := range w {
		if pi == p {
			return i
		}
	}
	return -1
}

func (p Point) north() Point {
	return Point{p.x, p.y - 1}
}

func (p Point) south() Point {
	return Point{p.x, p.y + 1}
}

func (p Point) east() Point {
	return Point{p.x + 1, p.y}
}

func (p Point) west() Point {
	return Point{p.x - 1, p.y}
}
