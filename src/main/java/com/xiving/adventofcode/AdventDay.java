package com.xiving.adventofcode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class AdventDay {

  public int year;
  public int day;

  public AdventDay(int year, int day) {
    this.year = year;
    this.day = day;
  }

  public List<String> getDayInput() {
    Path inputPath = Path.of(String.format("src/main/resources/year%d/day%d.txt", year, day));
    try {
      return Files.readAllLines(inputPath, Charset.defaultCharset());
    } catch (IOException e) {
      return new ArrayList<>();
    }
  }

  abstract public String solvePartOne(List<String> input);
  
  abstract public String solvePartTwo(List<String> input);

  public String solvePartOneForInput() {
    return solvePartOne(getDayInput());
  }
  
  public String solvePartTwoForInput() {
    return solvePartTwo(getDayInput());
  }

}
