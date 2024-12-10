package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day10 extends Year2024Day {

  public Day10() {
    super(10);
  }

  private record Coord(int x, int y) {

  }

  private static Stream<Coord> streamNeighbours(Coord coord) {
    return Stream.of(
        new Coord(coord.x + 1, coord.y),
        new Coord(coord.x - 1, coord.y),
        new Coord(coord.x, coord.y + 1),
        new Coord(coord.x, coord.y - 1)
    );
  }

  private static class HeightMap {

    int[][] heightMap;
    List<Coord> trailHeads;

    HeightMap(List<String> input) {
      this.heightMap = new int[input.size()][input.getFirst().length()];
      this.trailHeads = new ArrayList<>();

      for (int y = 0; y < input.size(); y++) {
        char[] line = input.get(y).toCharArray();

        for (int x = 0; x < line.length; x++) {
          heightMap[y][x] = line[x] - '0';

          if (heightMap[y][x] == 0) {
            trailHeads.add(new Coord(x, y));
          }
        }
      }
    }

    boolean inBound(Coord coord) {
      return coord.x >= 0 && coord.y >= 0 && coord.y < this.heightMap.length && coord.x < this.heightMap[0].length;
    }

    int valueOf(Coord coord) {
      return inBound(coord) ? this.heightMap[coord.y][coord.x] : -1;
    }

    Stream<Coord> getNextPathTiles(Coord coord) {
      int nextValue = heightMap[coord.y][coord.x] + 1;
      return streamNeighbours(coord)
          .filter(e -> valueOf(e) == nextValue);
    }

    List<Coord> calcReachablePeaks(Coord trailhead) {
      int currentValue = 0;
      Stream<Coord> currentTiles = Stream.of(trailhead);

      while (currentValue < 9) {
        currentTiles = currentTiles.flatMap(this::getNextPathTiles);
        currentValue++;
      }

      return currentTiles.toList();
    }

    Set<Coord> calcDistinctReachablePeaks(Coord trailhead) {
      return new HashSet<>(calcReachablePeaks(trailhead));
    }

  }


  @Override
  public String solvePartOne(List<String> input) {
    HeightMap heightMap = new HeightMap(input);
    long totalScore = 0;

    for (Coord trailhead: heightMap.trailHeads) {
      totalScore += heightMap.calcDistinctReachablePeaks(trailhead).size();
    }

    return String.valueOf(totalScore);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    HeightMap heightMap = new HeightMap(input);
    long totalScore = 0;

    for (Coord trailhead: heightMap.trailHeads) {
      totalScore += heightMap.calcReachablePeaks(trailhead).size();
    }

    return String.valueOf(totalScore);
  }
}
