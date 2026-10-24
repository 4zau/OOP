package ru.nsu.esapronov.task121;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Для каждой вершины хранится множество исходящих соседей. */
public final class AdjacencyListGraph<V> extends AbstractGraph<V> {
    private final Map<V, Set<V>> adjacency = new LinkedHashMap<>();

    @Override
    public boolean addVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");
        return adjacency.putIfAbsent(vertex, new LinkedHashSet<>()) == null;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");

        if (adjacency.remove(vertex) == null) {
            return false;
        }
        for (Set<V> neighbors : adjacency.values()) {
            neighbors.remove(vertex);
        }

        return true;
    }

    @Override
    public boolean containsVertex(V vertex) {
        return adjacency.containsKey(Objects.requireNonNull(vertex, "vertex"));
    }

    @Override
    public boolean addEdge(V from, V to) {
        requireEndpoints(from, to);
        return adjacency.get(from).add(to);
    }

    @Override
    public boolean removeEdge(V from, V to) {
        requireEndpoints(from, to);
        return adjacency.get(from).remove(to);
    }

    @Override
    public boolean containsEdge(V from, V to) {
        requireEndpoints(from, to);
        return adjacency.get(from).contains(to);
    }

    @Override
    public List<V> vertices() {
        return List.copyOf(adjacency.keySet());
    }

    @Override
    public List<V> neighbors(V vertex) {
        requireVertex(vertex);
        return List.copyOf(adjacency.get(vertex));
    }
}
