package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A directed graph consisting of a set of vertices and edges. Vertices are given by
 * any type N. Edges have source and target vertices. Self-loops are not allowed, i.e.,
 * if v1 = v2, then (v1,v2) is not in E.
 *
 * @param <N> type of vertices, which must be immutable and comparable
 */
public interface Graph<N extends Comparable<N>> {

    /**
     * Return true if this graph contains a given vertex. For example, consider a graph
     * with vertices {v1, v2, v3, v4} and edges {(v1,v2), (v1,v4), (v2, v4)}. Then,
     * containsVertex(v1) = true and containsVertex(v5) = false.
     *
     * @param vertex a vertex
     * @return {@code true} if the graph contains vertex
     */
    boolean containsVertex(@NotNull N vertex);

    /**
     * Return true if this graph contains an edge from source to target. Consider a graph
     * with vertices {v1, v2, v3, v4} and edges {(v1,v2), (v1,v4), (v2, v4)}. Then,
     * Then, containsEdge(v1,v2) = true and containsEdge(v2,v3) = false.
     *
     * @param source a source vertex
     * @param target a target vertex
     * @return true if source and target is connected by an edge
     */
    boolean containsEdge(@NotNull N source, @NotNull N target);

    /**
     * Return the set of adjacent vertices of a given vertex. If the vertex is not in
     * the graph, returns the empty set. For example, consider a graph with vertices
     * {v1, v2, v3, v4} and edges {(v1,v2), (v1,v4), (v2, v4)}. Then,
     *   getNeighborhood(v1) = {v2, v4}, getNeighborhood(v2) = {v4},
     *   getNeighborhood(v3) = {}, and getNeighborhood(v4) = {}.
     *
     * @param vertex a vertex
     * @return the set of adjacent vertices of vertex
     */
    @NotNull Set<N> getNeighborhood(N vertex);

    /**
     * Returns all the vertices in this graph.
     *
     * @return the set of vertices in the graph
     */
    @NotNull Set<N> getVertices();

    /**
     * Returns all the edges in this graph.
     *
     * @return the set of edges in the graph
     */
    @NotNull Set<Edge<N>> getEdges();


    /**
     * Add a given vertex to this graph, and returns {@code true} if the graph is
     * changed. If the graph already contains the vertex, the graph is not changed and
     * this method returns {@code false}.
     *
     * @param vertex a vertex to add
     * @return {@code true} if the graph is modified, {@code false} otherwise
     */
    boolean addVertex(@NotNull N vertex);

    /**
     * Remove a vertex from this graph, together with all edges that involve the vertex,
     * and returns {@code true} if the graph is changed. If the graph does not contain the
     * vertex, the graph is not changed and this method returns {@code false}.
     *
     * @param vertex a vertex to remove
     * @return {@code true} if the graph is modified, {@code false} otherwise
     */
    boolean removeVertex(@NotNull N vertex);

    /**
     * Add an edge to this graph, and returns {@code true} if the graph is changed. If
     * source and/or target vertices are not in the graph, missing vertices are added to
     * the graph. If the graph already contains the edge, the graph is not changed and
     * this method returns {@code false}.
     *
     * @param source a source vertex
     * @param target a target vertex
     * @return {@code true} if the graph is modified, {@code false} otherwise
     */
    boolean addEdge(@NotNull N source, @NotNull N target);

    /**
     * Remove an edge from this graph, and returns {@code true} if the graph is changed.
     * If the graph does not contain the edge, the graph is not changed and this method
     * returns {@code false}.
     *
     * @param source a source vertex
     * @param target a target vertex
     * @return {@code true} if the graph is modified, {@code false} otherwise
     */
    boolean removeEdge(@NotNull N source, @NotNull N target);

    /**
     * Returns all the vertices that are reachable from a given vertex in this graph,
     * based on a breadth-first search strategy.
     *
     * @param vertex a vertex
     * @return the set of reachable vertices from {@code vertex}
     */
    default @NotNull Set<N> findReachableVertices(@NotNull N vertex) {
        Set<N> seen = new HashSet<>();
        Set<N> frontier = new HashSet<>();

        if (containsVertex(vertex))
            frontier.add(vertex);

        while (!seen.containsAll(frontier)) {
            seen.addAll(frontier);
            frontier = frontier.stream()
                    .flatMap(n -> getNeighborhood(n).stream())
                    .filter(n -> !seen.contains(n))
                    .collect(Collectors.toSet());
        }
        return Collections.unmodifiableSet(seen);
    }
}
