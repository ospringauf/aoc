package main

import (
	"fmt"
	"io/ioutil"
	"strings"
	"time"
)

type Orbit struct {
	inner, outer string
}

func Run06() {
	t := time.Now()

	orbits := read06()
	// fmt.Println(orbits[2].inner, orbits[2].outer)
	fmt.Println("read ", len(orbits), " orbits")

	fmt.Println("--- part 1")
	cnt := countOrbits(orbits, 0, "COM")
	fmt.Println(cnt)

	fmt.Println("--- part 2")
	tra := minTransfers(orbits, "YOU", "SAN") - 2
	fmt.Println(tra)

	fmt.Printf("=== end (%d ms)\n", (time.Now().Sub(t).Milliseconds()))
}

func countOrbits(orbits []Orbit, ci int, inner string) int {
	c := 0
	for _, o := range orbits {
		if o.inner == inner {
			c += ci + 1 + countOrbits(orbits, ci+1, o.outer)
		}
	}
	return c
}

func minTransfers(orbits []Orbit, start, target string) int {
	inf := 1000000
	distmap := make(map[string]int)
	distmap[start] = 0

	dst := func(x string) int {
		val, exists := distmap[x]
		if !exists {
			val = inf
			distmap[x] = val
		}
		return val
	}
	better := true
	for better {
		better = false
		for _, o := range orbits {
			di := dst(o.inner)
			do := dst(o.outer)
			if di > 1+do {
				better = true
				distmap[o.inner] = 1 + do
			}
			if do > 1+di {
				better = true
				distmap[o.outer] = 1 + di
			}
		}
	}

	return distmap[target]
}

func read06() []Orbit {
	fileBytes, _ := ioutil.ReadFile("input06.txt")
	lines := strings.Split(string(fileBytes), "\r\n")

	orb := make([]Orbit, 0, len(lines))
	for _, l := range lines {
		s := strings.Split(l, ")")
		orb = append(orb, Orbit{s[0], s[1]})
	}
	return orb
}
