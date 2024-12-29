package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.LongStream.Builder;

public class Day17 extends Year2024Day {

  public Day17() {
    super(17);
  }

  private static class Computer {

    int pc;
    long[] regs;
    int[] program;
    Runnable[] opcodeTable;
    Builder out;

    Computer(List<String> input) {
      this.pc = 0;
      this.regs = new long[3];

      this.regs[0] = Integer.parseInt(input.get(0).substring(12));
      this.regs[1] = Integer.parseInt(input.get(1).substring(12));
      this.regs[2] = Integer.parseInt(input.get(2).substring(12));

      this.program = Arrays.stream(input.get(4).substring(9).split(","))
          .mapToInt(c -> c.charAt(0) - '0')
          .toArray();

      this.opcodeTable = new Runnable[]{
          () -> regs[0] >>= getComboOperant(),
          () -> regs[1] ^= program[pc + 1],
          () -> regs[1] = getComboOperant() & 7,
          () -> pc = regs[0] == 0 ? pc : (program[pc + 1] - 2),
          () -> regs[1] ^= regs[2],
          () -> out.add(getComboOperant() & 7),
          () -> regs[1] = regs[0] >> getComboOperant(),
          () -> regs[2] = regs[0] >> getComboOperant()
      };

      this.out = LongStream.builder();
    }

    long getComboOperant() {
      int operand = program[pc + 1];
      return (operand <= 3) ? operand : regs[operand - 4];
    }

    void run() {
      while (pc < program.length) {
        opcodeTable[program[pc]].run();
        pc += 2;
      }
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    Computer computer = new Computer(input);
    computer.run();
    return computer.out.build().mapToObj(String::valueOf).collect(Collectors.joining(","));
  }

  private static long reverseFindA(int[] program) {
    long a = 0;

    loop:
    for (int i = program.length - 1; i >= 0; i--) {
      int j = 0;
      while (program[i] != part2ProgramLoop(a + j)) {
        j++;
      }

      if ((a & 7) + j > 7) {
        a += j;

        for (int k = program.length - i - 1; k > 0; k--) {
          if (program[i + k] != part2ProgramLoop(a >> (3 * k))) {
            a = (a >> (3 * k)) + 1;
            i = i + k + 1;
            continue loop;
          }
        }
      } else {
        a += j;
        a <<= 3;
      }
    }

    return a >> 3;
  }


  private static long part2ProgramLoop(long a) {
    long b = a & 7;
    b = b ^ 5;
    long c = a >> b;
    b = b ^ 6;
    b = b ^ c;
    return b & 7;
  }

  @Override
  public String solvePartTwo(List<String> input) {
    int[] program = Arrays.stream(input.get(4).substring("Program: ".length()).split(",")).mapToInt(c -> c.charAt(0) - '0').toArray();
    long a = reverseFindA(program);
    return String.valueOf(a);
  }
}
