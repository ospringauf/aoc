package main

import (
	"fmt"
	"time"
)

// Run05 Day 5: Sunny with a Chance of Asteroids
func Run05() {

	t := time.Now()
	day05Part1()
	day05Part2()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day05Part1() {
	a := IntCode(ReadInputStrings("input05.txt")[0])

	comp := IntComputer{}
	comp.init(a)
	comp.provide(1, 2, 3, 4)
	go comp.run()

	// comp.in <- 1

	for !comp.halted() {
	}
}

func day05Part2() {
	a := IntCode(ReadInputStrings("input05.txt")[0])

	comp := IntComputer{}
	comp.init(a)
	comp.provide(5)
	go comp.run()

	// comp.in <- 1

	for !comp.halted() {
	}
}
