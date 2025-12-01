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

  private static final Map<Long, Map<Integer, Long>> cache = new HashMap<>();

  private static long calcStonesCached(long number, int depth) {
    Map<Integer, Long> numberCache = cache.get(number);

    if (Objects.isNull(numberCache)) {
      numberCache = new HashMap<>();
      cache.put(number, numberCache);
    }

    long cachedResult = numberCache.getOrDefault(depth, -1L);

    if (cachedResult != -1) {
      return cachedResult;
    }

    long count = caclStones(number, depth);
    numberCache.put(depth, count);
    return count;
  }

  private static long caclStones(long number, int depth) {
    if (depth <= 0) {
      return 1L;
    }

    if (number == 0) {
      return calcStonesCached(1, depth - 1);
    }

    int digits = (int) (Math.log10(number) + 1);

    if (digits % 2 == 0) {
      long divisor = (long) Math.pow(10, (double) digits / 2);
      long rightValue = number % divisor;
      long count = calcStonesCached((number - rightValue) / divisor, depth - 1);
      count += calcStonesCached(rightValue, depth - 1);
      return count;
    } else {
      return calcStonesCached(number * 2024, depth - 1);
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    Long total = Arrays.stream(input.getFirst().split(" "))
        .map(Long::valueOf)
        .mapToLong(stone -> calcStonesCached(stone, 25))
        .sum();

    return String.valueOf(total);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Long total = Arrays.stream(input.getFirst().split(" "))
        .map(Long::valueOf)
        .mapToLong(stone -> calcStonesCached(stone, 75))
        .sum();

    return String.valueOf(total);
  }
}
