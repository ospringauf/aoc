package main

import (
	"fmt"
	"time"
)

// Run09 Day 9: Sensor Boost
func Run09() {
	t := time.Now()
	prog := IntCode(ReadInputStrings("input09.txt")[0])

	fmt.Println("--- part 1")
	day09Part1(prog)

	fmt.Println("--- part 2")
	day09Part2(prog)

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day09Part1(prog []int64) {
	comp := IntComputer{}
	comp.init(prog)
	comp.provide(1)

	comp.run()
}

func day09Part2(prog []int64) {
	comp := IntComputer{}
	comp.init(prog)
	comp.provide(2)

	comp.run()
	fmt.Println(comp.cntInstr)
}
