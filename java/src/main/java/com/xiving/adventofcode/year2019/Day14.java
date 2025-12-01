package com.xiving.adventofcode.year2019;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.graalvm.collections.Pair;

public class Day14 extends Year2019Day {

  private static final Pattern INGREDIENT_PATTERN = Pattern.compile("(\\d+) (\\w+)");

  public Day14() {
    super(14);
  }

  private record Recipe(
      String name,
      long singleAmount,
      List<Pair<Long, String>> singleIngredients,
      List<Pair<Long, String>> totalIngredients
  ) {

    static Recipe fromString(String input) {
      ArrayList<Pair<Long, String>> ingredients = INGREDIENT_PATTERN.matcher(input).results()
          .map(result -> Pair.create(Long.parseLong(result.group(1)), result.group(2)))
          .collect(Collectors.toCollection(ArrayList::new));

      Pair<Long, String> recipeResult = ingredients.removeLast();
      return new Recipe(recipeResult.getRight(), recipeResult.getLeft(), ingredients, null);
    }

    List<Pair<Long, String>> calcNeeded(long neededAmount) {
      double recipeAmount = Math.ceil((double) neededAmount / singleAmount);

      return this.singleIngredients.stream()
          .map(v -> Pair.create((long) (v.getLeft() * recipeAmount), v.getRight()))
          .toList();
    }
  }

  private record ExtraIngredientNeeded(String recipeName, long extraAmount) {

  }

  private static long oreNeededForFuel(long fuel, Map<String, Recipe> singleRecipes) {
    Map<String, Pair<Long, Recipe>> recipes = singleRecipes.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey, e -> Pair.create(0L, e.getValue())));

    ArrayDeque<ExtraIngredientNeeded> extraNeeded = new ArrayDeque<>();
    extraNeeded.add(new ExtraIngredientNeeded("FUEL", fuel));

    ExtraIngredientNeeded oreNeeded = new ExtraIngredientNeeded("ORE", 0);

    while (!extraNeeded.isEmpty()) {
      ExtraIngredientNeeded needed = extraNeeded.pop();

      if (Objects.equals(needed.recipeName, "ORE")) {
        oreNeeded = new ExtraIngredientNeeded(needed.recipeName, oreNeeded.extraAmount + needed.extraAmount);
        continue;
      }

      Pair<Long, Recipe> recipeNeeded = recipes.get(needed.recipeName);
      long oldRecipeNeeded = recipeNeeded.getLeft();
      Recipe recipe = recipeNeeded.getRight();

      long ingredientNeeded = oldRecipeNeeded + needed.extraAmount;

      List<Pair<Long, String>> newIngredientsNeeded = recipe.calcNeeded(ingredientNeeded);
      List<Pair<Long, String>> oldIngredientsNeeded = recipe.totalIngredients == null
          ? recipeNeeded.getRight().calcNeeded(0L)
          : recipe.totalIngredients();

      for (int i = 0; i < oldIngredientsNeeded.size(); i++) {
        var oldNeeded = oldIngredientsNeeded.get(i);
        var newNeeded = newIngredientsNeeded.get(i);
        long increase = newNeeded.getLeft() - oldNeeded.getLeft();

        if (increase > 0) {
          extraNeeded.add(new ExtraIngredientNeeded(oldNeeded.getRight(), increase));
        }
      }

      Recipe newRecipe = new Recipe(recipe.name, recipe.singleAmount, recipe.singleIngredients, newIngredientsNeeded);
      recipes.put(newRecipe.name, Pair.create(ingredientNeeded, newRecipe));
    }

    return oreNeeded.extraAmount;
  }

  @Override
  public String solvePartOne(List<String> input) {
    Map<String, Recipe> recipes = input.stream()
        .map(Recipe::fromString)
        .collect(Collectors.toMap(Recipe::name, e -> e));

    long oreNeeded = oreNeededForFuel(1L, recipes);
    return String.valueOf(oreNeeded);
  }

  private static final long ORE_TARGET = 1000000000000L;

  @Override
  public String solvePartTwo(List<String> input) {
    Map<String, Recipe> recipes = input.stream()
        .map(Recipe::fromString)
        .collect(Collectors.toMap(Recipe::name, e -> e));

    long minFuelAmount = 1L;
    long maxFuelAmount = Long.MAX_VALUE;
    long oreNeeded = oreNeededForFuel(minFuelAmount, recipes);

    while (minFuelAmount + 1 != maxFuelAmount) {
      long fuelAmount = (maxFuelAmount == Long.MAX_VALUE
          ? ((ORE_TARGET / oreNeeded) + 1) * minFuelAmount
          : (minFuelAmount + maxFuelAmount) / 2);

      oreNeeded = oreNeededForFuel(fuelAmount, recipes);

      if (oreNeeded > ORE_TARGET) {
        maxFuelAmount = fuelAmount;
      } else {
        minFuelAmount = fuelAmount;
      }
    }

    return String.valueOf(minFuelAmount);
  }
}
