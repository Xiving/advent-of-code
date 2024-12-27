package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 extends Year2024Day {

  public Day24() {
    super(24);
  }

  private static final Pattern EQUATION = Pattern.compile("(\\w*) (XOR|OR|AND) (\\w*) -> (\\w*)");

  private static final Map<String, Expression> expressions = new HashMap<>();

  private enum BooleanOp {OR, AND, XOR}

  private sealed interface Expression permits Constant, Equation {

    boolean solve();

  }

  private record Constant(boolean constant) implements Expression {

    @Override
    public boolean solve() {
      return constant;
    }

  }

  private record Equation(String left, BooleanOp op, String right, String variable) implements Expression {

    @Override
    public boolean solve() {
      boolean leftValue = expressions.get(left).solve();
      boolean rightValue = expressions.get(right).solve();

      boolean result = switch (op) {
        case OR -> leftValue || rightValue;
        case AND -> leftValue && rightValue;
        case XOR -> leftValue ^ rightValue;
      };

      expressions.put(variable, new Constant(result));
      return result;
    }

  }


  private static Equation[] initializeFromInput(List<String> input) {
    int i = 0;

    while (!input.get(i).isBlank()) {
      String constant = input.get(i);
      expressions.put(constant.substring(0, 3), new Constant(constant.charAt(5) != '0'));
      i++;
    }

    i++;
    List<Equation> zEquations = new ArrayList<>();

    while (i < input.size()) {
      Matcher match = EQUATION.matcher(input.get(i));
      match.matches();
      Equation equation = new Equation(match.group(1), BooleanOp.valueOf(match.group(2)), match.group(3), match.group(4));
      expressions.put(equation.variable, equation);

      if (equation.variable.matches("z.*")) {
        zEquations.add(equation);
      }

      i++;
    }

    Equation[] equations = new Equation[zEquations.size()];

    for (Equation zEquation : zEquations) {
      equations[Integer.parseInt(zEquation.variable.substring(1, 3))] = zEquation;
    }

    return equations;
  }

  @Override
  public String solvePartOne(List<String> input) {
    Equation[] zEquations = initializeFromInput(input);

    long result = 0;

    for (int i = zEquations.length - 1; i >= 0; i--) {
      result = (result << 1) + (zEquations[i].solve() ? 1 : 0);
    }

    return String.valueOf(result);
  }

  private static int validInputOp(Equation eq, BooleanOp op) {
    if ((op != null && eq.op != op) || !eq.left.regionMatches(1, eq.right, 1, 2)) {
      return -1;
    }

    char l = eq.left.charAt(0);
    char r = eq.right.charAt(0);
    return ((l == 'y' && r == 'x') || (l == 'x' && r == 'y')) ? Integer.parseInt(eq.left.substring(1, 3)) : -1;
  }

  private static List<String> baseCase(Equation zEq0) {
    if (validInputOp(zEq0, BooleanOp.XOR) == 0) {
      return new ArrayList<>();
    }

    return new ArrayList<>(Arrays.asList(zEq0.variable));
  }

  private static Equation unfold(String l, String r, BooleanOp op, int n, BooleanOp target, List<String> invalid) {
    if (expressions.get(l) instanceof Equation eq1 && expressions.get(r) instanceof Equation eq2) {
      if (validInputOp(eq1, op) == n) {
        return eq2;
      } else if (validInputOp(eq2, op) == n) {
        return eq1;
      } else {
        if (eq1.op == target) {
          invalid.add(eq2.variable);
          return eq1;
        } else if (eq2.op == target) {
          invalid.add(eq1.variable);
          return eq2;
        }
      }
    }

    throw new RuntimeException();
  }

  private static List<String> nCase(Equation zEqN, int n) {
    if (zEqN.op != BooleanOp.XOR) {
      return List.of(zEqN.variable);
    }

    List<String> invalid = new ArrayList<>();
    Equation orEq = unfold(zEqN.left, zEqN.right, BooleanOp.XOR, n, BooleanOp.OR, invalid);

    if (n != 1) {
      if (orEq.op != BooleanOp.OR) {
        invalid.add(orEq.variable);
        return invalid;
      }

      Equation andEq = unfold(orEq.left, orEq.right, BooleanOp.AND, n - 1, BooleanOp.AND, invalid);

      if (andEq.op != BooleanOp.AND) {
        invalid.add(andEq.variable);
      }
    } else {
      if (validInputOp(orEq, BooleanOp.AND) != 0) {
        invalid.add(orEq.variable);
      }
    }

    return invalid;
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Equation[] zEquations = initializeFromInput(input);

    List<String> invalid = baseCase(zEquations[0]);

    for (int i = 1; i < zEquations.length - 1; i++) {
      invalid.addAll(nCase(zEquations[i], i));
    }

    return invalid.stream()
        .sorted()
        .collect(Collectors.joining(","));
  }
}
