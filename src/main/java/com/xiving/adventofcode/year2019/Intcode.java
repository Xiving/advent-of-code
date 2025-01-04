package com.xiving.adventofcode.year2019;

import static java.util.Map.entry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

class Intcode {

  private int[] initial;
  private int[] program;
  private int pc;
  private boolean hasNext;

  Intcode(int[] program) {
    this.initial = program;
    this.program = Arrays.copyOf(program, program.length);
    this.pc = 0;
    this.hasNext = true;
  }

  static Intcode ofString(String str) {
    return new Intcode(Arrays.stream(str.split(",")).mapToInt(Integer::parseInt).toArray());
  }


  void run() {
    while (this.hasNext) {
      OPCODE_TABLE.get(program[pc]).accept(this);
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
    hasNext = true;
  }

  //
  // OPCODES
  //

  private static final Map<Integer, Consumer<Intcode>> OPCODE_TABLE = Map.ofEntries(
      entry(1, Intcode::add),
      entry(2, Intcode::mul),
      entry(99, Intcode::halt)
  );

  void add() {
    program[arg3()] = program[arg1()] + program[arg2()];
    pc += 4;
  }

  void mul() {
    program[arg3()] = program[arg1()] * program[arg2()];
    pc += 4;
  }

  void halt() {
    hasNext = false;
    pc += 1;
  }

  //
  // UTIL
  //

  int arg1() {
    return program[pc + 1];
  }

  int arg2() {
    return program[pc + 2];
  }

  int arg3() {
    return program[pc + 3];
  }
}
