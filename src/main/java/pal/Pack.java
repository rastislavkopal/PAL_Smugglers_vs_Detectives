package pal;

import java.util.*;

public class Pack {
    public List<Node> nodes;
    public int[] nodesDegrees;
    public Map<Integer, List<Integer>> mapDegrees = new HashMap<>();

    public Pack() {
        this.nodes = new LinkedList<Node>();
    }

    public Pack(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pack pack = (Pack) o;
        return Objects.equals(nodes, pack.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
}
