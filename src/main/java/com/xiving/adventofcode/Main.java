package com.xiving.adventofcode;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<AdventDay> adventDays = AdventDayLoader.loadAllAdventDays();

    System.out.println("Solving advent days");

    for (AdventDay day : adventDays) {
      System.out.println(String.format("Year %d, Day %d:", day.year, day.day));
      System.out.println("\tPart 1: " + day.solvePartOneForInput());
      System.out.println("\tPart 2: " + day.solvePartTwoForInput());
    }
  }
}