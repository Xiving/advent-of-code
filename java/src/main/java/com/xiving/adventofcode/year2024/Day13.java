package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends Year2024Day {

  private static final Pattern BUTTON_A = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
  private static final Pattern BUTTON_B = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
  private static final Pattern PRIZE = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

  public Day13() {
    super(13);
  }

  private record ClawMachine(long ax, long ay, long bx, long by, long x, long y) {

    long cheapestPriceToWin() {
      long a = ((y * bx) - (x * by)) / ((ay * bx) - (ax * by));
      long b = (x / bx) - ((ax * a) / bx);

      if (x == (a * ax + b * bx) && y == (a * ay + b * by)) {
        return a * 3 + b;
      } else {
        return 0;
      }
    }
  }

  private static List<ClawMachine> fromInput(List<String> input, long positionCorrection) {
    List<ClawMachine> clawMachines = new ArrayList<>();

    int i = 0;

    while (i < input.size()) {
      Matcher buttonA = BUTTON_A.matcher(input.get(i));
      Matcher buttonB = BUTTON_B.matcher(input.get(i + 1));
      Matcher prize = PRIZE.matcher(input.get(i + 2));

      if (!buttonA.matches() || !buttonB.matches() || !prize.matches()) {
        return clawMachines;
      }

      clawMachines.add(new ClawMachine(
          Long.parseLong(buttonA.group(1)),
          Long.parseLong(buttonA.group(2)),
          Long.parseLong(buttonB.group(1)),
          Long.parseLong(buttonB.group(2)),
          Long.parseLong(prize.group(1)) + positionCorrection,
          Long.parseLong(prize.group(2)) + positionCorrection
      ));

      i += 4;
    }

    return clawMachines;
  }

  @Override
  public String solvePartOne(List<String> input) {
    List<ClawMachine> clawMachines = fromInput(input, 0);
    long totalPrice = clawMachines.stream()
        .mapToLong(ClawMachine::cheapestPriceToWin)
        .sum();
    return String.valueOf(totalPrice);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<ClawMachine> clawMachines = fromInput(input, 10000000000000L);
    long totalPrice = clawMachines.stream()
        .mapToLong(ClawMachine::cheapestPriceToWin)
        .sum();
    return String.valueOf(totalPrice);
  }
}
