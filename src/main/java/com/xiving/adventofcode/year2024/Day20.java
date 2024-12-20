package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day20 extends Year2024Day {

  public Day20() {
    super(20);
  }

  private static class RaceTrack {

    int[][] map;
    int startX;
    int startY;

    RaceTrack(List<String> input) {
      this.map = new int[input.size()][input.getFirst().length()];
      this.startX = 0;
      this.startY = 0;

      for (int i = 0; i < input.size(); i++) {
        char[] line = input.get(i).toCharArray();

        for (int j = 0; j < line.length; j++) {
          map[i][j] = line[j] == '#' ? -1 : 0;

          if (line[j] == 'S') {
            startX = j;
            startY = i;
            map[i][j] = 1;
          }
        }
      }
    }

    private void computeTrackLength() {
      int currentX = startX;
      int currentY = startY;
      int length = 1;

      while (true) {
        if (map[currentY][currentX + 1] == 0) {
          currentX += 1;
        } else if (map[currentY][currentX - 1] == 0) {
          currentX -= 1;
        } else if (map[currentY + 1][currentX] == 0) {
          currentY += 1;
        } else if (map[currentY - 1][currentX] == 0) {
          currentY -= 1;
        } else {
          return;
        }

        map[currentY][currentX] = ++length;
      }
    }

    private List<Integer> computeCheats(int noCollisionLength, int timeSavedLowerBound) {
      List<Integer> cheats = new ArrayList<>();

      for (int y1 = 1; y1 < map.length - 1; y1++) {
        for (int x1 = 1; x1 < map[0].length - 1; x1++) {
          if (map[y1][x1] < 0) {
            continue;
          }

          for (int y2 = y1; y2 <= Math.min(y1 + noCollisionLength, map.length - 2); y2++) {
            int xWidth = noCollisionLength - (y2 - y1);

            for (int x2 = y2 == y1 ? x1 + 1 : Math.max(x1 - xWidth, 1); x2 <= Math.min(x1 + xWidth, map[0].length - 2); x2++) {
              if (map[y2][x2] < 0) {
                continue;
              }

              int timeSaved = Math.abs(map[y1][x1] - map[y2][x2]) - (Math.abs(y1 - y2) + Math.abs(x1 - x2));

              if (timeSaved >= timeSavedLowerBound) {
                cheats.add(timeSaved);
              }
            }
          }
        }
      }

      return cheats;
    }
  }


  @Override
  public String solvePartOne(List<String> input) {
    RaceTrack raceTrack = new RaceTrack(input);
    raceTrack.computeTrackLength();
    long cheatCount = raceTrack.computeCheats(2, 100).size();
    return String.valueOf(cheatCount);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    RaceTrack raceTrack = new RaceTrack(input);
    raceTrack.computeTrackLength();
    long cheatCount = raceTrack.computeCheats(20, 100).size();
    return String.valueOf(cheatCount);
  }
}
