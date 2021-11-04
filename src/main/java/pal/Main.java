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
                        // check if this combination was already printed
                        Set<Integer> currentMerged = new HashSet<>();

//                        checkNeighs(correctPacks.get(i), correctPacks.get(j));
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
            graph[from.label].adjacentNodes.add(to);
            graph[to.label].adjacentNodes.add(from);
        }

        PackGenerator generator = new PackGenerator(set, graph, pack_conn);
        generator.k_subsets(pack_size, 0, new int[pack_size],0);

        findIsoPacks(generator.correctPacks);

        System.out.print("");
    }
}
