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

  private final int[] initial;
  private final int[] program;
  private int pc;
  private boolean halted;
  private boolean awaitingInput;

  private Deque<Integer> input;
  private List<Integer> output;

  Intcode(int[] program) {
    this.initial = program;
    this.program = Arrays.copyOf(program, program.length);
    this.pc = 0;
    this.halted = false;

    this.input = new ArrayDeque<>();
    this.output = new ArrayList<>();
  }

  static Intcode ofInput(String input) {
    int[] program = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
    return new Intcode(program);
  }

  Intcode copy() {
    return new Intcode(this.program);
  }

  Intcode input(Integer... inputs) {
    input.addAll(Arrays.asList(inputs));
    this.awaitingInput = false;
    this.run();
    return this;
  }

  boolean run() {
    while (!this.halted && !this.awaitingInput) {
      OPCODE_TABLE.get(opcodeFromInstr()).accept(this);
    }

    return this.halted;
  }

  void setAddress(int i, int value) {
    program[i] = value;
  }

  int getAddress(int i) {
    return program[i];
  }

  Intcode reset() {
    System.arraycopy(initial, 0, program, 0, initial.length);
    pc = 0;
    halted = false;
    awaitingInput = false;
    return this;
  }

  List<Integer> flushOutput() {
    List<Integer> result = output;
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

      entry(3, Intcode::execute),
      entry(4, Intcode::output),

      entry(5, Intcode::jumpIfTrue),
      entry(6, Intcode::jumpIfFalse),
      entry(7, Intcode::lessThan),
      entry(8, Intcode::equals),

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

  void execute() {
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
    pc = readParam(1) != 0 ? readParam(2) : pc + 3;
  }

  void jumpIfFalse() {
    Main.debug("pc: %d, jumpIfFalse", pc);
    pc = readParam(1) == 0 ? readParam(2) : pc + 3;
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

  void halt() {
    Main.debug("pc: %d, halt", pc);
    halted = true;
    pc += 1;
  }

  //
  // UTIL
  //

  int opcodeFromInstr() {
    return program[pc] % 100;
  }

  int readParam(int param) {
    return switch (ParameterMode.fromInst(program[pc], param)) {
      case POSITION -> program[program[pc + param]];
      case IMMEDIATE -> program[pc + param];
    };
  }

  void writeParam(int param, int value) {
    program[program[pc + param]] = value;
  }

}
