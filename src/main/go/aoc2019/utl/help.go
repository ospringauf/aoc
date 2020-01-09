package utl

import (
	"bufio"
	"log"
	"os"
	"strconv"
	"strings"
)

func ReadInputStrings(fname string) []string {
	file, err := os.Open(fname)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	s := make([]string, 0, 1000)

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		s = append(s, scanner.Text())
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	return s
}

func ParseToInt(s []string) []int {
	n := make([]int, len(s))
	for i, si := range s {
		// x, _ := strconv.ParseInt(si, 10, 32)
		x, _ := strconv.Atoi(si)
		n[i] = x
	}

	return n
}

func IntCode(s0 string) []int64 {
	s := strings.Split(s0, ",")
	n := make([]int64, len(s))
	for i, si := range s {
		x, _ := strconv.ParseInt(si, 10, 32)
		n[i] = x
	}

	return n
}
