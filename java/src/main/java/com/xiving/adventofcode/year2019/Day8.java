package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day8 extends Year2019Day {

  public Day8() {
    super(8);
  }

  private static final int HEIGHT = 6;
  private static final int WIDTH = 25;

  private record Layer(int[][] layer) {
    int countOccurrences(int i) {
      int count = 0;

      for (int y = 0; y < layer.length; y++) {
        for (int x = 0; x < layer[0].length; x++) {
          if (layer[y][x] == i) {
            count++;
          }
        }
      }

      return count;
    }

    Layer stack(Layer other) {
      for (int y = 0; y < layer.length; y++) {
        for (int x = 0; x < layer[0].length; x++) {
          if (layer[y][x] == 2) {
            layer[y][x] = other.layer[y][x];
          }
        }
      }

      return this;
    }

    @Override
    public String toString() {
      StringBuilder str = new StringBuilder();

      for (int y = 0; y < layer.length; y++) {
        for (int x = 0; x < layer[0].length; x++) {
          str.append(layer[y][x] == 1? "█": " ");
        }

        str.append("\n");
      }

      str.deleteCharAt(str.length() - 1);
      return "\n" + str;
    }
  }

  private List<Layer> loadLayersFromInput(List<String> input) {
    List<Layer> layers = new ArrayList<>(input.getFirst().length() / HEIGHT / WIDTH);

    int x = -1;
    int y = 0;
    Layer currentLayer = new Layer(new int[HEIGHT][WIDTH]);
    layers.add(currentLayer);

    for(int i : input.getFirst().toCharArray()) {
      x++;

      if (x >= WIDTH) {
        x = 0;
        y++;

        if (y >= HEIGHT) {
          y = 0;
          currentLayer = new Layer(new int[HEIGHT][WIDTH]);
          layers.add(currentLayer);
        }
      }

      currentLayer.layer[y][x] = i - '0';
    }

    return layers;
  }

  @Override
  public String solvePartOne(List<String> input) {
    List<Layer> layers = loadLayersFromInput(input);
    Layer leastZero = layers.stream().min(Comparator.comparing(e -> e.countOccurrences(0))).get();
    return String.valueOf(leastZero.countOccurrences(1) * leastZero.countOccurrences(2));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<Layer> layers = loadLayersFromInput(input);
    Layer layer = layers.stream().reduce(Layer::stack).get();
    return layer.toString();
  }
}
