package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class Day16 extends Year2024Day {

  public Day16() {
    super(16);
  }

  private record Coord(int x, int y) {

  }

  private record Maze(char[][] map, int startX, int startY, int goalX, int goalY) {
    static Maze fromInput(List<String> input) {
      char[][] map = new char[input.size()][];
      int startX = 0;
      int startY = 0;
      int goalX = 0;
      int goalY = 0;

      for (int i = 0; i < input.size(); i++) {
        map[i] = input.get(i).toCharArray();

        for (int j = 0; j < map[i].length; j++) {
          if (map[i][j] == 'E') {
            goalX = j;
            goalY = i;
          } else if (map[i][j] == 'S') {
            startX = j;
            startY = i;
          }
        }
      }

      return new Maze(map, startX, startY, goalX, goalY);
    }
  }

  private record PathHead(int x, int y, int xDir, int yDir, int score, PathHead prev) {
    List<PathHead> next() {
      return (this.xDir != 0) ?
          List.of(
              new PathHead(this.x + this.xDir, this.y, this.xDir, this.yDir, this.score + 1, this),
              new PathHead(this.x, this.y + 1, 0, 1, this.score + 1001, this),
              new PathHead(this.x, this.y - 1, 0, -1, this.score + 1001, this)
          ) :
          List.of(
              new PathHead(this.x, this.y + this.yDir, this.xDir, this.yDir, this.score + 1, this),
              new PathHead(this.x + 1, this.y, 1, 0, this.score + 1001, this),
              new PathHead(this.x - 1, this.y, -1, 0, this.score + 1001, this)
          );
    }

    int projectScoreToGoal(int x, int y) {
      int xDiff = x - this.x;
      int yDiff = y - this.y;
      int score = Math.abs(xDiff) + Math.abs(yDiff);

      if (Integer.signum(xDiff) != this.xDir) {
        score += 1000;
      }

      if (Integer.signum(yDiff) != this.yDir) {
        score += 1000;
      }

      return this.score + score;
    }

    Stream<Coord> streamPathCoords() {
      Builder<Coord> builder = Stream.builder();
      PathHead current = this;

      while (!Objects.isNull(current.prev)) {
        builder.add(new Coord(current.x, current.y));
        current = current.prev;
      }

      builder.add(new Coord(current.x, current.y));
      return builder.build();
    }

  }

  private static List<PathHead> aStar(Maze maze) {
    List<PathHead> result = new ArrayList<>();

    SortedSet<PathHead> edges = new TreeSet<>((e1, e2) -> {
      int v = Integer.compare(e1.projectScoreToGoal(maze.goalX, maze.goalY), e2.projectScoreToGoal(maze.goalX, maze.goalY));
      return (v == 0) ? 1 : v;
    });

    edges.add(new PathHead(maze.startX, maze.startY, 1, 0, 0, null));

    int[][] fScore = new int[maze.map.length][maze.map[0].length];

    for (int[] row : fScore) {
      Arrays.fill(row, Integer.MAX_VALUE);
    }

    while (!edges.isEmpty()) {
      PathHead head = edges.removeFirst();

      if (head.x == maze.goalX && head.y == maze.goalY) {
        if (!result.isEmpty() && result.getFirst().score != head.score) {
          return result;
        }

        result.add(head);
        continue;
      }

      for (PathHead next : head.next()) {
        if (maze.map[next.y][next.x] != '#' && (fScore[next.y][next.x] >= next.score || fScore[next.y][next.x] == next.score - 1000)) {
          fScore[next.y][next.x] = next.score;
          edges.add(next);
        }
      }
    }

    return result;
  }

  @Override
  public String solvePartOne(List<String> input) {
    Maze maze = Maze.fromInput(input);
    List<PathHead> result = aStar(maze);
    return String.valueOf(result.getFirst().score);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Maze maze = Maze.fromInput(input);
    Set<Coord> uniquePathTiles = aStar(maze).stream()
        .flatMap(PathHead::streamPathCoords)
        .collect(Collectors.toSet());

    return String.valueOf(uniquePathTiles.size());
  }
}
