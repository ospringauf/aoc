package main

import (
	"fmt"
	"log"
	"math/rand"
	"time"
)

type Beer struct {
	full bool
}

var storage = make(chan Beer, 10)

var done = make(chan string)

func deliver(name string, amount int) {
	for i := 0; i < amount; i++ {
		storage <- Beer{full: true}
		log.Printf("%s delivered\n", name)
		time.Sleep(time.Duration(1000+rand.Intn(200)) * time.Millisecond)
	}
	log.Printf("=== %s has finished delivering beer\n", name)
}

func drink(name string, amount int) {
	countBeers := 0
	timePerBeer := 500 + rand.Intn(100)
	for countBeers <= amount {
		beer := <-storage
		beer.full = false
		countBeers++
		log.Printf("%s drinks beer %d, %d left\n", name, countBeers, len(storage))
		time.Sleep(time.Duration(timePerBeer*countBeers) * time.Millisecond)
	}
	// log.Printf("=== %s has finished drinking beer\n", name)
	done <- fmt.Sprintf("=== %s has finished drinking beer\n", name)
}

func exercise185() {
	go deliver("Maria", 20)
	go deliver("Doris", 15)

	go drink("Rainer", 8)
	go drink("Lutz", 5)
	go drink("Thomas", 3)
	go drink("Michael", 7)

	for i := 0; i < 4; i++ {
		msg := <-done
		log.Println(msg)
	}
	// time.Sleep(30000 * time.Millisecond)
	fmt.Println("END")
}

func main0() {
	exercise185()
}
