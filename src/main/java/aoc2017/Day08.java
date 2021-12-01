package aoc2017;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import common.AocPuzzle;

class Day08 extends AocPuzzle {

    class Instruction {

        public Instruction action;
        public Instruction condition;

        String reg;
        String op;
        int value;

        boolean eval() {
            var r = getRegister(reg);
            switch (op) {
            case "==":
                return r == value;
            case "!=":
                return r != value;
            case "<":
                return r < value;
            case "<=":
                return r <= value;
            case ">":
                return r > value;
            case ">=":
                return r >= value;

            default:
                throw new RuntimeException("unsupported condition: " + this);
            }
        }

        void act() {
            switch (op) {
            case "inc":
                putRegister(reg, getRegister(reg) + value);
                break;
            case "dec":
                putRegister(reg, getRegister(reg) - value);
                break;
            default:
                throw new RuntimeException("unsupported operation: " + this);
            }
        }

        void execute() {
            if (condition.eval())
                action.act();
        }

        @Override
        public String toString() {
            return (action != null) ? String.format("%s if %s", action, condition)
                    : String.format("%s %s %d", reg, op, value);

        }
    }

    String[] input = { "b inc 5 if a > 1", "a inc 1 if b < 5", "c dec -10 if a >= 1", "c inc -20 if c == 10" };

    Instruction parse(String s1) {
        Instruction i = new Instruction();
        var s2 = s1.split(" ");

        i.reg = s2[0];
        i.op = s2[1];
        i.value = Integer.parseInt(s2[2]);
        return i;
    }

    int highest = Integer.MIN_VALUE;
    
    public void putRegister(String reg, int i) {
        highest = Math.max(highest, i);
        register.put(reg, i);
        
    }

    Instruction parseLine(String s) {
        var s1 = s.split(" if ");
        Instruction i = new Instruction();
        i.action = parse(s1[0]);
        i.condition = parse(s1[1]);
        return i;
    }

    Map<String, Integer> register = new HashMap<>();

    Integer getRegister(String r) {
        if (!register.containsKey(r)) {
            register.put(r, 0);
        }
        return register.get(r);
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=== part1 ===");
        new Day08().solve();
    }


    private void solve() throws Exception {

        // var i1 = parseLine("b inc 5 if a > 1");
        // System.out.println(i1);
        // i1.execute();

        // var prog = Arrays.stream(input).map(this::parseLine).collect(Collectors.toList());
        var prog = file2lines("input08.txt").map(this::parseLine).toJavaList();

        prog.forEach(Instruction::execute);
        prog.forEach(System.out::println);
        System.out.println(register);
        System.out.println(register.values().stream().mapToInt(x -> x).max());
        System.out.println(highest);
    }
}
