package com.xiving.adventofcode.year2019;

import java.util.List;

public class Day1 extends Year2019Day {

  public Day1() {
    super(1);
  }

  @Override
  public String solvePartOne(List<String> input) {
    return String.valueOf(input.stream().mapToInt(Integer::parseInt).map(v -> (v / 3) - 2).sum());
  }

  @Override
  public String solvePartTwo(List<String> input) {
    return String.valueOf(input.stream().mapToInt(Integer::parseInt).map(v -> {
      int fuelMass = 0;

      while (v != 0) {
        v = Math.max(0, (v / 3) - 2);
        fuelMass += v;
      }

      return fuelMass;
    }).sum());
  }
}
