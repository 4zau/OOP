package ru.nsu.esapronov.task121;

/** Фабрика для выбора реализации при чтении файла. */
public enum Representation {
    ADJACENCY_LIST, ADJACENCY_MATRIX, INCIDENCE_MATRIX;

    /**
     * Создаёт пустой граф выбранного представления.
     *
     * @param <V> тип вершин
     * @return новый пустой граф
     */
    public <V> Graph<V> create() {
        return switch (this) {
            case ADJACENCY_LIST -> new AdjacencyListGraph<>();
            case ADJACENCY_MATRIX -> new AdjacencyMatrixGraph<>();
            case INCIDENCE_MATRIX -> new IncidenceMatrixGraph<>();
        };
    }
}
