package edu.postech.csed232;

import org.jetbrains.annotations.NotNull;

/**
 * An edge of a graph, given by a pair of two vertices of type V.
 *
 * @param <N> type of vertices, which must be immutable and comparable
 */
public record Edge<N extends Comparable<N>>(N source, N target) implements Comparable<Edge<N>> {

    @Override
    public int compareTo(@NotNull Edge<N> o) {
        int c1 = this.source.compareTo(o.source);
        return c1 != 0 ? c1 : this.target.compareTo(o.target);
    }

    @Override
    public String toString() {
        return "(" + source + "," + target + ")";
    }
}
