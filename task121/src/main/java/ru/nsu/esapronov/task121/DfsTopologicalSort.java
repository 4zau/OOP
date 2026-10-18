package ru.nsu.esapronov.task121;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class DfsTopologicalSort implements TopologicalSort {
    private enum Color { GRAY, BLACK }

    @Override
    public <V> List<V> sort(Graph<V> graph) {
        Objects.requireNonNull(graph, "graph");

        Map<V, Color> colors = new HashMap<>();
        List<V> result = new ArrayList<>();

        for (V vertex : graph.vertices()) {
            if (!colors.containsKey(vertex)) {
                visit(graph, vertex, colors, result);
            }
        }

        Collections.reverse(result);
        return List.copyOf(result);
    }

    private <V> void visit(Graph<V> graph, V vertex,
                           Map<V, Color> colors, List<V> result) {
        colors.put(vertex, Color.GRAY);

        List<V> neighbors = graph.neighbors(vertex);

        for (int i = 0; i < neighbors.size(); i++) {
            V next = neighbors.get(i);

            if (colors.get(next) == Color.GRAY) {
                throw new IllegalArgumentException();
            }

            if (!colors.containsKey(next)) {
                visit(graph, next, colors, result);
            }
        }

        colors.put(vertex, Color.BLACK);
        result.add(vertex);
    }
}