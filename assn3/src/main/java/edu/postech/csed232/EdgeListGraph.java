package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class EdgeListGraph<N extends Comparable<N>> implements Graph<N> {

    /**
     * A set of vertices in the graph
     */
    private final @NotNull Set<N> vertices;

    /**
     * A set of edges in the graph
     */
    private final @NotNull List<Edge<N>> edges;

    /**
     * Creates an empty graph
     */
    public EdgeListGraph() {
        vertices = new HashSet<>();
        edges = new ArrayList<>();
    }

    @Override
    public boolean containsVertex(@NotNull N vertex) {
        // TODO: implement this, and remove the following throw statement
        return vertices.contains(vertex);
    }

    @Override
    public boolean addVertex(@NotNull N vertex) {
        // TODO: implement this, and remove the following throw statement
        return vertices.add(vertex);
    }

    @Override
    public boolean removeVertex(@NotNull N vertex) {
        // TODO: implement this, and remove the following throw statement
        if (!vertices.contains(vertex)) return false;
        vertices.remove(vertex);
        edges.removeIf(edge-> edge.source().equals(vertex) || edge.target().equals(vertex));
        return true;
    }

    @Override
    public boolean containsEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this, and remove the following throw statement
        for (Edge<N> edge : edges) {
            if (edge.source().equals(source) && edge.target().equals(target)) return true;
        }
        return false;
    }

    @Override
    public boolean addEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this, and remove the following throw statement
        if (source.equals(target)) return false;
        // return false if already exist edge
        if (containsEdge(source, target)) return false;

        vertices.add(source);
        vertices.add(target);

        edges.add(new Edge<>(source, target));
        return true;
    }

    @Override
    public boolean removeEdge(@NotNull N source, @NotNull N target) {
        // TODO: implement this, and remove the following throw statement
        return edges.removeIf(edge-> edge.source().equals(source) && edge.target().equals(target));
    }

    @Override
    public @NotNull Set<N> getNeighborhood(N vertex) {
        // TODO: implement this, and remove the following throw statement
        if (!vertices.contains(vertex)) return Set.of();
        Set<N> neighbors = new HashSet<>();
        for (Edge<N> edge : edges) {
            if (edge.source().equals(vertex)) {
                neighbors.add(edge.target());
            }
        }
        return neighbors;
    }

    @Override
    public @NotNull Set<N> getVertices() {
        return Collections.unmodifiableSet(vertices);
    }

    @Override
    public @NotNull Set<Edge<N>> getEdges() {
        return Set.copyOf(edges);
    }

}
