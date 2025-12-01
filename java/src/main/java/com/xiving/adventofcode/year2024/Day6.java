package com.xiving.adventofcode.year2024;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day6 extends Year2024Day {

  private static final int[] DIRECTIONS = new int[]{-1, 0, 1, 0};
  private static final char[] DIR_MARKER = new char[]{'^', '>', 'v', '<'};
  private static final char[] BI_DIR_MARKER = new char[]{'|', '-'};

  public Day6() {
    super(6);
  }

  private static class PatrolRoute implements Cloneable {

    private char[][] level;
    private int x;
    private int y;

    private int dir;
    private int xDir;
    private int yDir;

    PatrolRoute(List<String> input) {
      level = input.stream().map(String::toCharArray).toArray(char[][]::new);
      int[] startPosition = levelStartPosition(level);
      y = startPosition[0];
      x = startPosition[1];

      dir = 0;
      yDir = DIRECTIONS[dir];
      xDir = DIRECTIONS[(dir + 1) % DIRECTIONS.length];
    }

    public boolean hasNext() {
      return y + yDir >= 0 && y + yDir < level.length && x + xDir >= 0 && x + xDir < level[0].length;
    }

    public void moveNext() {
      if (level[y + yDir][x + xDir] == '#') {
        rotate();
      } else {
        y = y + yDir;
        x = x + xDir;
      }
    }

    public long countChar(char c) {
      return Arrays.stream(level)
          .mapToLong(chars -> IntStream.range(0, level[0].length).filter(x -> chars[x] == c).count()).sum();
    }

    public boolean loopsWithObjectInFront(){
      if (!hasNext() || level[y + yDir][x + xDir] == '#' || level[y + yDir][x + xDir] == 'O') {
        return false;
      }

      level[y + yDir][x + xDir] = '#' ;
      rotate();
      setCurrent(DIR_MARKER[dir]);

      while (hasNext()) {
        moveNext();

        if (level[y][x] == DIR_MARKER[dir] || level[y][x] == BI_DIR_MARKER[dir % 2]) {
          return true;
        } else if (level[y][x] == DIR_MARKER[(dir + 2) % DIR_MARKER.length]) {
          setCurrent(BI_DIR_MARKER[dir % 2]);
        } else {
          setCurrent(DIR_MARKER[dir]);
        }
      }

      return false;
    }
    
    public void setCurrent(char c) {
      level[y][x] = c;
    }

    private void rotate() {
      dir = (dir + 1) % DIRECTIONS.length;
      yDir = DIRECTIONS[dir];
      xDir = DIRECTIONS[(dir + 1) % DIRECTIONS.length];
    }

    private static int[] levelStartPosition(char[][] level) {
      for (int y = 0; y < level.length; y++) {
        for (int x = 0; x < level[0].length; x++) {
          if (level[y][x] == '^') {
            return new int[]{y, x};
          }
        }
      }

      return new int[]{0, 0};
    }

    @Override
    public PatrolRoute clone() {
      try {
        PatrolRoute clone = (PatrolRoute) super.clone();
        clone.level = Arrays.stream(this.level).map(char[]::clone).toArray(char[][]::new);
        clone.x = this.x;
        clone.y = this.y;
        clone.dir = this.dir;
        clone.xDir = this.xDir;
        clone.yDir = this.yDir;
        return clone;
      } catch (CloneNotSupportedException e) {
        throw new AssertionError();
      }
    }
  }

  @Override
  public String solvePartOne(List<String> input) {
    PatrolRoute patrolRoute = new PatrolRoute(input);
    patrolRoute.setCurrent('X');

    while(patrolRoute.hasNext()) {
      patrolRoute.moveNext();
      patrolRoute.setCurrent('X');
    }

    return String.valueOf(patrolRoute.countChar('X'));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    PatrolRoute patrolRoute = new PatrolRoute(input);
    patrolRoute.setCurrent('O');
    int loopPossibilities = 0;

    while(patrolRoute.hasNext()) {
      PatrolRoute copy = patrolRoute.clone();

      // destructive method
      if (patrolRoute.loopsWithObjectInFront()) {
        loopPossibilities++;
      }

      patrolRoute = copy;
      patrolRoute.moveNext();
      patrolRoute.setCurrent('O');
    }

    return String.valueOf(loopPossibilities);
  }
}