package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;

public class Day22 extends Year2024Day {

  public Day22() {
    super(22);
  }

  private static final int SEQUENCE_COUNT = 19 * 19 * 19 * 19;

  private static long nextSecret(long secret) {
    secret = (secret ^ (secret << 6)) & 0xFFFFFF; // * 64
    secret = (secret ^ (secret >> 5)) & 0xFFFFFF; // * 32
    secret = (secret ^ (secret << 11)) & 0xFFFFFF; // * 2024
    return secret;
  }

  private static long nextSecret(long secret, int n) {
    for (int i = 0; i < n; i++) {
      secret = nextSecret(secret);
    }

    return secret;
  }

  @Override
  public String solvePartOne(List<String> input) {
    long result = input.stream()
        .mapToLong(Long::parseLong)
        .map(v -> nextSecret(v, 2000))
        .sum();

    return String.valueOf(result);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    int[] sequenceBananas = new int[SEQUENCE_COUNT];
    int[] sequenceTraversed = new int[SEQUENCE_COUNT];

    for (int i = 0; i < input.size(); i++) {
      long secret = Long.parseLong(input.get(i));
      int sequence = 0;
      int oldPrice = (int) (secret % 10);

      for (int j = 0; j < 2000; j++) {
        secret = nextSecret(secret);
        int price = (int) (secret % 10);
        sequence = ((sequence * 19) + (price + 9 - oldPrice)) % SEQUENCE_COUNT;
        oldPrice = price;

        if (j >= 3) {
          if (sequenceTraversed[sequence] < i) {
            sequenceTraversed[sequence] = i;
            sequenceBananas[sequence] += price;
          }
        }
      }
    }

    return String.valueOf(Arrays.stream(sequenceBananas).max().getAsInt());
  }
}
