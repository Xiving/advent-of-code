package com.xiving.adventofcode.year2019;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.graalvm.collections.Pair;

public class Day11 extends Year2019Day {

  public Day11() {
    super(11);
  }

  private static class Robot {

    private int xDir;
    private int yDir;
    private int xPos;
    private int yPos;

    private Intcode brain;
    private Map<Pair<Integer, Integer>, Boolean> painted;

    Robot(Intcode brain, Map painted) {
      this.xDir = 0;
      this.yDir = -1;
      this.xPos = 0;
      this.yPos = 0;
      this.brain = brain;
      this.painted = painted;
    }

    private void turnRight() {
      if (xDir == 0) {
        xDir = yDir * -1;
        yDir = 0;
      } else {
        yDir = xDir;
        xDir = 0;
      }
    }

    private void turnLeft() {
      if (xDir == 0) {
        xDir = yDir;
        yDir = 0;
      } else {
        yDir = xDir * -1;
        xDir = 0;
      }
    }

    private void walk() {
      xPos += xDir;
      yPos += yDir;
    }

    private void run() {
      while (!brain.hasHalted()) {
        Pair<Integer, Integer> pos = Pair.create(xPos, yPos);
        List<Long> output = brain.run(painted.getOrDefault(pos, false) ? 1L : 0L).flushOutput();
        painted.put(pos, output.getFirst() == 1);

        if (output.get(1) == 0) {
          turnLeft();
        } else {
          turnRight();
        }

        walk();
      }
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    Map<Pair<Integer, Intcode>, Boolean> painter = new HashMap<>();
    Intcode brain = Intcode.ofInput(input.getFirst());
    Robot robot = new Robot(brain,painter);
    robot.run();
    return String.valueOf(painter.values().size());
  }

  private boolean[][] rasterize(Map<Pair<Integer, Integer>, Boolean> map) {
    int minX = 0;
    int maxX = 0;
    int minY = 0;
    int maxY = 0;

    for (Pair<Integer, Integer> pos :map.keySet()) {
      minX = Math.min(pos.getLeft(), minX);
      maxX = Math.max(pos.getLeft(), maxX);
      minY = Math.min(pos.getRight(), minY);
      maxY = Math.max(pos.getRight(), maxY);
    }

    final int offsetX = minX * -1;
    final int offsetY = minY * -1;

    boolean[][] raster = new boolean[offsetY + maxY + 1][offsetX + maxX + 1];
    map.forEach((k, v) -> raster[k.getRight() + offsetY][k.getLeft() + offsetX] = v);
    return raster;
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Map<Pair<Integer, Integer>, Boolean> painted = new HashMap<>();
    painted.put(Pair.create(0, 0), true);

    Intcode brain = Intcode.ofInput(input.getFirst());
    Robot robot = new Robot(brain, painted);
    robot.run();

    boolean[][] panel = rasterize(robot.painted);

    StringBuilder str = new StringBuilder();

    for (int y = 0; y < panel.length; y++) {
      for (int x = 0; x < panel[0].length; x++) {
        str.append(panel[y][x]? "█": " ");
      }

      str.append("\n");
    }

    str.deleteCharAt(str.length() - 1);
    return "\n" + str;
  }
}
