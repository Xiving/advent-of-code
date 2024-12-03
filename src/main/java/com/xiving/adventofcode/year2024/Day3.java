package com.xiving.adventofcode.year2024;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 extends Year2024Day {

  private static final Pattern MUL_REGEX = Pattern.compile("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");
  private static final Pattern MUL_DO_DONT_REGEX = Pattern.compile("(don't\\(\\))|(do\\(\\))|mul\\(([0-9]{1,3}),([0-9]{1,3})\\)");

  public Day3() {
    super(3);
  }

  @Override
  public String solvePartOne(List<String> input) {
    int total = input.stream()
        .map(MUL_REGEX::matcher)
        .flatMap(Matcher::results)
        .mapToInt(e -> Integer.parseInt(e.group(1)) * Integer.parseInt(e.group(2)))
        .sum();
    return String.valueOf(total);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    boolean ignore = false;

    List<MatchResult> matches = input.stream()
        .map(MUL_DO_DONT_REGEX::matcher)
        .flatMap(Matcher::results)
        .collect(Collectors.toList());

    int total = 0;

    for (MatchResult match : matches) {
      if (!Objects.isNull(match.group(1))) {
        ignore = true;
      } else if (!Objects.isNull(match.group(2))) {
        ignore = false;
      } else {
        if (!ignore) {
          total += Integer.parseInt(match.group(3)) * Integer.parseInt(match.group(4));
        }
      }
    }

    return String.valueOf(total);
  }
}
