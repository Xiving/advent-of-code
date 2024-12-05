package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day5 extends Year2024Day {

  public Day5() {
    super(5);
  }

  private static List<Integer> orderCorrectly(Map<Integer, List<Integer>> orderingRules, List<Integer> update) {
    if (update.size() <= 1) {
      return update;
    }

    List<Integer> correctOrder = new ArrayList<>();
    correctOrder.add(update.get(0));

    loop: for (int i = 1; i < update.size(); i++) {
      Integer value = update.get(i);
      List<Integer> valueRules = orderingRules.getOrDefault(value, Collections.emptyList());

      for (int j = 0; j < correctOrder.size(); j++) {
        if (valueRules.contains(correctOrder.get(j))) {
          correctOrder.add(j, value);
          continue loop;
        }
      }

      correctOrder.add(value);
    }

    return correctOrder;
  }

  private static boolean updateIsCorrect(Map<Integer, List<Integer>> orderingRules, List<Integer> update) {
    if (update.size() <= 1) {
      return true;
    }

    List<Integer> traversed = new ArrayList<>();
    traversed.add(update.get(0));

    for (int i = 1; i < update.size(); i++) {
      for (Integer rule : orderingRules.getOrDefault(update.get(i), Collections.emptyList())) {
        if (traversed.contains(rule)) {
          return false;
        }
      }

      traversed.add(update.get(i));
    }

    return true;
  }

  private static Map<Integer, List<Integer>> orderingRulesFromInput(List<String> input) {
    Map<Integer, List<Integer>> orderingRules = new HashMap<>();

    for (String str : input) {
      int[] rule = Arrays.stream(str.split("\\|")).mapToInt(Integer::valueOf).toArray();
      List<Integer> rules = orderingRules.getOrDefault(rule[0], new ArrayList<>());
      rules.add(rule[1]);
      orderingRules.put(rule[0], rules);
    }

    return orderingRules;
  }

  @Override
  public String solvePartOne(List<String> input) {
    Map<Boolean, List<String>> isOrderingRules = input.stream().collect(Collectors.partitioningBy(str -> str.contains("|")));
    Map<Integer, List<Integer>> orderingRules = orderingRulesFromInput(isOrderingRules.get(true));

    int result = isOrderingRules.get(false).stream()
        .filter(str -> !str.isBlank())
        .map(update -> Arrays.stream(update.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
        .filter(update -> updateIsCorrect(orderingRules, update))
        .mapToInt(update -> update.get(update.size() / 2))
        .sum();

    return String.valueOf(result);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Map<Boolean, List<String>> isOrderingRules = input.stream().collect(Collectors.partitioningBy(str -> str.contains("|")));
    Map<Integer, List<Integer>> orderingRules = orderingRulesFromInput(isOrderingRules.get(true));

    int result = isOrderingRules.get(false).stream()
        .filter(str -> !str.isBlank())
        .map(update -> Arrays.stream(update.split(",")).map(Integer::valueOf).collect(Collectors.toList()))
        .filter(update -> !updateIsCorrect(orderingRules, update))
        .map(incorrectUpdate -> orderCorrectly(orderingRules, incorrectUpdate))
        .mapToInt(update -> update.get(update.size() / 2))
        .sum();

    return String.valueOf(result);
  }
}
