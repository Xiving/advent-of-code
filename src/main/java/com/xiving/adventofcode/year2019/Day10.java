package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Day10 extends Year2019Day {

  public Day10() {
    super(10);
  }

  private static final double PI2 = 2 * Math.PI;

  private record Detected(Asteroid asteroid, double distance) {

  }

  private record Asteroid(double x, double y, Map<Double, Detected> detectedAngles) {

    void addDetectedAngles(Asteroid other) {
      double distance = Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
      double radian = (Math.atan2(other.x - this.x, this.y - other.y) + PI2) % PI2;

      this.detectedAngles.compute(radian, (k, v) -> v == null
          ? new Detected(other, distance)
          : v.distance < distance ? v : new Detected(other, distance));
      other.detectedAngles.compute((radian + Math.PI) % PI2, (k, v) -> v == null
          ? new Detected(this, distance)
          : v.distance < distance ? v : new Detected(this, distance));
    }

  }

  List<Asteroid> asteroidsFromInput(List<String> input) {
    List<Asteroid> asteroids = new ArrayList<>();

    for (int i = 0; i < input.size(); i++) {
      char[] line = input.get(i).toCharArray();

      for (int j = 0; j < line.length; j++) {
        if (line[j] == '#') {
          Asteroid newAsteroid = new Asteroid(j, i, new TreeMap<>());
          asteroids.forEach(newAsteroid::addDetectedAngles);
          asteroids.add(newAsteroid);
        }
      }
    }

    return asteroids;
  }

  @Override
  public String solvePartOne(List<String> input) {
    List<Asteroid> asteroids = asteroidsFromInput(input);
    int maxDetected = asteroids.stream()
        .map(Asteroid::detectedAngles)
        .mapToInt(Map::size)
        .max().getAsInt();
    return String.valueOf(maxDetected);
  }


  @Override
  public String solvePartTwo(List<String> input) {
    List<Asteroid> asteroids = asteroidsFromInput(input);
    Asteroid maxDetected = asteroids.stream().max(Comparator.comparing(e -> e.detectedAngles.size())).get();
    Detected detected = maxDetected.detectedAngles.values().toArray(Detected[]::new)[199];
    return String.valueOf((long) (detected.asteroid.x * 100 + detected.asteroid.y));
  }
}
