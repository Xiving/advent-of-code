package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Day3 extends Year2019Day {

  public Day3() {
    super(3);
  }

  private static boolean between(int v1, int value, int v2) {
    return (v1 < v2) ? v1 < value && value < v2 : v2 < value && value < v1;
  }

  record Intersection(int x, int y, int steps) {

  }

  record Vertical(int x, int yFrom, int yTo, int distBefore) {

  }

  record Horizontal(int y, int xFrom, int xTo, int distBefore) {

    Optional<Intersection> intersects(Vertical ver) {
      return (between(xFrom, ver.x, xTo) && between(ver.yFrom, y, ver.yTo))
          ? Optional.of(new Intersection(ver.x, y, distBefore + ver.distBefore + Math.abs(xFrom - ver.x) + Math.abs(ver.yFrom - y)))
          : Optional.empty();
    }
  }

  record Path(List<Horizontal> horizontals, List<Vertical> verticals) {

    static Path ofString(String input) {
      Path path = new Path(new ArrayList<>(), new ArrayList<>());
      int x = 0;
      int y = 0;
      int totalDist = 0;

      for (String stretch : input.split(",")) {
        int dist = Integer.parseInt(stretch.substring(1));

        switch (stretch.charAt(0)) {
          case 'R':
            path.horizontals.add(new Horizontal(y, x, x + dist, totalDist));
            x += dist;
            break;
          case 'L':
            path.horizontals.add(new Horizontal(y, x, x - dist, totalDist));
            x -= dist;
            break;
          case 'D':
            path.verticals.add(new Vertical(x, y, y + dist, totalDist));
            y += dist;
            break;
          case 'U':
            path.verticals.add(new Vertical(x, y, y - dist, totalDist));
            y -= dist;
            break;
        }

        totalDist += dist;
      }

      return path;
    }
  }

  private static Stream<Intersection> intersections(Path path1, Path path2) {
    return Stream.concat(
            path1.horizontals.stream().flatMap(hor -> path2.verticals.stream().map(hor::intersects)),
            path2.horizontals.stream().flatMap(hor -> path1.verticals.stream().map(hor::intersects)))
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Path wire1 = Path.ofString(input.getFirst());
    Path wire2 = Path.ofString(input.get(1));

    int nearestIntersection = intersections(wire1, wire2)
        .mapToInt(coord -> Math.abs(coord.x) + Math.abs(coord.y))
        .min()
        .getAsInt();

    return String.valueOf(nearestIntersection);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Path wire1 = Path.ofString(input.getFirst());
    Path wire2 = Path.ofString(input.get(1));

    int nearestIntersection = intersections(wire1, wire2)
        .mapToInt(Intersection::steps)
        .min()
        .getAsInt();

    return String.valueOf(nearestIntersection);
  }
}
