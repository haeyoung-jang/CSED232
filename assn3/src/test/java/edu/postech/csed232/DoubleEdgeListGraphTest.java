package edu.postech.csed232;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleEdgeListGraphTest extends AbstractGraphTest<Double, EdgeListGraph<Double>> {

    @BeforeEach
    void setUp() {
        graph = new EdgeListGraph<>();
        v1 = 1.1;
        v2 = 2.2;
        v3 = 3.3;
        v4 = 4.4;
        v5 = 5.5;
        v6 = 6.6;
        v7 = 7.7;
        v8 = 8.8;
    }

    // TODO: write more white-box test cases to achieve more code coverage, if needed.
    //  You do not need to add more test methods, if you tests already meet the desired coverage.
    @Test
    void testEdgeCompareSourceIsSame() {
        Edge<Double> e1 = new Edge<>(v1, v2);
        Edge<Double> e2 = new Edge<>(v1, v3);

        assertTrue(e1.compareTo(e2) < 0);
    }

    @Test
    void testEdgeCompareSourceIsDifferent() {
        Edge<Double> e1 = new Edge<>(v1, v2);
        Edge<Double> e2 = new Edge<>(v2, v4);

        assertTrue(e1.compareTo(e2) < 0);
    }

    @Test
    void testEdgeToString() {
        Edge<Double> e1 = new Edge<>(v4, v6);

        assertEquals("(4.4,6.6)", e1.toString());
    }

    @Test
    void testRemoveVertex_SourceEqualsVertex() {
        graph.addEdge(v1, v5);
        graph.removeVertex(v1);

        assertFalse(graph.containsEdge(v1, v5));
    }

    @Test
    void testRemoveVertex_TargetEqualsVertex() {
        graph.addEdge(v1, v5);
        graph.removeVertex(v5);

        assertFalse(graph.containsEdge(v1, v5));
    }

    @Test
    void testRemoveVertex_BothNotEqualsVertex() {
        graph.addEdge(v3, v4);
        graph.removeVertex(v7);

        assertTrue(graph.containsEdge(v3, v4));
    }

    @Test
    void testRemoveVertex_duplicated() {
        graph.addEdge(v1, v5);
        graph.addEdge(v2, v5);
        graph.removeVertex(v1);

        assertFalse(graph.containsEdge(v1, v5));
        assertTrue(graph.containsEdge(v2, v5));
    }

    @Test
    void testRemoveEdge_removeIf_ExistEdge() {
        graph.addEdge(v1, v2);

        assertTrue(graph.removeEdge(v1, v2));
    }

    @Test
    void testRemoveEdge_removeIf_notEqualSource() {
        graph.addEdge(v5, v8);
        assertFalse(graph.removeEdge(v4, v8));
    }

    @Test
    void testRemoveEdge_removeIf_notEqualTarget() {
        graph.addEdge(v5, v8);
        assertFalse(graph.removeEdge(v5, v7));
    }
}
