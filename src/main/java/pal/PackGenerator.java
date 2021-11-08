package pal;


import java.util.*;
import java.util.stream.Collectors;

public class PackGenerator {

    private Node[] graph;
    private int[] set;
    public List<Pack> correctPacks = new ArrayList<>();
    private int pack_connections;

    public PackGenerator(int[] set) {
        this.set = set;
    }

    public PackGenerator(int[] set, Node[] graph, int pack_connections) {
        this.graph = graph;
        this.set = set;
        this.pack_connections = pack_connections;
    }


    // result is always sorted
    // return -1 if not found, index in result otherwise
    private int isNodeInResult(int val, int[] result) {
        for (int i =0; i < result.length; i++) {
            if (result[i] > val) // if value is bigger => in sorted array it does not contain val
                return -1;
            if (result[i] == val)
                return i;
        }

        return -1; // val not found
    }


    // returns whether a pack is connected
    // also checks the number of roads between points
    public boolean isConnected(int[] pack) {
      Set<Integer> packNodes =  Arrays.stream(pack).boxed().collect(Collectors.toSet());
      Set<Integer> visited = new HashSet<>();
      int edgesVisited = 0;


      for (int i : pack) {// for each node in pack check neighbours
            int packEdgescount = 0;
            for (Node adjacentNode : graph[i].adjacentNodes){
                if (packNodes.contains(adjacentNode.label))
                    packEdgescount++;
                if (!visited.contains(adjacentNode.label) && isNodeInResult(adjacentNode.label, pack) != -1){
                    visited.add(adjacentNode.label);
                    edgesVisited++;
                } else if (isNodeInResult(adjacentNode.label, pack) != -1) {
                    edgesVisited++;
                }
            }
          if (packEdgescount == 0)
              return false;
      }
      return edgesVisited == 2*pack_connections;
    }

    // update pack's nodeDegrees
    private void updatePackNodeDegrees(Pack pack) {
        int[] nodesDegrees = new int[pack.nodes.size()];

        int idx = 0;
        Map<Integer, List<Integer>> mapDegs = new HashMap<>();
        for (Node i : pack.nodes){ // for each node, count n of neigbhours from pack
            for (Node j : i.adjacentNodes)
                if (pack.nodes.contains(j))
                    nodesDegrees[idx]++;



            if (!mapDegs.containsKey(nodesDegrees[idx]))
                mapDegs.put(nodesDegrees[idx], new LinkedList<Integer>());
            mapDegs.get(nodesDegrees[idx]).add(i.label);

            i.mapDegrees.put(pack, nodesDegrees[idx]);
            idx++;
        }
        Arrays.sort(nodesDegrees);
        pack.mapDegrees = mapDegs;
        pack.nodesDegrees = nodesDegrees;
    }

    private void createPack(int[] result) {
        if (!isConnected(result))
            return;

        Pack p = new Pack();
        for (int i : result){
            p.nodes.add(graph[i]);
        }
        updatePackNodeDegrees(p);
        correctPacks.add(p);
    }
    //       3. -------------------------------------------------------------------------
//       Idea:
//       Collect the items of the subset in a single result list.
//       Add one item to the result on each recursion level.
//       The technical advantage of the idea is that no
//       intermediate lists (results) are generated.
    public void k_subsets(int k, int i_start, int[] result, int depth){
        if (depth == k){
            createPack(result);
            return;
        }

        int i_lastStart = set.length - (k-depth);
        for (int i = i_start; i < i_lastStart+1; i++) {
            result[depth] = set[i];
            k_subsets(k, i+1, result, depth+1);
        }
    }
}
