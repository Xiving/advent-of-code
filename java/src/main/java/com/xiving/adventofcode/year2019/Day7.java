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
    long maxOutput = 0;

    for (Permutations it = new Permutations(new long[]{0, 1, 2, 3, 4}); it.hasNext(); ) {
      long[] phases = it.next();

      long out1 = intcode.reset().run(phases[0], 0L).flushOutput().getFirst();
      long out2 = intcode.reset().run(phases[1], out1).flushOutput().getFirst();
      long out3 = intcode.reset().run(phases[2], out2).flushOutput().getFirst();
      long out4 = intcode.reset().run(phases[3], out3).flushOutput().getFirst();
      long out5 = intcode.reset().run(phases[4], out4).flushOutput().getFirst();

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

    long maxOutput = 0;

    for (Permutations it = new Permutations(new long[]{5, 6, 7, 8, 9}); it.hasNext(); ) {
      long[] phases = it.next();

      amp1Program.reset().run(phases[0]);
      ;
      amp2Program.reset().run(phases[1]);
      ;
      amp3Program.reset().run(phases[2]);
      ;
      amp4Program.reset().run(phases[3]);
      ;
      amp5Program.reset().run(phases[4]);
      ;

      long out5 = 0;
      while (!amp1Program.hasHalted()) {
        long out1 = amp1Program.run(out5).flushOutput().getFirst();
        long out2 = amp2Program.run(out1).flushOutput().getFirst();
        long out3 = amp3Program.run(out2).flushOutput().getFirst();
        long out4 = amp4Program.run(out3).flushOutput().getFirst();
        out5 = amp5Program.run(out4).flushOutput().getFirst();
      }

      maxOutput = Math.max(out5, maxOutput);
    }

    return String.valueOf(maxOutput);
  }

  private static void swap(long[] elements, int a, int b) {
    elements[a] = elements[a] ^ elements[b];
    elements[b] = elements[a] ^ elements[b];
    elements[a] = elements[a] ^ elements[b];
  }

  private static class Permutations implements Iterator<long[]> {

    private final int[] indexes;
    private final long[] elements;
    private int i = -1;

    Permutations(long[] elements) {
      this.elements = elements;
      this.indexes = new int[elements.length];
    }

    @Override
    public boolean hasNext() {
      return i < elements.length;
    }

    @Override
    public long[] next() {
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
