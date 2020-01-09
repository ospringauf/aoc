package main

import (
	"fmt"
	"aoc2019/utl"
)

func fuel(mass int) int {
	return (mass / 3) - 2
}

func fuel2(mass int) (f int) {
	f = fuel(mass)
	if f > 0 {
		f += fuel2(f)
	} else {
		f = 0
	}
	return
}

// Main01 Day 1: The Tyranny of the Rocket Equation
func Main01() {

	fmt.Println(fuel(100756))
	
	a := utl.ParseToInt(utl.ReadInputStrings("input01.txt"))

	f := 0
	for _, m := range a {
		f += fuel(m)
	}
	fmt.Println(f)

	f2 := 0
	for _, m := range a {
		f2 += fuel2(m)
	}
	fmt.Println(f2)
}
