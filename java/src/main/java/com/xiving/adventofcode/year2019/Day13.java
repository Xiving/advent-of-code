package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 extends Year2019Day {

  public Day13() {
    super(13);
  }

  private static final int WIDTH = 35;
  private static final int HEIGHT = 23;

//  private static final int[] WINNING_KEY_PRESSES = {
//      1, 80
////      1, 80, -4, 155, -6, 30, -4, 50, 2, 200, 2, 200, -6, 90, 6, 200, -6, 100, 14, 500, -4, 200, 8, 100, -20, 270, 29, 10,
////      -10, 40,
////      14, 60,
////      -26, 20,
////      25, 10,
////      -25, 10,
////      1, 410,
////      10, 40,
////      -10, 20,
////      11, 8,
////      -10, 40,
////      5, 120,
////      2, 100,
////      9, 50,
////      -1, 15,
////      -21, 40,
////      22, 30,
////      -10, 55,
////      10, 18,
////      -22, 6,
////      26, 40
//  };

  private enum TileType {
    EMPTY(' '),
    WALL('X'),
    BLOCK('#'),
    HOR_PADDLE('-'),
    BALL('0');

    private char symbol;

    TileType(char c) {
      symbol = c;
    }

    @Override
    public String toString() {
      return String.valueOf(symbol);
    }

  }

  private record Tile(int x, int y, TileType type) {

  }

  @Override
  public String solvePartOne(List<String> input) {
    Intcode program = Intcode.ofInput(input.getFirst());
    List<Long> output = program.run().flushOutput();

    List<Tile> tiles = new ArrayList<>();

    for (int i = 0; i < output.size(); i += 3) {
      int x = Math.toIntExact(output.get(i));
      int y = Math.toIntExact(output.get(i + 1));
      int tileType = Math.toIntExact(output.get(i + 2));
      tiles.add(new Tile(x, y, TileType.values()[tileType]));
    }

    return String.valueOf(tiles.stream().filter(e -> e.type.equals(TileType.BLOCK)).count());
  }

  private String gridToString(TileType[][] grid) {
    StringBuilder str = new StringBuilder();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        str.append(grid[i][j].toString());
      }

      str.append('\n');
    }

    str.deleteCharAt(str.length() - 1);
    return str.toString();
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Intcode program = Intcode.ofInput(input.getFirst());
    program.setAddress(0, 2);
    List<Long> output = Collections.emptyList();

    int paddleX = 0;
    int ballX = 0;

    while (!program.hasHalted()) {
      int dir = Integer.compare(ballX, paddleX);
      output = program.run((long) dir).flushOutput();

      for (int j = 0; j < output.size(); j += 3) {
        int tileType = Math.toIntExact(output.get(j + 2));

        if (tileType == TileType.BALL.ordinal()) {
          ballX = Math.toIntExact(output.get(j));
        } else if (tileType == TileType.HOR_PADDLE.ordinal()) {
          paddleX = Math.toIntExact(output.get(j));
        }
      }
    }

    for (int i = 0; i < output.size(); i += 3) {
      if (output.get(i) == -1) {
        return String.valueOf(output.get(i + 2));
      }
    }

    return "";
  }
}
