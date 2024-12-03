package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day2 extends Year2024Day {


  public Day2() {
    super(2);
  }

  private static boolean isUnsafe(int a, int b, boolean asc) {
    return Objects.equals(a, b) || asc != a < b || Math.abs(a - b) > 3;
  }

  private static boolean safeReport(Integer[] report) {
    return safeReport(report, -1);
  }

  private static boolean safeReport(Integer[] report, int ignoreI) {
    if (report.length < 2) {
      return true;
    } else if (ignoreI >= 0 && ignoreI < report.length && report.length == 2) {
      return true;
    }

    boolean asc = (ignoreI == 0 || ignoreI == 1)
        ? ignoreI == 0
        ? report[1] < report[2]
        : report[0] < report[2]
        : report[0] < report[1];

    int startI = ignoreI == 0 ? 1 : 0;
    int endI = ignoreI == report.length - 1 ? report.length - 2 : report.length - 1;

    for (int i = startI; i < endI; i++) {
      if (ignoreI == i + 1) {
        if (isUnsafe(report[i], report[i + 2], asc)) {
          return false;
        }

        i++;
        continue;
      }

      if (isUnsafe(report[i], report[i + 1], asc)) {
        return false;
      }
    }

    return true;
  }

  private static boolean safeReportFaultToleranceBruteForce(Integer[] report) {
    if (safeReport(report)) {
      return true;
    }

    for (int i = 0; i < report.length; i++) {
      if (safeReport(report, i)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public String solvePartOne(List<String> input) {
    long validCount = input.stream()
        .map(line -> Arrays.stream(line.split("\\s+"))
            .map(Integer::valueOf)
            .toArray(Integer[]::new))
        .filter(Day2::safeReport)
        .count();

    return String.valueOf(validCount);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    long validCount = input.stream()
        .map(line -> Arrays.stream(line.split("\\s+"))
            .map(Integer::valueOf)
            .toArray(Integer[]::new))
        .filter(Day2::safeReportFaultToleranceBruteForce)
        .count();

    return String.valueOf(validCount);
  }
}
