package main

import (
	"aoc2019/utl"
	"fmt"
	"time"
)

// Run02 Day 2: 1202 Program Alarm
func Run02() {

	t := time.Now()
	day02Part1()
	day02Part2()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day02Part1() {
	a := utl.IntCode(utl.ReadInputStrings("input02.txt")[0])

	comp := IntComputer{}
	comp.init(a)

	comp.mem[1], comp.mem[2] = 12, 2
	fmt.Println(comp.mem[0:20])
	comp.run()

	fmt.Println(comp.mem[0])
}

func day02Part2() {
	a := utl.IntCode(utl.ReadInputStrings("input02.txt")[0])

	comp := IntComputer{}
	comp.init(a)

	for noun := 0; noun <= 99; noun++ {
		for verb := 0; verb <= 99; verb++ {
			comp.init(a)
			comp.mem[1], comp.mem[2] = int64(noun), int64(verb)
			comp.run()
			if comp.mem[0] == 19690720 {
				fmt.Println(100*noun + verb)
				return
			}

		}

	}

}
