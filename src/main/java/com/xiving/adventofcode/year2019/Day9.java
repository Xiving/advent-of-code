package com.xiving.adventofcode.year2019;

import java.util.List;

public class Day9 extends Year2019Day {

  public Day9() {
    super(9);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Intcode program = Intcode.ofInput(input.getFirst());
    return program.run(1L).flushOutput().toString();
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Intcode program = Intcode.ofInput(input.getFirst());
    return program.run(2L).flushOutput().toString();
  }
}
