package com.xiving.adventofcode.year2019;

import java.util.List;
import java.util.function.Function;

public class Day4 extends Year2019Day {

  public Day4() {
    super(4);
  }

  private static boolean someSameNeighbour(int[] number) {
    for (int i = 0; i < number.length - 1; i++) {
      if (number[i] == number[i + 1]) {
        return true;
      }
    }

    return false;
  }

  private static boolean singleSameNeighbour(int[] number) {
    for (int i = 0; i < number.length - 1; i++) {
      if (number[i] == number[i + 1]) {
        if ((i - 1 > 0 && number[i - 1] == number[i]) || (i + 2 < number.length && number[i] == number[i + 2])) {
          i++;
        } else {
          return true;
        }
      }
    }

    return false;
  }

  private static boolean lessEqThan(int[] arr1, int[] arr2) {
    for (int i = 0; i < arr1.length; i++) {
      if (arr1[i] < arr2[i]) {
        return true;
      } else if (arr1[i] > arr2[i]) {
        return false;
      }
    }

    return true;
  }

  private static int countMatching(int[] number, int[] limit, Function<int[], Boolean> matchFun) {
    int matches = 0;

    while (lessEqThan(number, limit)) {
      if (matchFun.apply(number)) {
        matches += 1;
      }

      for (int j = number.length - 1; j >= 0; j--) {
        if (number[j] != 9) {
          number[j]++;

          for (int i = j + 1; i < number.length; i++) {
            number[i] = number[j];
          }

          break;
        }
      }
    }

    return matches;
  }

  @Override
  public String solvePartOne(List<String> input) {
    int[] number = input.getFirst().substring(0, 6).chars().map(v -> v - '0').toArray();
    int[] limit = input.getFirst().substring(7, 13).chars().map(v -> v - '0').toArray();
    return String.valueOf(countMatching(number, limit, Day4::someSameNeighbour));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    int[] number = input.getFirst().substring(0, 6).chars().map(v -> v - '0').toArray();
    int[] limit = input.getFirst().substring(7, 13).chars().map(v -> v - '0').toArray();
    return String.valueOf(countMatching(number, limit, Day4::singleSameNeighbour));
  }
}
