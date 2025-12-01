package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

public class Day8 extends Year2024Day {

  public Day8() {
    super(8);
  }

  private record Coord(int x, int y) {

    boolean inBounds(int xBound, int yBound) {
      return 0 <= this.y && this.y < yBound && 0 <= this.x && this.x < xBound;
    }
  }

  private static Stream<List<Coord>> coordsGroupedByFrequency(List<String> lines) {
    Map<Character, List<Coord>> coordsByFrequency = new HashMap<>();

    for (int y = 0; y < lines.size(); y++) {
      char[] chars = lines.get(y).toCharArray();

      for (int x = 0; x < chars.length; x++) {
        if (chars[x] != '.') {
          List<Coord> coords = coordsByFrequency.getOrDefault(chars[x], new ArrayList<>());
          coords.add(new Coord(x, y));
          coordsByFrequency.put(chars[x], coords);
        }
      }
    }

    return coordsByFrequency.values().stream();
  }

  private static Stream<Coord> getAntinodesPart1(Coord coord1, Coord coord2) {
    int xDiff = coord1.x - coord2.x;
    int yDiff = coord1.y - coord2.y;

    return Stream.of(
        new Coord(coord1.x + xDiff, coord1.y + yDiff),
        new Coord(coord2.x - xDiff, coord2.y - yDiff)
    );
  }

  private static Stream<Coord> getAntinodesPart2(Coord coord1, Coord coord2, int xBound, int yBound) {
    int xDiff = coord1.x - coord2.x;
    int yDiff = coord1.y - coord2.y;
    Builder<Coord> antinodes = Stream.builder();

    Coord coord = new Coord(coord1.x, coord1.y);

    while (coord.inBounds(xBound, yBound)) {
      antinodes.add(coord);
      coord = new Coord(coord.x + xDiff, coord.y + yDiff);
    }

    coord = new Coord(coord2.x, coord2.y);

    while (coord.inBounds(xBound, yBound)) {
      antinodes.add(coord);
      coord = new Coord(coord.x - xDiff, coord.y - yDiff);
    }

    return antinodes.build();
  }

  private static Stream<Coord> applyToAllCoordPairs(List<Coord> coords, BiFunction<Coord, Coord, Stream<Coord>> fun) {
    Builder<Coord> coordBuilder = Stream.builder();

    for (int i = 0; i < coords.size() - 1; i++) {
      for (int j = i + 1; j < coords.size(); j++) {
        fun.apply(coords.get(i), coords.get(j)).forEach(coordBuilder::add);
      }
    }

    return coordBuilder.build();
  }

  @Override
  public String solvePartOne(List<String> input) {
    int yBound = input.size();
    int xBound = input.get(0).length();

    Set<Coord> uniqueCoords = coordsGroupedByFrequency(input)
        .flatMap(coords -> applyToAllCoordPairs(coords, Day8::getAntinodesPart1))
        .filter(coords -> coords.inBounds(xBound, yBound))
        .collect(Collectors.toSet());

    return String.valueOf(uniqueCoords.size());
  }

  @Override
  public String solvePartTwo(List<String> input) {
    int yBound = input.size();
    int xBound = input.get(0).length();

    BiFunction<Coord, Coord, Stream<Coord>> antinodeFunction =
        (coord1, coord2) -> getAntinodesPart2(coord1, coord2, xBound, yBound);

    Set<Coord> uniqueCoords = coordsGroupedByFrequency(input)
        .flatMap(coords -> applyToAllCoordPairs(coords, antinodeFunction))
        .collect(Collectors.toSet());

    return String.valueOf(uniqueCoords.size());
  }
}
