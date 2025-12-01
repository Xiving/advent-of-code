package com.xiving.adventofcode.year2024;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 extends Year2024Day {

  public Day21() {
    super(21);
  }

  private record Button(int y, int x) {

  }

  private static final Map<Character, Button> NUM_PAD_CONFIG = Map.ofEntries(
      entry('7', new Button(0, 0)), entry('8', new Button(0, 1)), entry('9', new Button(0, 2)),
      entry('4', new Button(1, 0)), entry('5', new Button(1, 1)), entry('6', new Button(1, 2)),
      entry('1', new Button(2, 0)), entry('2', new Button(2, 1)), entry('3', new Button(2, 2)),
      entry('x', new Button(3, 0)), entry('0', new Button(3, 1)), entry('A', new Button(3, 2))
  );

  private static final Map<Character, Button> DIR_PAD_CONFIG = Map.ofEntries(
      entry('x', new Button(0, 0)), entry('^', new Button(0, 1)), entry('A', new Button(0, 2)),
      entry('<', new Button(1, 0)), entry('v', new Button(1, 1)), entry('>', new Button(1, 2))
  );

  private static String plot(int fromX, int fromY, int toX, int toY, int bannedX, int bannedY) {
    int dirX = toX - fromX;
    int dirY = toY - fromY;
    String updo = String.valueOf(dirY > 0 ? 'v' : '^').repeat(Math.abs(dirY));
    String leri = String.valueOf(dirX > 0 ? '>' : '<').repeat(Math.abs(dirX));

    if (fromX == toX || fromY == toY) {
      return fromX == toX ? updo : leri;
    }

    if (fromY == bannedY && toX == bannedX) {
      return updo + leri;
    } else if (fromX == bannedX && toY == bannedY) {
      return leri + updo;
    }

    return dirX < 0 ? leri + updo : updo + leri;
  }

  private static class ButtonPad {

    Map<Character, Button> config;
    Button current;
    Button banned;
    ButtonPad controllingPad;
    Map<String, Long> cache;

    ButtonPad(Map<Character, Button> padConfig, ButtonPad controllingPad) {
      this.config = padConfig;
      this.current = config.get('A');
      this.banned = config.get('x');
      this.controllingPad = controllingPad;
      this.cache = new HashMap<>();
    }

    long pressesForSequence(String sequence) {
      Long cachedValue = cache.get(sequence);

      if (cachedValue != null) {
        return cachedValue;
      }

      long sequencePressCount = sequence.chars()
          .mapToObj(c -> (char) c)
          .mapToLong(this::pressesForButton)
          .sum();

      cache.put(sequence, sequencePressCount);
      return sequencePressCount;
    }

    long pressesForButton(char key) {
      Button button = config.get(key);
      String route = plot(current.x, current.y, button.x, button.y, banned.x, banned.y) + 'A';
      current = button;

      return controllingPad == null
          ? route.length()
          : controllingPad.pressesForSequence(route);
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    ButtonPad dirPad = new ButtonPad(DIR_PAD_CONFIG, null);

    for (int i = 0; i < 1; i++) {
      dirPad = new ButtonPad(DIR_PAD_CONFIG, dirPad);
    }

    ButtonPad numPad = new ButtonPad(NUM_PAD_CONFIG, dirPad);

    long buttonPresses = input.stream().mapToLong(code -> numPad.pressesForSequence(code) * Long.parseLong(code.substring(0, 3))).sum();
    return String.valueOf(buttonPresses);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    ButtonPad dirPad = new ButtonPad(DIR_PAD_CONFIG, null);

    for (int i = 0; i < 24; i++) {
      dirPad = new ButtonPad(DIR_PAD_CONFIG, dirPad);
    }

    ButtonPad numPad = new ButtonPad(NUM_PAD_CONFIG, dirPad);

    long buttonPresses = input.stream().mapToLong(code -> numPad.pressesForSequence(code) * Long.parseLong(code.substring(0, 3))).sum();
    return String.valueOf(buttonPresses);
  }
}
