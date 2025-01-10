package com.xiving.adventofcode.year2019;

import java.util.List;

public class Day2 extends Year2019Day{

  public Day2() {
    super(2);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Intcode intcode = Intcode.ofInput(input.getFirst());
    intcode.setAddress(1, 12);
    intcode.setAddress(2, 2);
    intcode.run();
    return String.valueOf(intcode.getAddress(0));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Intcode intcode = Intcode.ofInput(input.getFirst());

    for (int noun = 0; noun < 100; noun++) {
      for (int verb = 0; verb < 100; verb++) {
        intcode.reset();
        intcode.setAddress(1, noun);
        intcode.setAddress(2, verb);
        intcode.run();

        if (intcode.getAddress(0) == 19690720) {
          return String.valueOf(noun * 100 + verb);
        }
      }
    }

    throw new RuntimeException("Could not find values for noun and/or verb!");
  }
}
