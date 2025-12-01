package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day19 extends Year2024Day {

  public Day19() {
    super(19);
  }

  private static int normalizeColour(char c) {
    return switch (c) {
      case 'w' -> 0;
      case 'u' -> 1;
      case 'b' -> 2;
      case 'r' -> 3;
      case 'g' -> 4;
      default -> throw new IllegalStateException("Unexpected value: " + c);
    };
  }

  private static int[] normalizeTowelColours(String towelColour) {
    char[] chars = towelColour.toCharArray();
    int[] normalized = new int[chars.length];

    for (int i = 0; i < chars.length; i++) {
      normalized[i] = normalizeColour(chars[i]);
    }

    return normalized;
  }

  private static class TowelPatterns {

    boolean matchEnd;
    TowelPatterns[] characterMatches;

    TowelPatterns() {
      this.matchEnd = false;
      this.characterMatches = new TowelPatterns[5];
    }

    void addPattern(int[] pattern) {
      addPattern(pattern, 0);
    }

    void addPattern(int[] pattern, int fromIndex) {
      if (fromIndex >= pattern.length) {
        matchEnd = true;
        return;
      }

      TowelPatterns towelPatterns = this.characterMatches[pattern[fromIndex]];

      if (Objects.isNull(towelPatterns)) {
        this.characterMatches[pattern[fromIndex]] = towelPatterns = new TowelPatterns();
      }

      towelPatterns.addPattern(pattern, fromIndex + 1);
    }

    long countMatches(int[] towelDesign) {
      long[] matchCountCache = new long[towelDesign.length];
      Arrays.fill(matchCountCache, -1);
      return countMatches(towelDesign, 0, this, matchCountCache);
    }

    private long countMatches(int[] towelDesign, int index, TowelPatterns patternStart, long[] matchCountCache) {
      if (index == towelDesign.length) {
        return matchEnd ? 1 : 0;
      } else {
        long count = 0;

        if (this.matchEnd) {
          long countFromIndex = (matchCountCache[index] != -1)
              ? matchCountCache[index]
              : patternStart.countMatches(towelDesign, index, patternStart, matchCountCache);
          matchCountCache[index] = countFromIndex;
          count += countFromIndex;
        }

        TowelPatterns towelPatterns = this.characterMatches[towelDesign[index]];

        if (!Objects.isNull(towelPatterns)) {
          count += towelPatterns.countMatches(towelDesign, index + 1, patternStart, matchCountCache);
        }

        return count;
      }
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    TowelPatterns towelPatterns = new TowelPatterns();
    Arrays.stream(input.getFirst().split(", "))
        .map(Day19::normalizeTowelColours)
        .forEach(towelPatterns::addPattern);

    long matches = input.stream().skip(2)
        .map(Day19::normalizeTowelColours)
        .filter(design -> towelPatterns.countMatches(design) > 0)
        .count();

    return String.valueOf(matches);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    TowelPatterns towelPatterns = new TowelPatterns();
    Arrays.stream(input.getFirst().split(", "))
        .map(Day19::normalizeTowelColours)
        .forEach(towelPatterns::addPattern);

    long countTotalMatches = input.stream().skip(2)
        .map(Day19::normalizeTowelColours)
        .mapToLong(towelPatterns::countMatches)
        .sum();

    return String.valueOf(countTotalMatches);
  }
}
