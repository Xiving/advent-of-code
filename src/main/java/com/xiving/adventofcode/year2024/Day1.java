package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day1 extends Year2024Day {

  public Day1() {
    super(1);
  }

  @Override
  public String solvePartOne(String input) {
    List<Integer> leftIds = new ArrayList<>();
    List<Integer> rightIds = new ArrayList<>();

    Scanner scanner = new Scanner(input);

    while (scanner.hasNext()) {
      leftIds.add(scanner.nextInt());
      rightIds.add(scanner.nextInt());
    }

    Collections.sort(leftIds);
    Collections.sort(rightIds);

    int totalDifference = 0;

    for (int i = 0; i < leftIds.size(); i++) {
      totalDifference += Math.abs(leftIds.get(i) - rightIds.get(i));
    }

    return String.valueOf(totalDifference);
  }

  @Override
  public String solvePartTwo(String input) {
    List<Integer> leftValues = new ArrayList<>();
    List<Integer> rightValues = new ArrayList<>();

    Scanner scanner = new Scanner(input);

    while (scanner.hasNext()) {
      leftValues.add(scanner.nextInt());
      rightValues.add(scanner.nextInt());
    }

    Collections.sort(leftValues);
    Collections.sort(rightValues);

    int leftIdx = 0;
    int rightIdx = 0;

    int similarScore = 0;
    int similarCount = 0;

    while (leftIdx < leftValues.size() && rightIdx < rightValues.size()) {
      int left = leftValues.get(leftIdx);
      int right = rightValues.get(rightIdx);

      if (left < right) {
        similarScore += left * similarCount;
        similarCount = 0;
        leftIdx++;
      } else {
        if (left == right) {
          similarCount++;
        }

        rightIdx++;
      }
    }

    return String.valueOf(similarScore);
  }
}
