package main

import (
	"aoc2019/utl"
	"fmt"
	"strconv"
	"strings"
	"time"
)

// Run03 Day 3: Crossed Wires
func Run03() {

	t := time.Now()
	day03Part1()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func wirePoints(w []string) []Point {
	r := make([]Point, 0, 1000)
	p := Point{0, 0}
	for _, wi := range w {
		l, _ := strconv.Atoi(wi[1:])
		for i := 0; i < l; i++ {
			switch wi[0] {
			case 'U':
				p = p.north()
			case 'D':
				p = p.south()
			case 'L':
				p = p.west()
			case 'R':
				p = p.east()
			}
			r = append(r, p)
		}
	}
	return r
}

func day03Part1() {
	s := utl.ReadInputStrings("input03.txt")
	w1 := wirePoints(strings.Split(s[0], ","))
	w2 := wirePoints(strings.Split(s[1], ","))

	fmt.Println(len(w1))
	fmt.Println(len(w2))
	inters := intersect(w1, w2)
	// fmt.Println(inters)

	p := inters[0]
	for _, x := range inters {
		if x.manhattan() < p.manhattan() {
			p = x
		}
	}
	fmt.Println(p.manhattan())

	wlen := func(p Point) int {
		return p.indexIn(w1) + p.indexIn(w2) + 2
	}

	fmt.Println("--- part 2 ---")
	p = inters[0]
	for _, x := range inters {
		if wlen(x) < wlen(p) {
			p = x
		}
	}
	fmt.Println(wlen(p))
}

func intersect(w1, w2 []Point) []Point {
	m := make(map[Point]bool, len(w1))
	for _, p := range w1 {
		m[p] = true
	}
	result := make([]Point, 0, len(w2))
	seen := make(map[Point]bool) // avoid dups
	for _, p := range w2 {
		if m[p] && (!seen[p]) {
			result = append(result, p)
			seen[p] = true
		}
	}
	return result
}
