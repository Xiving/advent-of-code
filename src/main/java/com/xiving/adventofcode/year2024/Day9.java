package com.xiving.adventofcode.year2024;

import java.util.List;

public class Day9 extends Year2024Day {

  private static final char ASCII_0 = '0';

  public Day9() {
    super(9);
  }

  private static record DiskMap(int[] map, int totalSize) {

  }

  private static DiskMap fromInput(List<String> input) {
    char[] chars = input.get(0).toCharArray();
    int[] diskMap = new int[chars.length];
    int totalSizeDisk = 0;

    for (int i = 0; i < diskMap.length; i++) {
      diskMap[i] = chars[i] - ASCII_0;
      totalSizeDisk += diskMap[i];
    }

    return new DiskMap(diskMap, totalSizeDisk);
  }

  private int[] diskBlocksFromDiskMap(DiskMap diskMap) {
    int[] diskBlocks = new int[diskMap.totalSize];
    int diskBlockI = 0;

    for (int i = 0; i < diskMap.map.length; i++) {
      for (int j = 0; j < diskMap.map[i]; j++) {
        diskBlocks[diskBlockI] = ((i & 1) == 1) ? -1 : (i >> 1);
        diskBlockI++;
      }
    }

    return diskBlocks;
  }

  private static void compress(int[] diskBlocks) {
    int frontPivot = 0;
    int backPivot = diskBlocks.length - 1;

    while (frontPivot < backPivot) {
      if (diskBlocks[frontPivot] >= 0) {
        frontPivot++;
      } else if (diskBlocks[backPivot] == -1) {
        backPivot--;
      } else {
        diskBlocks[frontPivot] = diskBlocks[backPivot];
        diskBlocks[backPivot] = -1;
        frontPivot++;
        backPivot--;
      }
    }
  }

  private static int findEmptyBlock(int[] diskBlocks, int size, int lowerBound, int upperBound) {
    int i = lowerBound;

    while (i < upperBound) {
      while (diskBlocks[i] != -1) {
        i++;
      }

      int emptyBlock = i;
      int emptyBlockSize = 0;

      while (i + emptyBlockSize < upperBound && diskBlocks[i + emptyBlockSize] == -1 && emptyBlockSize < size) {
        emptyBlockSize++;
      }

      if (emptyBlockSize == size) {
        return emptyBlock;
      } else {
        i = emptyBlock + emptyBlockSize;
      }
    }

    return -1;
  }

  private static void compressWholeBlocks(int[] diskBlocks) {
    int firstEmptyBlock = findEmptyBlock(diskBlocks, 1, 0, diskBlocks.length);
    int blockStart = diskBlocks.length - 1;

    for (int id = diskBlocks[diskBlocks.length - 1]; id > 0; id--) {
      while (diskBlocks[blockStart] != id) {
        blockStart--;
      }

      int blockSize = 1;

      while (blockStart - 1 >= 0 && diskBlocks[blockStart - 1] == id) {
        blockStart--;
        blockSize++;
      }

      int emptyBlock = findEmptyBlock(diskBlocks, blockSize, firstEmptyBlock, blockStart);

      if (emptyBlock != -1) {
        for (int i = 0; i < blockSize; i++) {
          diskBlocks[emptyBlock + i] = id;
          diskBlocks[blockStart + i] = -1;
        }

        if (emptyBlock == firstEmptyBlock) {
          firstEmptyBlock = findEmptyBlock(diskBlocks,1, firstEmptyBlock, diskBlocks.length);
        }
      }
    }
  }

  private static long calcChecksum(int[] diskBlocks) {
    long totalChecksum = 0;

    for (int i = 0; i < diskBlocks.length; i++) {
      if (diskBlocks[i] >= 0) {
        totalChecksum += (long) diskBlocks[i] * i;
      }
    }

    return totalChecksum;
  }

  @Override
  public String solvePartOne(List<String> input) {
    DiskMap diskMap = fromInput(input);
    int[] diskBlocks = diskBlocksFromDiskMap(diskMap);
    compress(diskBlocks);
    return String.valueOf(calcChecksum(diskBlocks));
  }

  @Override
  public String solvePartTwo(List<String> input) {
    DiskMap diskMap = fromInput(input);
    int[] diskBlocks = diskBlocksFromDiskMap(diskMap);
    compressWholeBlocks(diskBlocks);
    return String.valueOf(calcChecksum(diskBlocks));
  }
}
