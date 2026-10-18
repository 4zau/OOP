package ru.nsu.esapronov.task121;

import java.util.List;
import java.util.Objects;

/** Общие правила операций и сравнения. */
public abstract class AbstractGraph<V> implements Graph<V> {
    protected final void requireVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");
        if (!containsVertex(vertex)) {
            throw new IllegalArgumentException("Unknown vertex: " + vertex);
        }
    }

    protected final void requireEndpoints(V from, V to) {
        requireVertex(from);
        requireVertex(to);
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof Graph<?> graph)) {
            return false;
        }

        return new java.util.HashSet<>(vertices()).equals(
                new java.util.HashSet<>(graph.vertices()))
                && edges().equals(graph.edges());
    }

    @Override
    public final int hashCode() {
        return 31 * new java.util.HashSet<>(vertices()).hashCode() + edges().hashCode();
    }

    @Override
    public final String toString() {
        return "Graph{vertices=" + vertices() + ", edges=" + edges() + "}";
    }

    protected final List<V> snapshot(List<V> values) {
        return List.copyOf(values);
    }
}
