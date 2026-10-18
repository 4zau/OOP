package ru.nsu.esapronov.task121;

import java.util.List;

public interface TopologicalSort {
    /** IllegalArgumentException при цикле **/
    <V> List<V> sort(Graph<V> graph);
}
