package ru.nsu.esapronov.task121;

/** Фабрика для выбора реализации при чтении файла. */
public enum Representation {
    ADJACENCY_LIST, ADJACENCY_MATRIX, INCIDENCE_MATRIX;

    public <V> Graph<V> create() {
        return switch (this) {
            case ADJACENCY_LIST -> new AdjacencyListGraph<>();
            case ADJACENCY_MATRIX -> new AdjacencyMatrixGraph<>();
            case INCIDENCE_MATRIX -> new IncidenceMatrixGraph<>();
        };
    }
}
