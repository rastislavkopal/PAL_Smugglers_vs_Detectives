package pal;

import java.io.IOException;
import java.util.*;

public class Main {

    private static int n_connections;
    private static int n_nodes;
    private static int pack_size;
    private static int pack_conn;
    private static Node[] graph;


    private static void printPacks(Pack p1, Pack p2){
        StringBuilder sb = new StringBuilder();
        for (Node n : p1.nodes)
            sb.append(n.label + " ");
        for (Node n : p2.nodes)
            sb.append(n.label + " ");

        System.out.println(sb.toString());
    }


    private static boolean hasIntersection(Pack pack1, Pack pack2){
        for (Node i : pack1.nodes){
            for (Node j : pack2.nodes){
                if (j.label == i.label)
                    return true;
            }
        }
        return false;
    }

    private static void updatePackNodesNeighbourDegrees(Pack p){
        for (Node n : p.nodes){
            // check each neighbour
            if (n.mapAdjacentDegrees.containsKey(p))
                continue;

            List<Integer> neighDegrees = new ArrayList<>();
            for (Node adjacentNode : n.adjacentNodes){
                if (p.nodes.contains(adjacentNode))
                    neighDegrees.add(adjacentNode.mapDegrees.get(p));
            }
            Collections.sort(neighDegrees);
            n.mapAdjacentDegrees.put(p, neighDegrees);
        }
    }

    public static boolean hasEqualAdjacentDegrees(Pack pack1, Pack pack2){
        boolean[] hasSame = new boolean[pack1.nodes.size()];
        Set<Integer> pack2NodesUsed = new HashSet<>();
        Set<Integer> pack1NodesUsed = new HashSet<>();

        int idx = 0;
        for (Map.Entry<Integer, List<Integer>> packDegrees : pack1.mapDegrees.entrySet()){
            for (int i : packDegrees.getValue()) {
                Node node = graph[i];
                for (int j : pack2.mapDegrees.get(packDegrees.getKey())) { // iterate all possible pack2 combination and find same adjacentNodesDegrees
                    Node possibleNode = graph[j];
                    if (!pack1NodesUsed.contains(i) && !pack2NodesUsed.contains(j) && possibleNode.mapAdjacentDegrees.get(pack2).equals(node.mapAdjacentDegrees.get(pack1))){
                        hasSame[idx] = true;
                        pack2NodesUsed.add(j);
                        pack1NodesUsed.add(i);
                    }
                }
                idx++;
            }
        }

        for (boolean i : hasSame) {
            if (i == false)
                return false;
        }

        return true;
    }

    public static void findIsoPacks(List<Pack> correctPacks){
        Set<Set<Integer>> printed = new HashSet<>();

        for (int i = 0; i < correctPacks.size(); i++) { // for each pack search all other packs if they are isomorphic
            for (int j = i; j < correctPacks.size(); j++){ // start at the position of i
                if (!correctPacks.get(i).equals(correctPacks.get(j))){
                    if (hasIntersection(correctPacks.get(i), correctPacks.get(j))) // have an item in common -> continue
                        continue;
                    // if the sorted nodeDegrees are different => its not isomorphic
                    if (!Arrays.equals(correctPacks.get(i).nodesDegrees, correctPacks.get(j).nodesDegrees))
                        continue;
                    // check isomorphism
                    if (Arrays.equals(correctPacks.get(i).nodesDegrees, correctPacks.get(j).nodesDegrees)){

                        updatePackNodesNeighbourDegrees(correctPacks.get(i));
                        updatePackNodesNeighbourDegrees(correctPacks.get(j));

                        if (!hasEqualAdjacentDegrees(correctPacks.get(i), correctPacks.get(j)) )
                            continue;

                        // check if this combination was already printed
                        Set<Integer> currentMerged = new HashSet<>();
                        for (Node x : correctPacks.get(i).nodes)
                            currentMerged.add(x.label);
                        for (Node x : correctPacks.get(j).nodes)
                            currentMerged.add(x.label);

                        if (printed.contains(currentMerged))
                            continue;

                        printPacks(correctPacks.get(i), correctPacks.get(j));
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        InputReader r = new InputReader();

        n_nodes = r.nextInt();
        n_connections = r.nextInt();
        pack_size = r.nextInt();
        pack_conn = r.nextInt();

        graph  = new Node[n_nodes];
        int[] set = new int[n_nodes];

        for (int i =0; i<n_nodes;i++) {
            set[i] = i;
            graph[i] = new Node(i);
        }

        for (int i = 0; i < n_connections; i++){
            Node from = new Node(r.nextInt());
            Node to = new Node(r.nextInt());
            graph[from.label].adjacentNodes.add(graph[to.label]);
            graph[to.label].adjacentNodes.add(graph[from.label]);
        }

        PackGenerator generator = new PackGenerator(set, graph, pack_conn);
        generator.k_subsets(pack_size, 0, new int[pack_size],0);

        findIsoPacks(generator.correctPacks);

        System.out.print("");
    }
}
