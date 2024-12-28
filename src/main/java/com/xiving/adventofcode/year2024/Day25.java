package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.List;
import org.graalvm.collections.Pair;

public class Day25 extends Year2024Day {

  public Day25() {
    super(25);
  }

  private record Key(int[] heights) {

  }

  private record Lock(int[] heights) {
    private boolean fits(Key key) {
      for (int i = 0; i < 5; i++) {
        if (heights[i] + key.heights[i] > 7) {
          return false;
        }
      }

      return true;
    }
  }

  private static Pair<List<Key>, List<Lock>> fromInput(List<String> input) {
    int i = 0;
    List<Key> keys = new ArrayList<>();
    List<Lock> locks = new ArrayList<>();

    while (i < input.size()) {
      boolean isLock = input.get(i).charAt(0) == '#';
      int[] heights = new int[5];

      for (int j = 0; j < 7; j++, i++) {
        char[] chars = input.get(i).toCharArray();

        for (int k = 0; k < 5; k++) {
          if (chars[k] == '#') {
            heights[k]++;
          }
        }
      }

      if (isLock) {
        locks.add(new Lock(heights));
      } else {
        keys.add(new Key(heights));
      }

      i++;
    }

    return Pair.create(keys, locks);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Pair<List<Key>, List<Lock>> keysAndLocks = fromInput(input);
    int fitCount = 0;

    for(Lock lock : keysAndLocks.getRight()) {
      for(Key key : keysAndLocks.getLeft()) {
        if (lock.fits(key)) {
          fitCount++;
        }
      }
    }

    return String.valueOf(fitCount);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    return "Happy Chrismund!";
  }
}
