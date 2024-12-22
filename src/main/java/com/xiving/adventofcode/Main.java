package com.xiving.adventofcode;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<AdventDay> adventDays = AdventDayLoader.loadAllAdventDays();

    System.out.println("Solving advent days");

    for (AdventDay day : adventDays) {
      if (day.day != 22) {
        continue;
      }

      System.out.println(String.format("Day %d:", day.day));
      Instant start = Instant.now();
      String part1 = day.solvePartOneForInput();
      Instant finish = Instant.now();
      long timeElapsedPart1 = Duration.between(start, finish).toMillis();

      System.out.printf("%5dms - Part 1: %s%n", timeElapsedPart1, part1);

      start = Instant.now();
      String part2 = day.solvePartTwoForInput();
      finish = Instant.now();
      long timeElapsedPart2 = Duration.between(start, finish).toMillis();

      System.out.printf("%5dms - Part 2: %s%n", timeElapsedPart2, part2);
    }
  }
}