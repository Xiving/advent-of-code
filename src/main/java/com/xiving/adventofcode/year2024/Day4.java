package com.xiving.adventofcode.year2024;

import java.util.List;

public class Day4 extends Year2024Day {

  private static final char[] XMAS = "XMAS".toCharArray();
  private static final char[] MAS = "MAS".toCharArray();

  public Day4() {
    super(4);
  }

  private boolean wordMatchesInDirection(char[][] wordMatrix, char[] word, int y, int x, int yDir, int xDir) {
    if (x < 0 || y < 0 || y >= wordMatrix.length || x >= wordMatrix[0].length) {
      return false;
    }

    int endX = x + xDir * (word.length - 1);
    int endY = y + yDir * (word.length - 1);

    if (endX < 0 || endY < 0 || endY >= wordMatrix.length || endX >= wordMatrix[0].length) {
      return false;
    }

    for (int i = 0; i < word.length; i ++) {
      if (wordMatrix[y + yDir * i][x + xDir * i] != word[i]) {
        return false;
      }
    }

    return true;
  }

  private long countMatchesAllDirections(char[][] wordMatrix, char[] word, int y, int x) {
    long matches = 0;

    for (int yDir = -1; yDir <= 1; yDir++) {
      for (int xDir = -1; xDir <= 1; xDir++) {
        if (yDir == 0 & xDir == 0) {
          continue;
        }

        if (wordMatchesInDirection(wordMatrix, word, y, x, yDir, xDir)) {
          matches++;
        }
      }
    }

    return matches;
  }

  private long countMatchesX(char[][] wordMatrix, char[] word, int y, int x) {
    long matches = 0;

    if (wordMatchesInDirection(wordMatrix, word, y, x, 1, 1)) {
      if (wordMatchesInDirection(wordMatrix, word, y + (word.length - 1), x, -1, 1)) {
        matches++;
      }

      if (wordMatchesInDirection(wordMatrix, word, y, x + (word.length - 1), 1, -1)) {
        matches++;
      }
    }

    if (wordMatchesInDirection(wordMatrix, word, y, x, -1, -1)) {
      if (wordMatchesInDirection(wordMatrix, word, y, x - (word.length - 1), -1, 1)) {
        matches++;
      }

      if (wordMatchesInDirection(wordMatrix, word, y - (word.length - 1), x, 1, -1)) {
        matches++;
      }
    }

    return matches;
  }

  @Override
  public String solvePartOne(List<String> input) {
    char[][] wordMatrix = input.stream().map(String::toCharArray).toArray(char[][]::new);

    long count = 0;

    for (int y = 0; y < wordMatrix.length; y++) {
      for (int x = 0; x < wordMatrix[0].length; x++) {
        count += countMatchesAllDirections(wordMatrix, XMAS, y, x);
      }
    }

    return String.valueOf(count);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    char[][] wordMatrix = input.stream().map(String::toCharArray).toArray(char[][]::new);

    long count = 0;

    for (int y = 0; y < wordMatrix.length; y++) {
      for (int x = 0; x < wordMatrix[0].length; x++) {
        count += countMatchesX(wordMatrix, MAS, y, x);
      }
    }

    return String.valueOf(count);
  }
}
