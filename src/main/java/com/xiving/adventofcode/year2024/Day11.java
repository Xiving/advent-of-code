package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day11 extends Year2024Day {

  public Day11() {
    super(11);
  }

  private static long recCountCached(long number, long count, int depth, Map<Long, Map<Integer, Long>> cache) {
    Map<Integer, Long> numberCache = cache.get(number);

    if (Objects.isNull(numberCache)) {
      numberCache = new HashMap<>();
      cache.put(number, numberCache);
    }

    long cachedResult = numberCache.getOrDefault(depth, -1L);

    if (cachedResult != -1) {
      return cachedResult + count;
    }

    long result = recCount(number, count, depth, cache);
    numberCache.put(depth, result - count);
    return result;
  }

  private static long recCount(long number, long count, int depth, Map<Long, Map<Integer, Long>> cache) {
    if (depth <= 0) {
      return count + 1L;
    }

    if (number == 0) {
      return recCountCached(1, count, depth - 1, cache);
    }

    int numberOfDigits = (int) (Math.log10(number) + 1);

    if (numberOfDigits % 2 == 0) {
      long x = (long) Math.pow(10, (double) numberOfDigits / 2);
      long rightValue = number % x;
      long countLeft = recCountCached((number - rightValue) / x, count, depth - 1, cache);
      return recCountCached(rightValue, countLeft, depth - 1, cache);
    } else {
      return recCountCached(number * 2024, count, depth - 1, cache);
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    List<Long> stones = Arrays.stream(input.getFirst().split(" "))
        .map(Long::valueOf)
        .toList();

    HashMap<Long, Map<Integer, Long>> cache = new HashMap<>();
    long total = 0;

    for (Long stone : stones) {
      total = recCount(stone, total, 25, cache);
    }

    return String.valueOf(total);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<Long> stones = Arrays.stream(input.getFirst().split(" "))
        .map(Long::valueOf)
        .toList();

    HashMap<Long, Map<Integer, Long>> cache = new HashMap<>();
    long total = 0;

    for (Long stone : stones) {
      total = recCountCached(stone, total, 75, cache);
    }

    return String.valueOf(total);
  }
}
