package com.xiving.adventofcode;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdventDayLoader {

  private static final String ROOT = "src/main/java/com/xiving/adventofcode/";
  private static final Pattern YEAR_PATTERN = Pattern.compile("year(\\d+)", Pattern.CASE_INSENSITIVE);
  private static final Pattern DAY_PATTERN = Pattern.compile("Day(\\d+).java", Pattern.CASE_INSENSITIVE);

  private static List<Integer> getYears() {
    return Stream.of(new File(ROOT).listFiles())
        .filter(File::isDirectory)
        .map(file -> YEAR_PATTERN.matcher(file.getAbsoluteFile().getName()))
        .filter(Matcher::find)
        .map(matcher -> Integer.valueOf(matcher.group(1)))
        .collect(Collectors.toList());
  }

  private static List<Integer> getDaysOfYear(int year) {
    return Stream.of(new File(ROOT + "year" + year).listFiles())
        .map(file -> DAY_PATTERN.matcher(file.getAbsoluteFile().getName()))
        .filter(Matcher::find)
        .map(matcher -> Integer.valueOf(matcher.group(1)))
        .collect(Collectors.toList());
  }

  private static Optional<AdventDay> loadDayClass(int year, int day) {
    try {
      return Optional.of(Class.forName(Main.class.getPackageName() + ".year" + year + ".Day" + day)
          .asSubclass(AdventDay.class)
          .getConstructor()
          .newInstance());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Optional.empty();
    }
  }

  public static List<AdventDay> loadAllAdventDays() {
    return getYears().stream()
        .flatMap(year ->
            getDaysOfYear(year).stream()
                .map(day -> loadDayClass(year, day))
                .filter(Optional::isPresent)
                .map(Optional::get))
        .collect(Collectors.toList());
  }

}
