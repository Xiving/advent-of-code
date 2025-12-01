package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day6 extends Year2019Day {

  private static final String CENTRE_OF_MASS = "COM";

  public Day6() {
    super(6);
  }

  private record OrbitCount(int direct, int indirect) {

  }

  private static class Moon {

    Moon orbits;
    List<Moon> moons;

    Moon(Moon orbits, List<Moon> moons) {
      this.orbits = orbits;
      this.moons = moons;
    }

    Moon add(Moon moon) {
      this.moons.add(moon);
      return this;
    }

    OrbitCount countOrbits() {
      int direct = 0;
      int indirect = 0;

      for (Moon moon : this.moons) {
        OrbitCount count = moon.countOrbits();
        direct += count.direct + 1;
        indirect += count.indirect + count.direct;
      }

      return new OrbitCount(direct, indirect);
    }

    List<Moon> reversePathToCentreOfMass() {
      List<Moon> path = new ArrayList<>();
      Moon current = this;

      while (current != null) {
        path.add(current);
        current = current.orbits;
      }

      return path;
    }
  }

  private record MoonRegistry(Map<String, Moon> moonRegistry) {

    void addOrbit(String centreName, String orbiterName) {
      Moon moon = moonRegistry.compute(orbiterName, (k, v) -> v == null ? new Moon(null, new ArrayList<>()) : v);
      moon.orbits = moonRegistry.compute(centreName,
          (k, v) -> v == null ? new Moon(null, new ArrayList<>(Arrays.asList(moon))) : v.add(moon));
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    MoonRegistry moonRegistry = new MoonRegistry(new HashMap<>());
    input.forEach(str -> moonRegistry.addOrbit(str.substring(0, 3), str.substring(4, 7)));
    OrbitCount count = moonRegistry.moonRegistry.get(CENTRE_OF_MASS).countOrbits();
    return String.valueOf(count.direct + count.indirect);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    MoonRegistry moonRegistry = new MoonRegistry(new HashMap<>());
    input.forEach(str -> moonRegistry.addOrbit(str.substring(0, 3), str.substring(4, 7)));

    List<Moon> pathYou = moonRegistry.moonRegistry.get("YOU").reversePathToCentreOfMass();
    List<Moon> pathSan = moonRegistry.moonRegistry.get("SAN").reversePathToCentreOfMass();

    while (pathSan.removeLast() == pathYou.removeLast()) {

    }

    return String.valueOf(pathYou.size() + pathSan.size());
  }
}
