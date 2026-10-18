package ru.nsu.esapronov.task121;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

/** хан. */
public final class KahnTopologicalSort implements TopologicalSort {
    @Override
    public <V> List<V> sort(Graph<V> graph) {
        Objects.requireNonNull(graph, "graph");
        List<V> vertices = graph.vertices();

        Map<V, Integer> indegree = new HashMap<>();
        for (V vertex : vertices) {
            indegree.put(vertex, 0);
        }

        for (V vertex : vertices) {
            for (V next : graph.neighbors(vertex)) {
                indegree.put(next, indegree.get(next) + 1);
            }
        }

        Queue<V> ready = new ArrayDeque<>();
        for (V vertex : vertices) {
            if (indegree.get(vertex) == 0) {
                ready.add(vertex);
            }
        }

        List<V> result = new ArrayList<>();
        while (!ready.isEmpty()) {
            V vertex = ready.remove();
            result.add(vertex);
            for (V next : graph.neighbors(vertex)) {
                int remaining = indegree.get(next) - 1;
                indegree.put(next, remaining);
                if (remaining == 0) {
                    ready.add(next);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalArgumentException();
        }
        return List.copyOf(result);
    }
}
