package com.xiving.adventofcode.year2019;

import java.util.Arrays;

class Intcode {

  private int[] initial;
  private int[] program;
  private int pc;

  static Intcode ofString(String str) {
    return new Intcode(Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray());
  }

  Intcode(int[] program) {
    this.initial = program;
    this.program = Arrays.copyOf(program, program.length);
    this.pc = 0;
  }

  private int runInstruction() {
    return switch (program[pc]) {
      case 1 -> {
        program[program[pc + 3]] = program[program[pc + 1]] + program[program[pc + 2]];
        yield 4;
      }
      case 2 -> {
        program[program[pc + 3]] = program[program[pc + 1]] * program[program[pc + 2]];
        yield 4;
      }
      case 99 -> 0;
      default -> throw new RuntimeException("Unsupported opcode: " + program[pc]);
    };
  }

  private boolean hasNext() {
    return program[pc] != 99;
  }

  void run() {
    while (hasNext()) {
      pc += runInstruction();
    }
  }

  void setAddress(int i, int value) {
    initial[i] = value;
    program[i] = value;
  }

  int getAddress(int i) {
    return program[i];
  }

  void reset() {
    System.arraycopy(initial, 0, program, 0, initial.length);
    pc = 0;
  }

}
