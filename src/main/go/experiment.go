package main

import (
	"fmt"
	"time"
)

func experiments() {

	ausgeben()

	d := Dog{"waldi"}
	d.bellen()
}

type Dog struct {
	name string
}

func (d *Dog) bellen() (int, int) {

	out := func(s string) {
		fmt.Println(s)
	}
	out("wau " + d.name)
	return 1, 2
}

func ausgeben() {
	c := make(chan int)
	go zaehle(c)
	for {
		n := <-c
		fmt.Println(n)
	}
}

func zaehle(c chan int) {

	for i := 0; i < 10000; i++ {
		c <- i
		time.Sleep(100)
	}
}
