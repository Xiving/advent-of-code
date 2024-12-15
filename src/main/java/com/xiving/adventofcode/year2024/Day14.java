package com.xiving.adventofcode.year2024;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 extends Year2024Day {

  private static final Pattern ROBOT_INPUT = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");
  private static final long WIDTH = 101;
  private static final long HEIGHT = 103;

  public Day14() {
    super(14);
  }

  private record Robot(long x, long y, long xDir, long yDir) {

    static Robot fromStr(String str) {
      Matcher matcher = ROBOT_INPUT.matcher(str);
      matcher.matches();
      return new Robot(
          Long.parseLong(matcher.group(1)),
          Long.parseLong(matcher.group(2)),
          Long.parseLong(matcher.group(3)),
          Long.parseLong(matcher.group(4))
      );
    }

    Robot calcPosition(long seconds) {
      long xPos = (x + xDir * seconds) % WIDTH;
      long yPos = (y + yDir * seconds) % HEIGHT;
      return new Robot(xPos < 0 ? xPos + WIDTH : xPos, yPos < 0 ? yPos + HEIGHT : yPos, xDir, yDir);
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    int[] count = new int[]{0, 0, 0, 0};

    input.stream()
        .map(Robot::fromStr)
        .map(robot -> robot.calcPosition(100))
        .filter(robot -> robot.x != WIDTH / 2 && robot.y != HEIGHT / 2)
        .forEach(robot -> {
          int index = (robot.x < WIDTH / 2 ? 0 : 1) + (robot.y < HEIGHT / 2 ? 0 : 2);
          count[index] = count[index] + 1;
        });

    return String.valueOf(count[0] * count[1] * count[2] * count[3]);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    List<Robot> robots = input.stream()
        .map(Robot::fromStr)
        .collect(Collectors.toList());

    double lowestDeviation = Double.MAX_VALUE;
    int lowestDeviationSeconds = 0;

    for (int n = 1; n < 7000; n++) {
      long totalX = 0;
      long totalY = 0;

      for (int i = 0; i < robots.size(); i++) {
        Robot robot = robots.get(i).calcPosition(1);
        robots.set(i, robot);
        totalX += robot.x;
        totalY += robot.y;
      }

      final long avgX = totalX / robots.size();
      final long avgY = totalY / robots.size();

      double deviation = Math.sqrt(
          robots.stream()
              .mapToDouble(robot -> Math.pow(robot.x - avgX, 2) + Math.pow(robot.y - avgY, 2))
              .sum() / robots.size()
      );

      if (deviation < lowestDeviation) {
        lowestDeviation = deviation;
        lowestDeviationSeconds = n;
      }
    }

    return String.valueOf(lowestDeviationSeconds);
  }
}
