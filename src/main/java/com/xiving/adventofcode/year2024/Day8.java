package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day8 extends Year2024Day {

  public Day8() {
    super(8);
  }

  private record Antenna(int x, int y) {

    boolean inBounds(int xBound, int yBound) {
      return 0 <= this.y && this.y < yBound && 0 <= this.x && this.x < xBound;
    }
  }

  private static Stream<List<Antenna>> antennasGroupedByFrequency(List<String> lines) {
    Map<Character, List<Antenna>> antennasByFrequency = new HashMap<>();

    for (int y = 0; y < lines.size(); y++) {
      char[] chars = lines.get(y).toCharArray();

      for (int x = 0; x < chars.length; x++) {
        if (chars[x] != '.') {
          List<Antenna> antennas = antennasByFrequency.getOrDefault(chars[x], new ArrayList<>());
          antennas.add(new Antenna(x, y));
          antennasByFrequency.put(chars[x], antennas);
        }
      }
    }

    return antennasByFrequency.values().stream();
  }

  private static Stream<Antenna> getAntinodesPart1(Antenna antenna1, Antenna antenna2) {
    int xDiff = antenna1.x - antenna2.x;
    int yDiff = antenna1.y - antenna2.y;

    return Stream.of(
        new Antenna(antenna1.x + xDiff, antenna1.y + yDiff),
        new Antenna(antenna2.x - xDiff, antenna2.y - yDiff)
    );
  }

  private static Stream<Antenna> getAntinodesPart2(Antenna antenna1, Antenna antenna2, int xBound, int yBound) {
    int xDiff = antenna1.x - antenna2.x;
    int yDiff = antenna1.y - antenna2.y;
    List<Antenna> antinodes = new ArrayList<>();

    for (
        Antenna antenna = new Antenna(antenna1.x, antenna1.y);
        antenna.inBounds(xBound, yBound);
        antenna = new Antenna(antenna.x + xDiff, antenna.y + yDiff)
    ) {
      antinodes.add(antenna);
    }

    for (
        Antenna antenna = new Antenna(antenna2.x, antenna2.y);
        antenna.inBounds(xBound, yBound);
        antenna = new Antenna(antenna.x - xDiff, antenna.y - yDiff)
    ) {
      antinodes.add(antenna);
    }

    return antinodes.stream();
  }

  @Override
  public String solvePartOne(List<String> input) {
    int yBound = input.size();
    int xBound = input.get(0).length();

    Set<Antenna> uniqueAntennas = antennasGroupedByFrequency(input)
        .flatMap(antennas -> allPairStreamFunction(antennas, Day8::getAntinodesPart1))
        .filter(antenna -> antenna.inBounds(xBound, yBound))
        .collect(Collectors.toSet());

    return String.valueOf(uniqueAntennas.size());
  }

  @Override
  public String solvePartTwo(List<String> input) {
    int yBound = input.size();
    int xBound = input.get(0).length();

    Set<Antenna> uniqueAntennas = antennasGroupedByFrequency(input)
        .flatMap(coords -> allPairStreamFunction(coords, (antenna1, antenna2) -> getAntinodesPart2(antenna1, antenna2, xBound, yBound)))
        .collect(Collectors.toSet());

    return String.valueOf(uniqueAntennas.size());
  }

  private static <T, R> Stream<R> allPairStreamFunction(List<T> inputs, BiFunction<T, T, Stream<R>> function) {
    return IntStream.range(0, inputs.size() - 1)
        .mapToObj(i -> IntStream.range(i + 1, inputs.size())
            .mapToObj(j -> function.apply(inputs.get(i), inputs.get(j)))
            .flatMap(Function.identity())
        ).flatMap(Function.identity());
  }
}
