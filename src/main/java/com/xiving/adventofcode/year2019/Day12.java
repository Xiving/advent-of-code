package com.xiving.adventofcode.year2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 extends Year2019Day {

  public Day12() {
    super(12);
  }

  private static final Pattern POSITION_INPUT = Pattern.compile("<x=(-?\\d+), y=(-?\\d+), z=(-?\\d+)>");

  private static class Moon {

    int xPos;
    int yPos;
    int zPos;

    int xDir;
    int yDir;
    int zDir;

    private Moon(int x, int y, int z) {
      this.xPos = x;
      this.yPos = y;
      this.zPos = z;
      this.xDir = 0;
      this.yDir = 0;
      this.zDir = 0;
    }

    private static Moon ofStr(String str) {
      Matcher match = POSITION_INPUT.matcher(str);

      if (match.matches()) {
        return new Moon(Integer.parseInt(match.group(1)), Integer.parseInt(match.group(2)), Integer.parseInt(match.group(3)));
      } else {
        throw new RuntimeException("Could not parse moon position string: " + str);
      }
    }

    private void applyGravity(Moon other) {
      int xDelta = Integer.compare(other.xPos, this.xPos);
      this.xDir += xDelta;
      other.xDir -= xDelta;

      int yDelta = Integer.compare(other.yPos, this.yPos);
      this.yDir += yDelta;
      other.yDir -= yDelta;

      int zDelta = Integer.compare(other.zPos, this.zPos);
      this.zDir += zDelta;
      other.zDir -= zDelta;
    }

    private void move() {
      xPos += xDir;
      yPos += yDir;
      zPos += zDir;
    }

    private int energy() {
      return (Math.abs(xPos) + Math.abs(yPos) + Math.abs(zPos)) * (Math.abs(xDir) + Math.abs(yDir) + Math.abs(zDir));
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    Moon[] moons = input.stream()
        .map(Moon::ofStr)
        .toArray(Moon[]::new);

    for (long n = 0; n < 1000; n++) {
      for (int i = 0; i < moons.length - 1; i++) {
        for (int j = i + 1; j < moons.length; j++) {
          moons[i].applyGravity(moons[j]);
        }
      }

      for (int i = 0; i < moons.length; i++) {
        moons[i].move();
      }
    }

    return String.valueOf(Arrays.stream(moons).mapToInt(Moon::energy).sum());
  }

  private static final int MINIMAL_PATTERN_LENGTH = 3;

  private static class PatternFinder {

    private List<Integer> pattern;
    private int repetitionStart;
    boolean found;

    private PatternFinder() {
      pattern = new ArrayList<>();
      pattern.add(0);
      repetitionStart = MINIMAL_PATTERN_LENGTH;
      found = false;
    }

    private void add(int x) {
      if (found) {
        return;
      }

      pattern.add(x);

      if (pattern.size() <= MINIMAL_PATTERN_LENGTH) {
        return;
      }

      if (pattern.getLast() == pattern.get(pattern.size() - repetitionStart - 1)) {
        if (pattern.size() == repetitionStart<<1) {
          found = true;
        }

        return;
      }

      loop:
      while (repetitionStart < pattern.size()) {
        for (int i = repetitionStart; i < pattern.size(); i++) {
          if (pattern.get(i - repetitionStart) != pattern.get(i)) {
            repetitionStart += 1;
            continue loop;
          }
        }

        break;
      }
    }
  }

  public static long lcm(long number1, long number2) {
    if (number1 == 0 || number2 == 0) {
      return 0;
    }

    long absNumber1 = Math.abs(number1);
    long absNumber2 = Math.abs(number2);
    long absHigherNumber = Math.max(absNumber1, absNumber2);
    long absLowerNumber = Math.min(absNumber1, absNumber2);
    long lcm = absHigherNumber;

    while (lcm % absLowerNumber != 0) {
      lcm += absHigherNumber;
    }

    return lcm;
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Moon[] moons = input.stream().map(Moon::ofStr).toArray(Moon[]::new);

    PatternFinder[] xPatterns = new PatternFinder[moons.length];
    PatternFinder[] yPatterns = new PatternFinder[moons.length];
    PatternFinder[] zPatterns = new PatternFinder[moons.length];

    for (int i = 0; i < xPatterns.length; i++) {
      xPatterns[i] = new PatternFinder();
      yPatterns[i] = new PatternFinder();
      zPatterns[i] = new PatternFinder();
    }

    loop:
    while (true) {
      for (int i = 0; i < moons.length - 1; i++) {
        for (int j = i + 1; j < moons.length; j++) {
          moons[i].applyGravity(moons[j]);
        }
      }

      for (int i = 0; i < moons.length; i++) {
        moons[i].move();
        xPatterns[i].add(moons[i].xDir);
        yPatterns[i].add(moons[i].yDir);
        zPatterns[i].add(moons[i].zDir);
      }

      for (int i = 0; i < moons.length; i++) {
        if (!xPatterns[i].found || !yPatterns[i].found || !zPatterns[i].found) {
          continue loop;
        }
      }

      int xPeriod = Arrays.stream(xPatterns).mapToInt(e -> e.pattern.size() / 2).max().getAsInt();
      int yPeriod = Arrays.stream(yPatterns).mapToInt(e -> e.pattern.size() / 2).max().getAsInt();
      int zPeriod = Arrays.stream(zPatterns).mapToInt(e -> e.pattern.size() / 2).max().getAsInt();

      return String.valueOf(lcm(xPeriod, lcm(yPeriod, zPeriod)));
    }
  }
}
