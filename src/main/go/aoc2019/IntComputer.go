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
	RELBASE       OpCode = 9
)

// IntComputer executes AOC 2019 IntCode
type IntComputer struct {
	mem      [10000]int64
	iptr     int
	relBase  int64
	cntInstr int64

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
	c.cntInstr++
	instr := c.mem[c.iptr]
	op := OpCode(instr % 100)
	p := c.mem[c.iptr : c.iptr+4]
	mode := []int{0, (int(instr) / 100) % 10, (int(instr) / 1000) % 10, (int(instr) / 10000) % 10}

	param := func(i int) int64 {
		switch mode[i] {
		case 1: // immediate
			return p[i]
		case 2: // relative
			return c.mem[p[i]+c.relBase]
		default: // position
			return c.mem[p[i]]
		}
	}

	tgt := func(i int) int64 {
		switch mode[i] {
		case 2: // relative
			return p[i] + c.relBase
		default: // position
			return p[i]
		}
	}

	switch op {
	case ADD:
		c.mem[tgt(3)] = param(1) + param(2)
		c.iptr += 4
	case MULT:
		c.mem[tgt(3)] = param(1) * param(2)
		c.iptr += 4
	case INPUT:
		c.mem[tgt(1)] = c.input()
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
			c.mem[tgt(3)] = 1
		} else {
			c.mem[tgt(3)] = 0
		}
		c.iptr += 4
	case EQUALS:
		if param(1) == param(2) {
			c.mem[tgt(3)] = 1
		} else {
			c.mem[tgt(3)] = 0
		}
		c.iptr += 4
	case RELBASE:
		c.relBase += param(1)
		c.iptr += 2
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
