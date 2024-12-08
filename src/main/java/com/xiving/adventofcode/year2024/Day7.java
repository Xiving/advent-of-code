package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class Day7 extends Year2024Day {

  public Day7() {
    super(7);
  }

  private record Equation(long result, Stream<String> constants) {

  }

  private static Equation fromInput(String input) {
    String[] resultAndConstants = input.split(":");
    long result = Long.parseLong(resultAndConstants[0]);
    Stream<String> constantStream = Arrays
        .stream(resultAndConstants[1].split(" "))
        .filter(str -> !str.isBlank());

    return new Equation(result, constantStream);
  }

  private static boolean isValidEquation(Equation equation, BiFunction<Long, String, Stream<Long>> operantsFun) {
    return equation.constants
        .reduce(
            Stream.of(0L),
            (leftNumbers, rightNumberStr) -> leftNumbers
                .flatMap(leftNumber -> operantsFun.apply(leftNumber, rightNumberStr))
                .filter(v -> v <= equation.result),
            Stream::concat)
        .anyMatch(e -> e == equation.result);
  }

  @Override
  public String solvePartOne(List<String> input) {
    BiFunction<Long, String, Stream<Long>> operantsFun = (leftLong, rightStr) -> {
      Long rightLong = Long.parseLong(rightStr);
      return Stream.of(Math.addExact(leftLong, rightLong), Math.multiplyExact(leftLong, rightLong));
    };

    long validEquationCount = input.stream()
        .map(Day7::fromInput)
        .filter(equation -> isValidEquation(equation, operantsFun))
        .mapToLong(Equation::result)
        .sum();

    return String.valueOf(validEquationCount);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    BiFunction<Long, String, Stream<Long>> operantsFun = (leftLong, rightStr) -> {
      Long rightLong = Long.parseLong(rightStr);
      long rightDigitCount = rightStr.length();

      return Stream.of(
          Math.addExact(leftLong, rightLong),
          Math.multiplyExact(leftLong, rightLong),
          leftLong * (long) Math.pow(10, rightDigitCount) + rightLong
      );
    };

    long validEquationCount = input.stream()
        .map(Day7::fromInput)
        .filter(equation -> isValidEquation(equation, operantsFun))
        .mapToLong(Equation::result)
        .sum();

    return String.valueOf(validEquationCount);
  }
}
