package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.List;

public class Day12 extends Year2024Day {

  public Day12() {
    super(12);
  }

  private static abstract class GardenPlot {

    int crop;
    List<GardenPlot> regionPlots;

    GardenPlot(int crop) {
      this.crop = crop;
      this.regionPlots = new ArrayList<>();
      this.regionPlots.add(this);
    }

    abstract void makeBottomNeighbours(GardenPlot neighbour);

    abstract void makeRightNeighbours(GardenPlot neighbour);

    abstract int consumePerimeter();

    int computePriceRegion() {
      return this.regionPlots.stream().mapToInt(GardenPlot::consumePerimeter).sum() * this.regionPlots.size();
    }
  }

  private static class SimpleGardenPlot extends GardenPlot {

    int perimeter;

    SimpleGardenPlot(int crop) {
      super(crop);
      this.perimeter = 4;
    }

    @Override
    void makeBottomNeighbours(GardenPlot neighbour) {
      SimpleGardenPlot plot = (SimpleGardenPlot) neighbour;

      this.perimeter -= 1;
      plot.perimeter -= 1;

      if (this.regionPlots != plot.regionPlots) {
        this.regionPlots.addAll(plot.regionPlots);
        plot.regionPlots.forEach(e -> e.regionPlots = this.regionPlots);
      }
    }

    @Override
    void makeRightNeighbours(GardenPlot neighbour) {
      SimpleGardenPlot plot = (SimpleGardenPlot) neighbour;

      this.perimeter -= 1;
      plot.perimeter -= 1;

      if (this.regionPlots != plot.regionPlots) {
        this.regionPlots.addAll(plot.regionPlots);
        plot.regionPlots.forEach(e -> e.regionPlots = this.regionPlots);
      }
    }

    @Override
    int consumePerimeter() {
      return perimeter;
    }
  }

  private static class DiscountGardenPlot extends GardenPlot {

    List<DiscountGardenPlot>[] perimeters;

    DiscountGardenPlot(int crop) {
      super(crop);
      this.perimeters = new List[]{singleton(this), singleton(this), singleton(this), singleton(this)};
    }

    List<DiscountGardenPlot> singleton(DiscountGardenPlot plot) {
      List<DiscountGardenPlot> list = new ArrayList<>();
      list.add(plot);
      return list;
    }

    @Override
    void makeBottomNeighbours(GardenPlot gardenPlot) {
      DiscountGardenPlot neighbour = (DiscountGardenPlot) gardenPlot;

      combinePerimeters(neighbour, 0);
      combinePerimeters(neighbour, 2);

      int index = this.perimeters[3].indexOf(this);

      if (index != this.perimeters[3].size() - 1) {
        DiscountGardenPlot plot = this.perimeters[3].getLast();
        plot.perimeters[3] = singleton(plot);
      }

      this.perimeters[3] = null;
      neighbour.perimeters[1] = null;

      if (this.regionPlots != neighbour.regionPlots) {
        this.regionPlots.addAll(neighbour.regionPlots);
        neighbour.regionPlots.forEach(e -> e.regionPlots = this.regionPlots);
      }
    }

    @Override
    void makeRightNeighbours(GardenPlot gardenPlot) {
      DiscountGardenPlot neighbour = (DiscountGardenPlot) gardenPlot;

      combinePerimeters(neighbour, 1);
      combinePerimeters(neighbour, 3);

      this.perimeters[2] = null;
      neighbour.perimeters[0] = null;

      if (this.regionPlots != neighbour.regionPlots) {
        this.regionPlots.addAll(neighbour.regionPlots);
        neighbour.regionPlots.forEach(e -> e.regionPlots = this.regionPlots);
      }
    }

    void combinePerimeters(DiscountGardenPlot neighbour, int index) {
      if (this.perimeters[index] != null && neighbour.perimeters[index] != null) {
        this.perimeters[index].addAll(neighbour.perimeters[index]);
        neighbour.perimeters[index].forEach(e -> e.perimeters[index] = this.perimeters[index]);
      }
    }

    @Override
    int consumePerimeter() {
      int perimeter = 0;

      for (int i = 0; i < 4; i++) {
        if (this.perimeters[i] != null && !this.perimeters[i].isEmpty()) {
          perimeter += 1;
          perimeters[i].clear();
        }
      }

      return perimeter;
    }
  }

  private static <T extends GardenPlot> int computePrice(T[][] gardenPlots) {
    for (int y = 0; y < gardenPlots.length; y++) {
      for (int x = 0; x < gardenPlots[0].length; x++) {
        if (x < gardenPlots[0].length - 1 && gardenPlots[y][x].crop == gardenPlots[y][x + 1].crop) {
          gardenPlots[y][x].makeRightNeighbours(gardenPlots[y][x + 1]);
        }

        if (y < gardenPlots.length - 1 && gardenPlots[y][x].crop == gardenPlots[y + 1][x].crop) {
          gardenPlots[y][x].makeBottomNeighbours(gardenPlots[y + 1][x]);
        }
      }
    }

    int totalPrice = 0;

    for (int y = 0; y < gardenPlots.length; y++) {
      for (int x = 0; x < gardenPlots[0].length; x++) {
        totalPrice += gardenPlots[y][x].computePriceRegion();
        gardenPlots[y][x].regionPlots.clear();
      }
    }

    return totalPrice;
  }

  @Override
  public String solvePartOne(List<String> input) {
    SimpleGardenPlot[][] gardenPlots = input.stream()
        .map(str -> str.chars().mapToObj(SimpleGardenPlot::new).toArray(SimpleGardenPlot[]::new))
        .toArray(SimpleGardenPlot[][]::new);

    int totalPrice = computePrice(gardenPlots);

    return String.valueOf(totalPrice);
  }

  @Override
  public String solvePartTwo(List<String> input) {
    DiscountGardenPlot[][] gardenPlots = input.stream()
        .map(str -> str.chars().mapToObj(DiscountGardenPlot::new).toArray(DiscountGardenPlot[]::new))
        .toArray(DiscountGardenPlot[][]::new);

    int totalPrice = computePrice(gardenPlots);

    return String.valueOf(totalPrice);
  }
}
