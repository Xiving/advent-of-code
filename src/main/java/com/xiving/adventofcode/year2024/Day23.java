package com.xiving.adventofcode.year2024;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Day23 extends Year2024Day {

  public Day23() {
    super(23);
  }

  private static boolean idStartsWithT(int id) {
    return id >> 5 == ('t' - 'a');
  }

  private static int nameFromChars(char c1, char c2) {
    return ((c1 - 'a') << 5) + (c2 - 'a');
  }

  private static String strFromName(int id) {
    return String.format("%c%c", (char) ((id >> 5) + 'a'), (char) ((id & 31) + 'a'));
  }

  private static Map<Integer, Collection<Integer>> connectionsFromInput(List<String> input) {
    Map<Integer, Collection<Integer>> connections = new HashMap<>();

    for (int i = 0; i < input.size(); i++) {
      char[] chars = input.get(i).toCharArray();
      int name1 = nameFromChars(chars[0], chars[1]);
      int name2 = nameFromChars(chars[3], chars[4]);

      if (name1 < name2) {
        connections.compute(name1, (k, ls) -> {
          ls = ls == null ? new TreeSet<>() : ls;
          ls.add(name2);
          return ls;
        });
      } else {
        connections.compute(name2, (k, ls) -> {
          ls = ls == null ? new TreeSet<>() : ls;
          ls.add(name1);
          return ls;
        });
      }
    }

    return connections;
  }

  private record Nodes(Nodes tail, Integer value) {

    public String getNames() {
      return tail == null ? strFromName(value) : String.format("%s,%s", tail.getNames(), strFromName(value));
    }

    public boolean anyStartWithT() {
      return idStartsWithT(value) || (tail != null && tail.anyStartWithT());
    }

  }

  private record StarGraph(Nodes coreNodes, Collection<Integer> edgeNodes) {

    public String getNames() {
      return coreNodes.getNames() + "," + strFromName(edgeNodes.iterator().next());
    }

    public long countNamesStartWithT() {
      return coreNodes.anyStartWithT() ? edgeNodes.size() : edgeNodes.stream().filter(Day23::idStartsWithT).count();
    }

  }


  private static List<StarGraph> fromConnections(Map<Integer, Collection<Integer>> connections) {
    return connections.entrySet().stream()
        .map(e -> new StarGraph(new Nodes(null, e.getKey()), e.getValue()))
        .toList();
  }

  private static List<Integer> intersection(Collection<Integer> ls1, Collection<Integer> ls2) {
    if (ls1.isEmpty() || ls2.isEmpty()) {
      return Collections.emptyList();
    }

    Iterator<Integer> it1 = ls1.iterator();
    Iterator<Integer> it2 = ls2.iterator();
    Integer value1 = it1.next();
    Integer value2 = it2.next();
    List<Integer> intersection = new ArrayList<>();

    while (true) {
      if (value1 < value2) {
        if (it1.hasNext()) {
          value1 = it1.next();
        } else {
          return intersection;
        }
      } else if (value2 < value1) {
        if (it2.hasNext()) {
          value2 = it2.next();
        } else {
          return intersection;
        }
      } else {
        intersection.add(value1);

        if (it1.hasNext() && it2.hasNext()) {
          value1 = it1.next();
          value2 = it2.next();
        } else {
          return intersection;
        }
      }
    }
  }

  private static List<StarGraph> growStarGraphs(List<StarGraph> graphs, Map<Integer, Collection<Integer>> connectionMap) {
    List<StarGraph> nextGraphs = new ArrayList<>();

    for (int i = 0; i < graphs.size(); i++) {
      StarGraph graph = graphs.get(i);

      for (Integer edgeNode : graph.edgeNodes) {
        Collection<Integer> edgeNodeConnections = connectionMap.get(edgeNode);

        if (edgeNodeConnections == null) {
          continue;
        }

        List<Integer> nextEdgeNodes = intersection(graph.edgeNodes, edgeNodeConnections);

        if (nextEdgeNodes.isEmpty()) {
          continue;
        }

        nextGraphs.add(new StarGraph(new Nodes(graph.coreNodes, edgeNode), nextEdgeNodes));
      }
    }

    return nextGraphs;
  }

  @Override
  public String solvePartOne(List<String> input) {
    Map<Integer, Collection<Integer>> connections = connectionsFromInput(input);
    List<StarGraph> starGraphs = fromConnections(connections);
    starGraphs = growStarGraphs(starGraphs, connections);
    return String.valueOf(starGraphs.stream().mapToLong(StarGraph::countNamesStartWithT).sum());
  }

  @Override
  public String solvePartTwo(List<String> input) {
    Map<Integer, Collection<Integer>> connections = connectionsFromInput(input);
    List<StarGraph> starGraphs = fromConnections(connections);

    while (true) {
      List<StarGraph> nextGraphs = growStarGraphs(starGraphs, connections);

      if (nextGraphs.isEmpty()) {
        break;
      }

      starGraphs = nextGraphs;
    }

    return starGraphs.getFirst().getNames();
  }
}
