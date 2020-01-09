package main

import (
	"fmt"
)

type OpCode int

const (
	HALT          OpCode = 99
	ADD           OpCode = 1
	MULT          OpCode = 2
	INPUT         OpCode = 3
	OUTPUT        OpCode = 4
	JUMP_IF_TRUE  OpCode = 5
	JUMP_IF_FALSE OpCode = 6
	LESS_THAN     OpCode = 7
	EQUALS        OpCode = 8
)

// IntComputer executes AOC 2019 IntCode
type IntComputer struct {
	mem  [10000]int64
	iptr int

	data       []int64
	in         func() int64
	out        func(value int64)
	lastOutput int64

	inchan  chan int64
	outchan chan int64
	done    chan int64
}

func (c *IntComputer) run() {
	for !c.halted() {
		c.run1()
	}
	c.run1()
}

func (c *IntComputer) runToOutput() {
	for !c.halted() && OpCode(c.mem[c.iptr]) != OUTPUT {
		c.run1()
	}
	if !c.halted() {
		c.run1()
	}
}

func (c *IntComputer) halted() bool {
	return OpCode(c.mem[c.iptr]) == HALT
}

func (c *IntComputer) run1() {
	instr := c.mem[c.iptr]
	op := OpCode(instr % 100)
	p := c.mem[c.iptr : c.iptr+4]
	mode := []int{0, (int(instr) / 100) % 10, (int(instr) / 1000) % 10, (int(instr) / 10000) % 10}

	pos := func(i int) int64 {
		return c.mem[p[i]]
	}
	imm := func(i int) int64 {
		return p[i]
	}

	param := func(i int) int64 {
		if mode[i] == 1 {
			return imm(i)
		}
		return pos(i)
	}

	switch op {
	case ADD:
		c.mem[p[3]] = param(1) + param(2)
		c.iptr += 4
	case MULT:
		c.mem[p[3]] = param(1) * param(2)
		c.iptr += 4
	case INPUT:
		c.mem[p[1]] = c.input()
		c.iptr += 2
	case OUTPUT:
		c.output(param(1))
		c.iptr += 2
	case JUMP_IF_TRUE:
		if param(1) != 0 {
			c.iptr = int(param(2))
		} else {
			c.iptr += 3
		}
	case JUMP_IF_FALSE:
		if param(1) == 0 {
			c.iptr = int(param(2))
		} else {
			c.iptr += 3
		}
	case LESS_THAN:
		if param(1) < param(2) {
			c.mem[p[3]] = 1
		} else {
			c.mem[p[3]] = 0
		}
		c.iptr += 4
	case EQUALS:
		if param(1) == param(2) {
			c.mem[p[3]] = 1
		} else {
			c.mem[p[3]] = 0
		}
		c.iptr += 4
	case HALT:
		c.done <- c.lastOutput
		// fmt.Println("HALT")
	default:
		panic(fmt.Sprintf("unknown opcode in istruction %d", instr))
	}
}

func (c *IntComputer) output(val int64) {
	c.lastOutput = val
	if c.out != nil {
		c.out(val)
	} else {
		fmt.Println("output: ", val)
	}
}

func (c *IntComputer) input() int64 {
	v := int64(0)
	if c.in != nil {
		v = c.in()
	} else {
		v = c.data[0]
		c.data = c.data[1:]
	}
	// log.Println("input ", v)
	return v
}

// func (c *IntComputer) input1() int64 {
// 	v := <-c.in
// 	// log.Println("input ", v)
// 	return v
// }

func (c *IntComputer) provide(values ...int64) {
	c.data = append(c.data, values...)
}

func (c *IntComputer) init(program []int64) {
	for i := range c.mem {
		c.mem[i] = 0
	}
	copy(c.mem[0:], program)
	c.iptr = 0
	c.data = make([]int64, 0)
	c.done = make(chan int64, 1)
}

func (c *IntComputer) initAsync() {
	c.inchan = make(chan int64, 2)
	c.outchan = make(chan int64, 2)
	c.in = func() int64 { return <-c.inchan }
	c.out = func(v int64) { c.outchan <- v }
}
