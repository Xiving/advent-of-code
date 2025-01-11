package com.xiving.adventofcode.year2019;

import static java.util.Map.entry;

import com.xiving.adventofcode.Main;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

class Intcode {

  private final long[] initial;
  private long[] program;
  private int pc;
  private int relativeBase;

  private boolean halted;
  private boolean awaitingInput;

  private Deque<Long> input;
  private List<Long> output;

  Intcode(long[] program) {
    this.initial = program;
    this.program = Arrays.copyOf(program, program.length);
    this.pc = 0;
    this.relativeBase = 0;

    this.halted = false;

    this.input = new ArrayDeque<>();
    this.output = new ArrayList<>();
  }

  static Intcode ofInput(String input) {
    long[] program = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();
    return new Intcode(program);
  }

  Intcode copy() {
    return new Intcode(this.initial);
  }

  Intcode run(Long... inputs) {
    input.addAll(Arrays.asList(inputs));
    this.awaitingInput = false;

    while (!this.halted && !this.awaitingInput) {
      OPCODE_TABLE.get(opcodeFromInstr()).accept(this);
    }

    return this;
  }

  Intcode reset() {
    System.arraycopy(initial, 0, program, 0, initial.length);

    if (program.length > initial.length) {
      for (int i = initial.length; i < program.length; i++) {
        program[i] = 0;
      }
    }

    pc = 0;
    halted = false;
    awaitingInput = false;
    return this;
  }

  List<Long> flushOutput() {
    List<Long> result = output;
    output = new ArrayList<>();
    return result;
  }

  boolean hasHalted() {
    return halted;
  }

  //
  // OPCODES
  //

  private static final Map<Integer, Consumer<Intcode>> OPCODE_TABLE = Map.ofEntries(
      entry(1, Intcode::add),
      entry(2, Intcode::mul),

      entry(3, Intcode::input),
      entry(4, Intcode::output),

      entry(5, Intcode::jumpIfTrue),
      entry(6, Intcode::jumpIfFalse),
      entry(7, Intcode::lessThan),
      entry(8, Intcode::equals),

      entry(9, Intcode::adjRelativeBase),

      entry(99, Intcode::halt)
  );

  void add() {
    Main.debug("pc: %d, add", pc);
    writeParam(3, readParam(1) + readParam(2));
    pc += 4;
  }

  void mul() {
    Main.debug("pc: %d, mul", pc);
    writeParam(3, readParam(1) * readParam(2));
    pc += 4;
  }

  void input() {
    Main.debug("pc: %d, input", pc);

    if (input.isEmpty()) {
      this.awaitingInput = true;
      return;
    }

    writeParam(1, input.pop());
    pc += 2;
  }

  void output() {
    Main.debug("pc: %d, output", pc);
    output.add(readParam(1));
    pc += 2;
  }

  void jumpIfTrue() {
    Main.debug("pc: %d, jumpIfTrue", pc);
    pc = readParam(1) != 0 ? (int) readParam(2) : pc + 3;
  }

  void jumpIfFalse() {
    Main.debug("pc: %d, jumpIfFalse", pc);
    pc = readParam(1) == 0 ? (int) readParam(2) : pc + 3;
  }

  void lessThan() {
    Main.debug("pc: %d, lessThan", pc);
    writeParam(3, readParam(1) < readParam(2)? 1 : 0);
    pc += 4;
  }

  void equals() {
    Main.debug("pc: %d, equals", pc);
    writeParam(3, readParam(1) == readParam(2)? 1 : 0);
    pc += 4;
  }

  void adjRelativeBase() {
    Main.debug("pc: %d, adjRelativeBase", pc);
    relativeBase += (int) readParam(1);
    pc += 2;
  }

  void halt() {
    Main.debug("pc: %d, halt", pc);
    halted = true;
    pc += 1;
  }

  //
  // UTIL
  //

  void setAddress(int i, long value) {
    if (program.length <= i) {
      growMemory(i);
    }

    program[i] = value;
  }

  long getAddress(int i) {
    if (program.length < i) {
      growMemory(i);
    }

    return program[i];
  }

  private void growMemory(int until) {
    int diff = (1 + until) - program.length;
    int newLength = program.length + Math.max(100, 2 * diff);
    long[] newMemory = new long[newLength];
    System.arraycopy(program, 0, newMemory, 0, program.length);
    program = newMemory;
  }


  int opcodeFromInstr() {
    return (int) (program[pc] % 100);
  }

  long readParam(int param) {
    return switch (ParameterMode.fromInst(program[pc], param)) {
      case POSITION -> getAddress((int) program[pc + param]);
      case IMMEDIATE -> program[pc + param];
      case RELATIVE -> getAddress((int) (relativeBase + program[pc + param]));
    };
  }

  void writeParam(int param, long value) {
    switch (ParameterMode.fromInst(program[pc], param)) {
      case POSITION -> setAddress((int) program[pc + param], value);
      case RELATIVE -> setAddress((int) (relativeBase + program[pc + param]), value);
      case IMMEDIATE -> throw new UnsupportedOperationException("Writing in IMMEDIATE parameter mode not supported!");
    }
  }

}
