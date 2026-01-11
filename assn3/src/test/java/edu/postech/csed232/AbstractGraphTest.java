package edu.postech.csed232;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A test class for Graph with blackbox test methods
 *
 * @param <V> type of vertices
 * @param <G> type of Graph
 */
@Disabled
abstract public class AbstractGraphTest<V extends Comparable<V>, G extends Graph<V>> {

    G graph;
    V v1, v2, v3, v4, v5, v6, v7, v8;

    @Test
    void testAddVertex() {
        assertTrue(graph.addVertex(v1));
        assertTrue(graph.containsVertex(v1));
    }

    @Test
    void testAddDuplicateVertices() {
        assertTrue(graph.addVertex(v6));
        assertTrue(graph.addVertex(v7));
        assertFalse(graph.addVertex(v6));
        assertTrue(graph.containsVertex(v6));
        assertTrue(graph.containsVertex(v7));
    }

    @Test
    void testFindReachableVertices1() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v4);
        graph.addEdge(v2, v4);
        graph.addEdge(v3, v6);
        graph.addEdge(v6, v3);

        assertEquals(Set.of(v1, v2, v4), graph.findReachableVertices(v1));
        assertEquals(Set.of(v2, v4), graph.findReachableVertices(v2));
        assertEquals(Set.of(v3, v6), graph.findReachableVertices(v3));
        assertEquals(Set.of(v4), graph.findReachableVertices(v4));
        assertTrue(graph.findReachableVertices(v5).isEmpty());
    }

    // TODO: write black-box test cases for each method of Graph according
    //  to the specification, based on equivalence partitioning
    @Test
    void testRemoveVertex() {
        assertFalse(graph.removeVertex(v1));
        graph.addVertex(v1);
        assertTrue(graph.removeVertex(v1));
        assertFalse(graph.removeVertex(v1));
        assertFalse(graph.containsVertex(v1));
    }

    @Test
    void testRemoveDuplicateVertices() {
        assertFalse(graph.removeVertex(v3));
        graph.addVertex(v3);
        graph.addVertex(v5);
        assertTrue(graph.containsVertex(v3));
        assertTrue(graph.removeVertex(v3));
        assertTrue(graph.removeVertex(v5));
        assertFalse(graph.removeVertex(v5));
        assertFalse(graph.containsVertex(v3));
        assertFalse(graph.containsVertex(v5));
    }

    @Test
    void testContainsVertex() {
        assertFalse(graph.containsVertex(v4));
        graph.addVertex(v4);
        assertTrue(graph.containsVertex(v4));
    }

    @Test
    void testAddEdge1() {
        assertTrue(graph.addEdge(v1, v2));
        assertTrue(graph.containsEdge(v1, v2));
    }

    @Test
    void testAddEdge2() {
        assertFalse(graph.addEdge(v1, v1));
    }

    @Test
    void testAddDuplicateEdges() {
        assertTrue(graph.addEdge(v1, v3));
        assertTrue(graph.addEdge(v3, v4));
        assertFalse(graph.addEdge(v3, v4));
        assertTrue(graph.containsEdge(v1, v3));
        assertTrue(graph.containsEdge(v3, v4));
        assertFalse(graph.containsEdge(v2, v4));
    }

    @Test
    void testAddEdgeWithVertices() {
        assertFalse(graph.containsVertex(v2));
        assertFalse(graph.containsVertex(v3));
        graph.addEdge(v2, v3);
        assertTrue(graph.containsVertex(v2));
        assertTrue(graph.containsVertex(v3));
        assertTrue(graph.containsEdge(v2, v3));
    }

    @Test
    void testRemoveEdge() {
        assertFalse(graph.removeEdge(v1, v2));
        graph.addEdge(v1, v2);
        assertTrue(graph.removeEdge(v1, v2));
        assertFalse(graph.removeEdge(v1, v2));
        assertFalse(graph.containsEdge(v1, v2));
    }

    @Test
    void testRemoveDuplicateEdges() {
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v4);
        assertTrue(graph.containsEdge(v1, v2));
        assertTrue(graph.containsEdge(v1, v4));
        assertTrue(graph.removeEdge(v1, v2));
        assertTrue(graph.removeEdge(v1, v4));
        assertFalse(graph.removeEdge(v1, v4));
        assertFalse(graph.containsEdge(v1, v2));
        assertFalse(graph.containsEdge(v1, v4));
    }

    @Test
    void testContainsEdge() {
        assertFalse(graph.containsEdge(v1, v2));
        graph.addEdge(v1, v2);
        assertTrue(graph.containsEdge(v1, v2));
    }

    @Test
    void testGetVertices() {
        assertTrue(graph.getVertices().isEmpty());
        graph.addVertex(v4);
        graph.addVertex(v5);
        graph.addVertex(v6);
        assertEquals(Set.of(v4, v5, v6), graph.getVertices());
    }

    @Test
    void testGetEdges() {
        assertTrue(graph.getEdges().isEmpty());
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v4);
        graph.addEdge(v2, v4);
        assertEquals(Set.of(new Edge<>(v1, v2), new Edge<>(v1, v4), new Edge<>(v2, v4)), graph.getEdges());
    }

    @Test
    void testGetNeighbors() {
        graph.addVertex(v1);
        graph.addVertex(v2);
        assertTrue(graph.getNeighborhood(v1).isEmpty());
        graph.addEdge(v1, v2);
        graph.addEdge(v1, v5);
        assertEquals(Set.of(v2, v5), graph.getNeighborhood(v1));

        assertTrue(graph.getNeighborhood(v2).isEmpty());
        assertTrue(graph.getNeighborhood(v5).isEmpty());
        assertTrue(graph.getNeighborhood(v6).isEmpty());
    }

    @Test
    void testFindReachableVertices2() {
        graph.addVertex(v1);
        assertEquals(Set.of(v1), graph.findReachableVertices(v1));
    }
}
