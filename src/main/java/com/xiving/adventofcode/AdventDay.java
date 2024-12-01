package com.xiving.adventofcode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AdventDay {

  public int year;
  public int day;

  public AdventDay(int year, int day) {
    this.year = year;
    this.day = day;
  }

  public String getDayInput() {
    Path inputPath = Path.of(String.format("src/main/resources/year%d/day%d.txt", year, day));
    try {
      return Files.readString(inputPath, Charset.defaultCharset());
    } catch (IOException e) {
      return "";
    }
  }

  abstract public String solvePartOne(String input);
  
  abstract public String solvePartTwo(String input);

  public String solvePartOneForInput() {
    return solvePartOne(getDayInput());
  }
  
  public String solvePartTwoForInput() {
    return solvePartTwo(getDayInput());
  }

}
