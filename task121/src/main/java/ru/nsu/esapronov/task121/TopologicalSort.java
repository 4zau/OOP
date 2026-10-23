package ru.nsu.esapronov.task121;

import java.util.List;

/** Алгоритм топологической сортировки вершин графа. */
public interface TopologicalSort {
    /**
     * Возвращает топологический порядок вершин.
     *
     * @param graph сортируемый граф
     * @param <V> тип вершин
     * @return список вершин в топологическом порядке
     * @throws IllegalArgumentException если граф содержит цикл
     */
    <V> List<V> sort(Graph<V> graph);
}
