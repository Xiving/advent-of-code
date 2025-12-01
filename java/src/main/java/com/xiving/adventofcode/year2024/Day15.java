package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day15 extends Year2024Day {

  public Day15() {
    super(15);
  }

  private static class Warehouse {

    char[][] map;
    int xScale;
    int robotX;
    int robotY;

    Warehouse(List<String> input, int xScale) {
      this.xScale = xScale;
      this.map = new char[input.size()][];

      for (int i = 0; i < map.length; i++) {
        char[] line = input.get(i).toCharArray();
        this.map[i] = new char[line.length * xScale];

        for (int j = 0; j < line.length; j++) {
          for (int k = 1; k < xScale; k++) {
            this.map[i][j * xScale + k] = '.';
          }

          this.map[i][j * xScale] = line[j];

          if (line[j] == '@') {
            map[i][j * xScale] = '.';
            this.robotX = j * xScale;
            this.robotY = i;
          }
        }
      }
    }

    void moveVer(int dir) {
      int rowY = robotY;
      Stack<Set<Integer>> boxesPerRow = new Stack<>();
      Set<Integer> currentRow = Set.of(robotX);

      while (!currentRow.isEmpty()) {
        rowY += dir;
        boxesPerRow.push(currentRow);
        currentRow = new HashSet<>();

        for (Integer boxX : boxesPerRow.peek()) {
          int maxX = rowY - dir == robotY? (robotX + 1): boxX + xScale;
          for (int x = boxX - xScale + 1; x < maxX; x++) {
            if (map[rowY][x] == '#') {
              return;
            } else if (map[rowY][x] == 'O') {
              currentRow.add(x);
            }
          }
        }
      }

      while (!boxesPerRow.isEmpty()) {
        rowY -= dir;

        for (Integer boxX : boxesPerRow.pop()) {
          map[rowY + dir][boxX] = 'O';
          map[rowY][boxX] = '.';
        }
      }

      map[robotY][robotX] = '.';
      robotY += dir;
      map[robotY][robotX] = '@';
    }

    void movHor(int dir) {
      int scaledDir = dir * xScale;
      int xPos = dir > 0? robotX + 1: robotX + scaledDir;

      while (map[robotY][xPos] != '#') {
        if (map[robotY][xPos] == '.') {
          xPos -= scaledDir;

          while (xPos != robotX && xPos != robotX - dir) {
            map[robotY][xPos + dir] = 'O';
            map[robotY][xPos] = '.';
            xPos -= scaledDir;
          }

          map[robotY][robotX] = '.';
          robotX += dir;
          map[robotY][robotX] = '@';
          return;
        }

        xPos += scaledDir;
      }
    }

    void move(char c) {
      int xDir = c == '<' ? -1 : (c == '>' ? 1 : 0);

      if (xDir != 0) {
        movHor(xDir);
      } else {
        moveVer(c == '^' ? -1 : (c == 'v' ? 1 : 0));
      }
    }

    long boxesGpsSum() {
      long sum = 0;

      for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] == 'O') {
            sum += 100L * i + j;
          }
        }
      }

      return sum;
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    Map<Boolean, List<String>> isMapLine = input.stream().collect(Collectors.partitioningBy(str -> str.contains("#")));
    Warehouse warehouse = new Warehouse(isMapLine.get(true), 1);

    isMapLine.get(false).stream()
        .flatMapToInt(String::chars)
        .forEach(c -> warehouse.move((char) c));

    return String.valueOf(warehouse.boxesGpsSum());
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Map<Boolean, List<String>> isMapLine = input.stream().collect(Collectors.partitioningBy(str -> str.contains("#")));
    Warehouse warehouse = new Warehouse(isMapLine.get(true), 2);

    isMapLine.get(false).stream()
        .flatMapToInt(String::chars)
        .forEach(c -> warehouse.move((char) c));

    return String.valueOf(warehouse.boxesGpsSum());
  }
}
