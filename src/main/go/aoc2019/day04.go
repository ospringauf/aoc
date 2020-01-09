package main

import (
	"fmt"
	"time"
)

func Run04() {
	t := time.Now()

	fmt.Println("--- part1")
	c := 0
	for p := 156218; p <= 652527; p++ {
		if passwd1(p) {
			c++
		}
	}
	fmt.Println(c)

	fmt.Println("--- part2")
	c = 0
	for p := 156218; p <= 652527; p++ {
		if passwd2(p) {
			c++
		}
	}
	fmt.Println(c)

	fmt.Printf("=== end (%d ms)\n", (time.Now().Sub(t).Milliseconds()))
}

func digits(p int) []int {
	return []int{p / 100000 % 10, p / 10000 % 10, p / 1000 % 10, p / 100 % 10, p / 10 % 10, p % 10}
}

func passwd1(p int) bool {
	d := digits(p)
	adj := false
	for i := 1; i < len(d); i++ {
		adj = adj || (d[i-1] == d[i])
	}
	mono := true
	for i := 1; i < len(d); i++ {
		mono = mono && (d[i] >= d[i-1])
	}
	return adj && mono
}

func passwd2(p int) bool {
	d := digits(p)
	m := make(map[int]int)
	for _, di := range d {
		m[di]++
	}
	adj := false
	for i := 1; i < len(d); i++ {
		adj = adj || ((d[i-1] == d[i]) && (m[d[i]] == 2))
	}
	mono := true
	for i := 1; i < len(d); i++ {
		mono = mono && (d[i] >= d[i-1])
	}
	return adj && mono
}
