package ru.nsu.esapronov.task121;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Строки - вершины, столбцы - рёбра.
 * Значения ячеек EDGE_START, EDGE_END и SELF_LOOP.
 */
public final class IncidenceMatrixGraph<V> extends AbstractGraph<V> {
    private static final int EDGE_START = -1;
    private static final int EDGE_END = 1;
    private static final int SELF_LOOP = 2;

    private final List<V> vertices = new ArrayList<>();
    private int[][] matrix = new int[0][0];
    private int edgeCount;

    @Override
    public boolean containsVertex(V vertex) {
        return vertices.contains(Objects.requireNonNull(vertex, "vertex"));
    }

    @Override
    public boolean addVertex(V vertex) {
        if (containsVertex(vertex)) {
            return false;
        }

        int[][] next = new int[vertices.size() + 1][edgeCount];
        for (int i = 0; i < vertices.size(); i++) {
            System.arraycopy(matrix[i], 0, next[i], 0, edgeCount);
        }

        vertices.add(vertex);
        matrix = next;

        return true;
    }

    /** Поиск столбца ребра по индексам его концов. */
    private int column(int from, int to) {
        for (int e = 0; e < edgeCount; e++) {
            if (from == to ? matrix[from][e] == SELF_LOOP
                    : matrix[from][e] == EDGE_START && matrix[to][e] == EDGE_END) {
                return e;
            }
        }
        return -1;
    }

    private void removeColumn(int removed) {
        int[][] next = new int[vertices.size()][edgeCount - 1];

        for (int i = 0; i < vertices.size(); i++) {
            System.arraycopy(matrix[i], 0, next[i], 0, removed);
            System.arraycopy(matrix[i], removed + 1, next[i], removed,
                    edgeCount - removed - 1);
        }

        matrix = next;
        edgeCount--;
    }

    @Override
    public boolean removeVertex(V vertex) {
        Objects.requireNonNull(vertex, "vertex");
        int removed = vertices.indexOf(vertex);
        if (removed < 0) {
            return false;
        }

        for (int e = edgeCount - 1; e >= 0; e--) {
            if (matrix[removed][e] != 0) {
                removeColumn(e);
            }
        }

        int[][] next = new int[vertices.size() - 1][edgeCount];
        for (int i = 0, ni = 0; i < vertices.size(); i++) {
            if (i != removed) {
                System.arraycopy(matrix[i], 0, next[ni++], 0, edgeCount);
            }
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
        if (column(i, j) >= 0) {
            return false;
        }

        int[][] next = new int[vertices.size()][edgeCount + 1];
        for (int v = 0; v < vertices.size(); v++) {
            System.arraycopy(matrix[v], 0, next[v], 0, edgeCount);
        }

        if (i == j) {
            next[i][edgeCount] = SELF_LOOP;
        } else {
            next[i][edgeCount] = EDGE_START;
            next[j][edgeCount] = EDGE_END;
        }

        matrix = next;
        edgeCount++;
        return true;
    }

    @Override
    public boolean removeEdge(V from, V to) {
        requireEndpoints(from, to);

        int found = column(vertices.indexOf(from), vertices.indexOf(to));
        if (found < 0) {
            return false;
        }

        removeColumn(found);
        return true;
    }

    @Override
    public boolean containsEdge(V from, V to) {
        requireEndpoints(from, to);
        return column(vertices.indexOf(from), vertices.indexOf(to)) >= 0;
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
        for (int e = 0; e < edgeCount; e++) {
            if (matrix[row][e] == SELF_LOOP) {
                result.add(vertex);
            } else if (matrix[row][e] == EDGE_START) {
                for (int v = 0; v < vertices.size(); v++) {
                    if (matrix[v][e] == EDGE_END) {
                        result.add(vertices.get(v));
                        break;
                    }
                }
            }
        }

        return snapshot(result);
    }
}

