package main

import (
	"fmt"
	"time"
)

// Run08 Day 8: Space Image Format
func Run08() {
	t := time.Now()
	fmt.Println("--- part 1")
	day08Part1()

	fmt.Println("--- part 2")
	day08Part2()

	fmt.Printf("=== end (%d ms) ===\n", time.Now().Sub(t).Milliseconds())
}

func day08Part1() {
	s0 := ReadInputStrings("input08.txt")[0]
	img := make([]int, len(s0))
	for i, si := range s0 {
		img[i] = int(si) - '0'
	}
	fmt.Println(img[0:10])
	layersize := 25 * 6
	nlayers := len(img) / layersize
	fmt.Println("layers in image: ", nlayers)

	layer := func(l int) []int {
		return img[l*layersize : (l+1)*layersize]
	}

	noOfDigits := func(layer []int, digit int) (noz int) {
		for _, d := range layer {
			if d == digit {
				noz++
			}
		}
		return
	}

	r, z := 0, layersize
	for l := 0; l < nlayers; l++ {
		x := noOfDigits(layer(l), 0)
		if x < z {
			r = l
			z = x
		}
	}
	fmt.Println("result: ", noOfDigits(layer(r), 1)*noOfDigits(layer(r), 2))
}

func day08Part2() {
	s0 := ReadInputStrings("input08.txt")[0]
	img := make([]int, len(s0))
	for i, si := range s0 {
		img[i] = int(si) - '0'
	}
	layersize := 25 * 6
	nlayers := len(img) / layersize
	layer := func(l int) []int {
		return img[l*layersize : (l+1)*layersize]
	}

	last := layer(nlayers - 1)
	for l := nlayers - 2; l >= 0; l-- {
		for i, p := range layer(l) {
			if p != 2 {
				last[i] = p
			}
		}
	}

	for y := 0; y < 6; y++ {
		for x := 0; x < 25; x++ {
			fmt.Print(string(" #"[last[y*25+x]]))
		}
		fmt.Println()
	}
}
