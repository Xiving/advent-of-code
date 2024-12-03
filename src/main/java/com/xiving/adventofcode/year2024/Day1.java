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
  public String solvePartOne(List<String> input) {
    List<Integer> leftValues = new ArrayList<>(input.size());
    List<Integer> rightValues = new ArrayList<>(input.size());

    for (String numberStr : input) {
      String[] strArr = numberStr.split("\\s+");
      leftValues.add(Integer.valueOf(strArr[0]));
      rightValues.add(Integer.valueOf(strArr[1]));
    }

    Collections.sort(leftValues);
    Collections.sort(rightValues);

    int totalDifference = 0;

    for (int i = 0; i < leftValues.size(); i++) {
      totalDifference += Math.abs(leftValues.get(i) - rightValues.get(i));
    }

    return String.valueOf(totalDifference);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<Integer> leftValues = new ArrayList<>(input.size());
    List<Integer> rightValues = new ArrayList<>(input.size());

    for (String numberStr : input) {
      String[] strArr = numberStr.split("\\s+");
      leftValues.add(Integer.valueOf(strArr[0]));
      rightValues.add(Integer.valueOf(strArr[1]));
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
