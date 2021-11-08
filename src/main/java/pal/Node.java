package pal;

import java.util.*;

public class Node {

    public int label;
    public List<Node> adjacentNodes = new ArrayList<>();
    public Map<Pack, Integer> mapDegrees = new HashMap<>();
    public Map<Pack, List<Integer>> mapAdjacentDegrees = new HashMap<>();

    public Node(int label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return label == node.label;
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
}
