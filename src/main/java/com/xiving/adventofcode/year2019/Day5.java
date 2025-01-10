package com.xiving.adventofcode.year2019;

import java.util.List;
import java.util.stream.Collectors;

public class Day5 extends Year2019Day {

  public Day5() {
    super(5);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Intcode intcode = Intcode.ofInput(input.getFirst());
    intcode.input(1);
    intcode.run();
    return intcode.flushOutput().stream().map(Object::toString).collect(Collectors.joining(", "));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Intcode intcode = Intcode.ofInput(input.getFirst());
    intcode.input(5);
    intcode.run();
    return intcode.flushOutput().stream().map(Object::toString).collect(Collectors.joining(", "));
  }
}
