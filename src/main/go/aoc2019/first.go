package main

import (
	"fmt"
	"time"

	"github.com/yanatan16/itertools"
)

func wake(zeit time.Duration, grund string) {
	time.Sleep(zeit)
	fmt.Println("Zeit abgelaufen: ", grund)
}

func main() {
	// go wake(180*time.Second, "Tee!")
	// go wake(200*time.Second, "Wichtiger Termin!")
	// time.Sleep(500 * time.Second)

	Run07()
	// testMap()
	// testIter()
}

func testIter() {
	// var m interface{}
	m := []int32{1, 2, 3, 4, 5}
	f := func(i interface{}) interface{} { return 2 * i.(int32) }
	//m2 := itertools.Map(f, itertools.Int32(1, 2, 3, 4, 5))
	m2 := itertools.Map(f, itertools.Int32(m...))
	fmt.Println(itertools.List(m2))

	r := func(memo interface{}, el interface{}) interface{} { return el.(int32) + memo.(int32) }

	m3 := itertools.Reduce(itertools.Map(f, itertools.Int32(m...)), r, int32(0))
	fmt.Println(m3)
}

func testMap() {
	p1 := Point{0, 0}
	p2 := Point{0, 0}
	p3 := Point{2, 3}
	m := make(map[Point]bool)
	m[p1] = true
	fmt.Println(m[p3])
	fmt.Println(m[p2])
	fmt.Println(&p1 == &p1)
}
