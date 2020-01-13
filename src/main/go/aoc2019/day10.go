package main

import (
	"fmt"
	"math"
	"sort"
	"time"
)

// Run10 Day 10: Monitoring Station
func Run10() {

	t := time.Now()
	fmt.Println("--- part 1")
	day10Part1()

	fmt.Println("--- part 2")
	day10Part2()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day10Part1() {
	input := ReadInputStrings("input10.txt")
	m := make(map[Point]bool)
	for y, s := range input {
		for x, c := range s {
			if c == '#' {
				m[Point{x, y}] = true
			}
		}
	}

	angle := func(p1, p2 Point) int {
		//return int(10000 * math.Atan2(float64(p2.y-p1.y), float64(p2.x-p1.x)))
		return int(10000 * (math.Atan2(-float64(p2.x-p1.x), float64(p2.y-p1.y)) + math.Pi))
	}
	max := 0
	best := Point{0, 0}
	for s := range m {
		vis := make(map[int]int)
		for a := range m {
			an := angle(s, a)
			_, ex := vis[an]
			if !ex {
				vis[an] = 1
			} else {
				vis[an]++
			}
		}
		// fmt.Printf("(%d,%d) sees %d\n", s.x, s.y, len(vis))
		if len(vis) > max {
			max = len(vis)
			best = s
		}
	}

	fmt.Println(max, " from ", best)
}

func day10Part2() {
	input := ReadInputStrings("input10.txt")
	m := make(map[Point]bool)
	for y, s := range input {
		for x, c := range s {
			if c == '#' {
				m[Point{x, y}] = true
			}
		}
	}

	station := Point{20, 21}
	angle := func(p1, p2 Point) int {
		return int(10000 * (math.Atan2(-float64(p2.x-p1.x), float64(p2.y-p1.y)) + math.Pi))
	}

	vis := make(map[int][]Point)
	for a := range m {
		alpha := angle(station, a)
		_, ex := vis[alpha]
		if !ex {
			vis[alpha] = []Point{a}
		} else {
			vis[alpha] = append(vis[alpha], a)
		}
	}

	// sort angles
	keys := make([]int, 0, len(vis))
	for k := range vis {
		keys = append(keys, k)
	}
	sort.Slice(keys, func(i, j int) bool { return keys[i] < keys[j] })
	// fmt.Println(keys)

	// sort asteroids by distance per angle
	for a := range vis {
		sort.Slice(vis[a], func(i, j int) bool { return vis[a][i].mdist(station) < vis[a][j].mdist(station) })
	}
	// fmt.Println(vis[0])

	// zap asteroids, nearest first, by increasing angle
	for i, zapped := 0, 0; zapped <= 200; i = (i + 1) % len(keys) {
		alpha := keys[i]
		if _, ex := vis[alpha]; ex {
			zapped++
			fmt.Println("kill ", zapped, ": ", vis[alpha][0])
			vis[alpha] = vis[alpha][1:]
		}

	}
}
