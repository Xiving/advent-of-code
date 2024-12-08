package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Day7 extends Year2024Day {

  public Day7() {
    super(7);
  }

  private record Equation(long result, LongStream numbers) {

  }

  private static Equation fromInput(String input) {
    String[] resultAndNumbers = input.split(":");
    long result = Long.parseLong(resultAndNumbers[0]);
    LongStream numberStream = Arrays.stream(resultAndNumbers[1].split(" "))
        .filter(str -> !str.isBlank())
        .mapToLong(Long::parseLong);

    return new Equation(result, numberStream);
  }

  private static Stream<Long> applyFunToAll(List<BinaryOperator<Long>> functions, Stream<Long> partialResults, Long nextNumber) {
    return partialResults.flatMap(v -> functions.stream().map(function -> function.apply(v, nextNumber)));
  }

  private static boolean isValidEquation(Equation equation, List<BinaryOperator<Long>> operantFunctions) {
    return equation.numbers
        .boxed()
        .reduce(
            Stream.of(0L),
            (xs, x) ->
                applyFunToAll(operantFunctions, xs, x)
                    .filter(v -> v <= equation.result),
            Stream::concat
        )
        .anyMatch(e -> e == equation.result);
  }

  @Override
  public String solvePartOne(List<String> input) {
    List<BinaryOperator<Long>> allowedOperants = List.of(Math::addExact, Math::multiplyExact);

    long validEquationCount = input.stream()
        .map(Day7::fromInput)
        .filter(equation -> isValidEquation(equation, allowedOperants))
        .mapToLong(Equation::result)
        .sum();

    return String.valueOf(validEquationCount);
  }

  private static long getMultipleOf10Above(long value) {
    if (value < 100) {
      return value < 10 ? 10 : 100;
    } else {
      return value < 1000 ? 1000 : 10000;
    }
  }

  private static Long customOrFunction(Long leftValue, Long rightValue) {
    return leftValue * getMultipleOf10Above(rightValue) + rightValue;
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<BinaryOperator<Long>> allowedOperants = List.of(Math::addExact, Math::multiplyExact, Day7::customOrFunction);

    long validEquationCount = input.stream()
        .map(Day7::fromInput)
        .filter(equation -> isValidEquation(equation, allowedOperants))
        .mapToLong(Equation::result)
        .sum();

    return String.valueOf(validEquationCount);
  }
}
