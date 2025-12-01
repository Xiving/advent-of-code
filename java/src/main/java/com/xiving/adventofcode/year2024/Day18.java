package com.xiving.adventofcode.year2024;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class Day18 extends Year2024Day {

  private static final int WIDTH = 71;
  private static final int HEIGHT = 71;

  public Day18() {
    super(18);
  }

  private static class Path {

    int x;
    int y;
    int length;

    Path(int x, int y, int length) {
      this.x = x;
      this.y = y;
      this.length = length;
    }

    Stream<Path> neighbours() {
      Builder<Path> builder = Stream.builder();

      if (x + 1 < WIDTH) {
        builder.add(new Path(x + 1, y, length + 1));
      }

      if (x - 1 >= 0) {
        builder.add(new Path(x - 1, y, length + 1));
      }

      if (y + 1 < HEIGHT) {
        builder.add(new Path(x, y + 1, length + 1));
      }

      if (y - 1 >= 0) {
        builder.add(new Path(x, y - 1, length + 1));
      }

      return builder.build();
    }

    int tentativeScore() {
      return this.length + Math.abs(this.x - (WIDTH - 1)) + Math.abs(this.y - (HEIGHT - 1));
    }
  }

  private static class WallMap {

    int[] wallCoords;
    boolean[][] walls;
    int wallAmount;
    int[][] score;

    WallMap(List<String> input) {
      this.wallCoords = new int[input.size() * 2];
      this.walls = new boolean[HEIGHT][WIDTH];
      this.wallAmount = 0;
      this.score = new int[HEIGHT][WIDTH];

      for (int i = 0; i < input.size(); i++) {
        String[] coord = input.get(i).split(",");
        int x = Integer.parseInt(coord[0]);
        int y = Integer.parseInt(coord[1]);
        wallCoords[(i << 1)] = x;
        wallCoords[(i << 1) + 1] = y;
      }
    }

    private void changeWallAmount(int wallAmount) {
      if (wallAmount == this.wallAmount) {
        return;
      }

      if (wallAmount > this.wallAmount) {
        for (int i = this.wallAmount; i < wallAmount; i++) {
          int x = wallCoords[(i * 2)];
          int y = wallCoords[(i * 2) + 1];
          walls[y][x] = !walls[y][x];
        }
      } else {
        for (int i = this.wallAmount - 1; i >= wallAmount; i--) {
          int x = wallCoords[(i * 2)];
          int y = wallCoords[(i * 2) + 1];
          walls[y][x] = !walls[y][x];
        }
      }

      this.wallAmount = wallAmount;
    }

    private void resetScore() {
      for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
          score[i][j] = Integer.MAX_VALUE;
        }
      }
    }

    private int pathLengthBetweenCorners() {
      resetScore();
      int goalY = HEIGHT - 1;
      int goalX = WIDTH - 1;

      SortedSet<Path> orderedByScore = new TreeSet<>((e1, e2) -> {
        int compare = Integer.compare(e1.tentativeScore(), e2.tentativeScore());
        return compare == 0 ? 1 : compare;
      });

      orderedByScore.add(new Path(0, 0, 0));

      while (!orderedByScore.isEmpty()) {
        Path current = orderedByScore.removeFirst();

        if (current.y == goalY && current.x == goalX) {
          return current.length;
        }

        current.neighbours()
            .filter(neighbour -> !walls[neighbour.y][neighbour.x])
            .forEach(neighbour -> {
              int tentativeScore = neighbour.tentativeScore();

              if (tentativeScore < score[neighbour.y][neighbour.x]) {
                score[neighbour.y][neighbour.x] = tentativeScore;
                orderedByScore.add(neighbour);
              }
            });
      }

      return -1;
    }

    private int binarySearchHighestValidWallAmount() {
      int minWallAmount = 0;
      int maxWallAmount = this.wallCoords.length/2;
      int currentWallAmount = (maxWallAmount + minWallAmount)>>1;

      while (minWallAmount != maxWallAmount - 1) {
        this.changeWallAmount(currentWallAmount);
        int pathLength = pathLengthBetweenCorners();

        if (pathLength == -1) {
          maxWallAmount = currentWallAmount;
          currentWallAmount = (maxWallAmount + minWallAmount)>>1;
        } else {
          minWallAmount = currentWallAmount;
          currentWallAmount = (maxWallAmount + minWallAmount)>>1;
        }
      }

      this.changeWallAmount(minWallAmount);
      return minWallAmount;
    }
  }


  @Override
  public String solvePartOne(List<String> input) {
    WallMap wallMap = new WallMap(input);
    wallMap.changeWallAmount(1024);
    int pathLength = wallMap.pathLengthBetweenCorners();
    return String.valueOf(pathLength);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    WallMap wallMap = new WallMap(input);
    int wallAmount = wallMap.binarySearchHighestValidWallAmount();
    int wallX = wallMap.wallCoords[(wallAmount * 2)];
    int wallY = wallMap.wallCoords[(wallAmount * 2) + 1];
    return String.format("%d,%d", wallX, wallY);
  }
}
