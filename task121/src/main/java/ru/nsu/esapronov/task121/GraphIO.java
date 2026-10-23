package ru.nsu.esapronov.task121;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Формат: N M, затем N строк с именами вершин, затем M строк "from to".
 */
public final class GraphIO {
    public static Graph<String> read(Path path, Representation representation)
            throws IOException {
        return read(path, representation, Function.identity());
    }

    /**
     * Читает граф, преобразуя объявленные имена вершин в значения типа V.
     *
     * @param path путь к файлу
     * @param representation способ хранения графа
     * @param vertexParser преобразование имени вершины в значение
     * @param <V> тип вершин
     * @return прочитанный граф
     * @throws IOException при ошибке чтения, формата или преобразования вершины
     */
    public static <V> Graph<V> read(Path path, Representation representation,
                                    Function<String, V> vertexParser)
            throws IOException {
        Objects.requireNonNull(representation, "representation");
        Objects.requireNonNull(vertexParser, "vertexParser");
        Graph<V> graph = representation.create();
        Map<String, V> verticesByName = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String[] header = fields(reader, 1, 2);
            int n = count(header[0]);
            int m = count(header[1]);

            for (int i = 0; i < n; i++) {
                String name = fields(reader,  i + 2, 1)[0];
                if (verticesByName.containsKey(name)) {
                    throw new IOException("duplicate vertex: " + name);
                }

                V vertex;
                try {
                    vertex = Objects.requireNonNull(vertexParser.apply(name), "vertex");
                } catch (RuntimeException exception) {
                    throw new IOException("Cannot parse vertex: " + name, exception);
                }

                if (!graph.addVertex(vertex)) {
                    throw new IOException("Duplicate vertex after conversion: " + name);
                }
                verticesByName.put(name, vertex);
            }

            for (int i = 0; i < m; i++) {
                String[] edge = fields(reader, n + i + 2, 2);
                if (!verticesByName.containsKey(edge[0])
                        || !verticesByName.containsKey(edge[1])) {
                    throw new IOException("Unknown endpoint: " + String.join(" ", edge));
                }
                V from = verticesByName.get(edge[0]);
                V to = verticesByName.get(edge[1]);
                if (!graph.addEdge(from, to)) {
                    throw new IOException("Duplicate edge: " + String.join(" ", edge));
                }
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    throw new IOException("Unexpected data after the last edge");
                }
            }
        }
        return graph;
    }

    private static int count(String value) throws IOException {
        try {
            int result = Integer.parseInt(value);
            if (result < 0) {
                throw new NumberFormatException();
            }
            return result;
        } catch (NumberFormatException exception) {
            throw new IOException("Expected a positive integer: " + value, exception);
        }
    }

    private static String[] fields(BufferedReader reader, int lineNumber, int expected)
            throws IOException {
        String line = reader.readLine();

        if (line == null || line.isBlank()) {
            throw new IOException("Missing data on line " + lineNumber);
        }

        String[] result = line.strip().split("\\s+");
        if (result.length != expected) {
            throw new IOException("Expected " + expected + " fields on line " + lineNumber);
        }
        return result;
    }
}
