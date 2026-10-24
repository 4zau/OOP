package ru.nsu.esapronov.task121;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Ориентированный невзвешенный граф без кратных рёбер.
 */
public interface Graph<V> {
    /** Возвращает false, если вершина уже существует. */
    boolean addVertex(V vertex);

    /** Удаляет также все входящие и исходящие рёбра. */
    boolean removeVertex(V vertex);

    /** Обе вершины должны существовать. Повторное добавление возвращает false. */
    boolean addEdge(V from, V to);

    /** Обе вершины должны существовать. Отсутствующее ребро даёт false. */
    boolean removeEdge(V from, V to);

    boolean containsVertex(V vertex);

    /** Для неизвестной вершины бросает IllegalArgumentException. */
    boolean containsEdge(V from, V to);

    List<V> vertices();

    /** Исходящие соседи. Для неизвестной вершины IllegalArgumentException. */
    List<V> neighbors(V vertex);

    /** Возвращает множество всех направленных рёбер графа. */
    default Set<Edge<V>> edges() {
        Set<Edge<V>> result = new LinkedHashSet<>();
        for (V from : vertices()) {
            for (V to : neighbors(from)) {
                result.add(new Edge<>(from, to));
            }
        }
        return result;
    }

    /** Создаёт новый граф выбранного представления. */
    static Graph<String> readFromFile(Path path, Representation representation)
            throws IOException {
        return GraphIo.read(path, representation);
    }

    /**
     * Создаёт граф с вершинами заданного типа.
     *
     * @param path путь к файлу
     * @param representation способ хранения графа
     * @param vertexParser преобразование имени вершины в значение
     * @param <V> тип вершин
     * @return прочитанный граф
     * @throws IOException при ошибке чтения, формата или преобразования вершины
     */
    static <V> Graph<V> readFromFile(Path path, Representation representation,
                                     Function<String, V> vertexParser)
            throws IOException {
        return GraphIo.read(path, representation, vertexParser);
    }
}
