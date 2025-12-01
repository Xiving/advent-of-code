package com.xiving.adventofcode;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {

  private static final boolean DEBUG = false;

  public static void main(String[] args) {
    List<AdventDay> adventDays = AdventDayLoader.loadAllAdventDays();

    System.out.println("Solving advent days");

    for (AdventDay day : adventDays) {
      System.out.println(String.format("┌ %d ─ Day %2d", day.year, day.day));
      Instant start = Instant.now();
      String part1 = day.solvePartOneForInput();
      Instant finish = Instant.now();
      long timeElapsedPart1 = Duration.between(start, finish).toMillis();

      System.out.printf("│ %5dms) Part 1: %s%n", timeElapsedPart1, part1);

      start = Instant.now();
      String part2 = day.solvePartTwoForInput();
      finish = Instant.now();
      long timeElapsedPart2 = Duration.between(start, finish).toMillis();

      System.out.printf("└ %5dms) Part 2: %s%n%n", timeElapsedPart2, part2);
    }
  }

  public static void debug(String format, Object... args) {
    if (DEBUG) {
      System.out.printf(format + "%n", args);
    }
  }
}
