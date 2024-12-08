package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Day7 extends Year2024Day {

  public Day7() {
    super(7);
  }

  private record Equation(long result, long[] numbers) {

  }

  private static Equation fromInput(String input) {
    String[] resultAndNumbers = input.split(":");
    long result = Long.parseLong(resultAndNumbers[0]);
    long[] numbers = Arrays.stream(resultAndNumbers[1].split(" "))
        .filter(str -> !str.isBlank())
        .mapToLong(Long::parseLong)
        .toArray();

    return new Equation(result, numbers);
  }

  private static List<Long> applyFunToAll(List<BinaryOperator<Long>> functions, Stream<Long> partialResults, Long nextNumber) {
    return partialResults
        .flatMap(v -> functions.stream().map(function -> function.apply(v, nextNumber)))
        .toList();
  }

  private static boolean isValidEquation(Equation equation, List<BinaryOperator<Long>> allowedOperantFunctions) {
    List<Long> possibleResults = Arrays.stream(equation.numbers).boxed()
        .reduce(
            Stream.of(0L),
            (xs, x) -> applyFunToAll(allowedOperantFunctions, xs, x)
                .stream()
                .filter(v -> v <= equation.result),
            Stream::concat)
        .toList();
    return possibleResults.contains(equation.result);
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

  private static Long customOrFunction(Long leftValue, Long rightValue) {
    return Long.parseLong(leftValue.toString() + rightValue.toString());
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
