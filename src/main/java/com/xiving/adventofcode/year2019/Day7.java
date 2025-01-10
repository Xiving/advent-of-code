package com.xiving.adventofcode.year2019;

import java.util.Iterator;
import java.util.List;

public class Day7 extends Year2019Day {

  public Day7() {
    super(7);
  }

  @Override
  public String solvePartOne(List<String> input) {
    Intcode intcode = Intcode.ofInput(input.getFirst());
    int maxOutput = 0;

    for (Permutations it = new Permutations(new int[]{0, 1, 2, 3, 4}); it.hasNext(); ) {
      int[] phases = it.next();

      int out1 = intcode.reset().input(phases[0], 0).flushOutput().getFirst();
      int out2 = intcode.reset().input(phases[1], out1).flushOutput().getFirst();
      int out3 = intcode.reset().input(phases[2], out2).flushOutput().getFirst();
      int out4 = intcode.reset().input(phases[3], out3).flushOutput().getFirst();
      int out5 = intcode.reset().input(phases[4], out4).flushOutput().getFirst();

      maxOutput = Math.max(out5, maxOutput);
    }

    return String.valueOf(maxOutput);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Intcode amp1Program = Intcode.ofInput(input.getFirst());
    Intcode amp2Program = amp1Program.copy();
    Intcode amp3Program = amp1Program.copy();
    Intcode amp4Program = amp1Program.copy();
    Intcode amp5Program = amp1Program.copy();

    int maxOutput = 0;

    for (Permutations it = new Permutations(new int[]{5, 6, 7, 8, 9}); it.hasNext(); ) {
      int[] phases = it.next();

      amp1Program.reset().input(phases[0]);;
      amp2Program.reset().input(phases[1]);;
      amp3Program.reset().input(phases[2]);;
      amp4Program.reset().input(phases[3]);;
      amp5Program.reset().input(phases[4]);;

      int out5 = 0;
      while (!amp1Program.hasHalted()) {
        int out1 = amp1Program.input(out5).flushOutput().getFirst();
        int out2 = amp2Program.input(out1).flushOutput().getFirst();
        int out3 = amp3Program.input(out2).flushOutput().getFirst();
        int out4 = amp4Program.input(out3).flushOutput().getFirst();
        out5 = amp5Program.input(out4).flushOutput().getFirst();
      }

      maxOutput = Math.max(out5, maxOutput);
    }

    return String.valueOf(maxOutput);
  }

  private static void swap(int[] elements, int a, int b) {
    elements[a] = elements[a] ^ elements[b];
    elements[b] = elements[a] ^ elements[b];
    elements[a] = elements[a] ^ elements[b];
  }

  private static class Permutations implements Iterator<int[]> {

    private int[] indexes;
    private int[] elements;
    private int i = -1;

    Permutations(int[] elements) {
      this.elements = elements;
      this.indexes = new int[elements.length];
    }

    @Override
    public boolean hasNext() {
      return i < elements.length;
    }

    @Override
    public int[] next() {
      if (i < 0) {
        i = 0;
        return elements;
      }

      while (i < elements.length) {
        if (indexes[i] < i) {
          swap(elements, i % 2 == 0 ? 0 : indexes[i], i);
          indexes[i]++;
          i = 0;
          return elements;
        } else {
          indexes[i] = 0;
          i++;
        }
      }

      return elements;
    }
  }
}
