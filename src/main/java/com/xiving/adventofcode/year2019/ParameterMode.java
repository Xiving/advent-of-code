package com.xiving.adventofcode.year2019;

public enum ParameterMode {
  POSITION,
  IMMEDIATE;

  static ParameterMode fromInst(int instr, int parameter) {
    int divisor = 100;
    parameter -= 1;

    while (parameter > 0) {
      divisor *= 10;
      parameter -= 1;
    }

    return ParameterMode.values()[(instr / divisor) % 10];
  }
}
