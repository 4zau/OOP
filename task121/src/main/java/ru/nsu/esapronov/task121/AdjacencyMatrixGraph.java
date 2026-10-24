package ru.nsu.esapronov.task121;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** matrix[i][j] == true означает ребро i -> j. */
public final class AdjacencyMatrixGraph<V> extends AbstractGraph<V> {
    private final List<V> vertices = new ArrayList<>();
    private boolean[][] matrix = new boolean[0][0];

    @Override
    public boolean containsVertex(V vertex) {
        return vertices.contains(Objects.requireNonNull(vertex, "vertex"));
    }

    @Override
    public boolean addVertex(V vertex) {
        if (containsVertex(vertex)) {
            return false;
        }

        int n = vertices.size();

        boolean[][] next = new boolean[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, next[i], 0, n);
        }

        vertices.add(vertex);
        matrix = next;
        return true;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");

        int removed = vertices.indexOf(vertex);
        if (removed < 0) {
            return false;

        }

        int n = vertices.size();

        boolean[][] next = new boolean[n - 1][n - 1];
        for (int i = 0, ni = 0; i < n; i++) {
            if (i == removed) {
                continue;
            }

            for (int j = 0, nj = 0; j < n; j++) {
                if (j != removed) {
                    next[ni][nj++] = matrix[i][j];
                }
            }
            ni++;
        }
        vertices.remove(removed);

        matrix = next;
        return true;
    }

    @Override
    public boolean addEdge(V from, V to) {
        requireEndpoints(from, to);

        int i = vertices.indexOf(from);
        int j = vertices.indexOf(to);
        boolean existed = matrix[i][j];

        matrix[i][j] = true;
        return !existed;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        requireEndpoints(from, to);

        int i = vertices.indexOf(from);
        int j = vertices.indexOf(to);
        boolean existed = matrix[i][j];

        matrix[i][j] = false;
        return existed;
    }

    @Override
    public boolean containsEdge(V from, V to) {
        requireEndpoints(from, to);
        return matrix[vertices.indexOf(from)][vertices.indexOf(to)];
    }

    @Override
    public List<V> vertices() {
        return snapshot(vertices);
    }

    @Override
    public List<V> neighbors(V vertex) {
        requireVertex(vertex);

        int row = vertices.indexOf(vertex);

        List<V> result = new ArrayList<>();
        for (int j = 0; j < vertices.size(); j++) {
            if (matrix[row][j]) {
                result.add(vertices.get(j));
            }

        }
        return snapshot(result);
    }
}
