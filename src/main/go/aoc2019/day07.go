package main

import (
	"aoc2019/utl"
	"fmt"
	"time"

	prmt "github.com/gitchander/permutation"
)

// Run07 Day 7: Amplification Circuit
func Run07() {

	t := time.Now()
	fmt.Println("--- part 1")
	day07Part1()

	fmt.Println("--- part 2")
	day07Part2()

	fmt.Println("--- part 2 async")
	day07Part2Async()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day07Part1() {
	a := utl.IntCode(utl.ReadInputStrings("input07.txt")[0])

	phases := []int{0, 1, 2, 3, 4}
	p := prmt.New(prmt.IntSlice(phases))
	best := int64(0)
	for p.Next() {
		// fmt.Println(phases)

		in := int64(0)
		for i := 0; i < 5; i++ {
			comp := IntComputer{}
			comp.init(a)
			comp.provide(int64(phases[i]), in)
			comp.out = func(v int64) { in = v }
			comp.run()
		}
		// fmt.Println(in)
		if in > best {
			best = in
		}
	}
	fmt.Println(best)
}

func day07Part2() {
	a := utl.IntCode(utl.ReadInputStrings("input07.txt")[0])
	phases := []int{5, 6, 7, 8, 9}
	amps := make([]IntComputer, 5)

	p := prmt.New(prmt.IntSlice(phases))
	best := int64(0)
	for p.Next() {

		for i := range amps {
			i := i
			amps[i].init(a)
			amps[i].out = func(v int64) {
				// log.Printf("amp %d output %d\n", i, v)
				if v > best {
					best = v
				}
				amps[(i+1)%5].provide(v)
			}
			amps[i].provide(int64(phases[i]))
		}
		amps[0].provide(0)

		i := 0
		for !amps[4].halted() {
			amps[i].runToOutput()
			i = (i + 1) % 5
		}
	}
	fmt.Println(best)
}

func day07Part2Async() {
	a := utl.IntCode(utl.ReadInputStrings("input07.txt")[0])

	phases := []int{5, 6, 7, 8, 9}
	amps := make([]IntComputer, 5)

	p := prmt.New(prmt.IntSlice(phases))
	best := int64(0)
	for p.Next() {

		for i := range amps {
			amps[i].init(a)
			amps[i].initAsync()
		}

		for i := range amps {
			i := i
			// amps[i].out = func(v int64) {
			// 	// log.Printf("amp %d output %d\n", i, v)
			// 	// tgt := &amps[(i+1)%5]
			// 	// tgt.inchan <- v
			// 	amps[(i+1)%5].inchan <- v
			// 	if v > best {
			// 		best = v
			// 	}
			// }
			amps[i].outchan = amps[(i+1)%5].inchan
			go amps[i].run()
			amps[i].inchan <- int64(phases[i])
		}

		amps[0].inchan <- 0

		v4 := <-amps[4].done

		if v4 > best {
			best = v4
		}

		// fmt.Println(in)
	}
	fmt.Println(best)
}
