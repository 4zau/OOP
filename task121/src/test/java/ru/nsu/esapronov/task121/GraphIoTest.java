package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class GraphIoTest {
    @TempDir
    Path directory;

    private Path file(String text) throws IOException {
        Path path = directory.resolve("graph.txt");
        Files.writeString(path, text, StandardCharsets.UTF_8);
        return path;
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void readsStringsAndPreservesIsolatedVertices(Representation representation)
            throws IOException {
        Path path = file("3 1\nА\nБ\nотдельная\nА Б\n");
        Graph<String> graph = GraphIo.read(path, representation);
        assertEquals(List.of("А", "Б", "отдельная"), graph.vertices());
        assertEquals(Set.of(new Edge<>("А", "Б")), graph.edges());
        assertTrue(graph.neighbors("отдельная").isEmpty());
    }

    @ParameterizedTest
    @EnumSource(Representation.class)
    void readsIntegersThroughParser(Representation representation) throws IOException {
        Graph<Integer> graph = GraphIo.read(
                file("3 2\n10\n20\n30\n10 20\n20 20\n"),
                representation, Integer::parseInt);
        assertEquals(List.of(10, 20, 30), graph.vertices());
        assertEquals(Set.of(new Edge<>(10, 20), new Edge<>(20, 20)), graph.edges());
        assertTrue(graph.neighbors(30).isEmpty());
    }

    @Test
    void graphInterfaceProvidesBothReadingOverloads() throws IOException {
        Path path = file("2 1\n1\n2\n1 2\n");
        Graph<String> strings = Graph.readFromFile(path, Representation.ADJACENCY_LIST);
        Graph<Integer> integers = Graph.readFromFile(
                path, Representation.INCIDENCE_MATRIX, Integer::parseInt);
        assertTrue(strings.containsEdge("1", "2"));
        assertTrue(integers.containsEdge(1, 2));
    }

    @Test
    void parserRunsOncePerDeclaredVertexAndEndpointsReuseObjects() throws IOException {
        AtomicInteger calls = new AtomicInteger();
        Graph<Object> graph = GraphIo.read(
                file("2 2\nA\nB\nA B\nB A\n"),
                Representation.ADJACENCY_LIST, name -> {
                    calls.incrementAndGet();
                    return new Object();
                });
        assertEquals(2, calls.get());
        Object first = graph.vertices().get(0);
        Object second = graph.vertices().get(1);
        assertSame(second, graph.neighbors(first).get(0));
        assertSame(first, graph.neighbors(second).get(0));
    }

    @Test
    void readsCustomVertexType() throws IOException {
        record Vertex(String name) { }

        Graph<Vertex> graph = GraphIo.read(
                file("2 1\nA\nB\nA B\n"),
                Representation.ADJACENCY_MATRIX, Vertex::new);
        assertTrue(graph.containsEdge(new Vertex("A"), new Vertex("B")));
    }

    @Test
    void acceptsWhitespaceAndEmptyGraph() throws IOException {
        Graph<String> graph = GraphIo.read(
                file("  2\t1  \n A \n B \n A\tB \n\n  \n"),
                Representation.ADJACENCY_LIST);
        assertTrue(graph.containsEdge("A", "B"));
        assertTrue(GraphIo.read(file("0 0\n"), Representation.ADJACENCY_LIST)
                .vertices().isEmpty());
        assertTrue(GraphIo.read(file("0 0"), Representation.INCIDENCE_MATRIX)
                .edges().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "2",
        "1 0 extra\n",
        "x 0\n",
        "0 x\n",
        "-1 0\n",
        "0 -1\n",
        "2147483648 0\n",
        "1 1\nA\nA A extra\n",
        "1 1\nA\nunknown A\n",
        "1 1\nA\nA unknown\n",
        "1 2\nA\nA A\nA A\n",
        "0 0\nextra\n"
    })
    void rejectsMalformedFiles(String text) throws IOException {
        Path path = file(text);
        for (Representation representation : Representation.values()) {
            assertThrows(IOException.class, () -> GraphIo.read(path, representation));
        }
    }

    @Test
    void rejectsDuplicateVerticesAfterConversion() throws IOException {
        Path path = file("2 0\n1\n01\n");
        for (Representation representation : Representation.values()) {
            assertThrows(IOException.class,
                    () -> GraphIo.read(path, representation, Integer::parseInt));
        }
    }

    @Test
    void parsingFailureRetainsItsCause() throws IOException {
        Path path = file("1 0\nnot-a-number\n");
        IOException exception = assertThrows(IOException.class,
                () -> GraphIo.read(path, Representation.ADJACENCY_LIST, Integer::parseInt));
        assertInstanceOf(NumberFormatException.class, exception.getCause());
        assertTrue(exception.getMessage().contains("not-a-number"));
    }

    @Test
    void rejectsParserReturningNull() throws IOException {
        Path path = file("1 0\nA\n");
        IOException exception = assertThrows(IOException.class,
                () -> GraphIo.read(path, Representation.ADJACENCY_LIST, name -> null));
        assertInstanceOf(NullPointerException.class, exception.getCause());
    }

    @Test
    void endpointMustUseDeclaredName() throws IOException {
        Path path = file("1 1\n1\n1 01\n");
        assertThrows(IOException.class,
                () -> GraphIo.read(path, Representation.ADJACENCY_LIST, Integer::parseInt));
    }

    @Test
    void rejectsMissingFileAndNullArguments() throws IOException {
        assertThrows(IOException.class, () -> GraphIo.read(
                directory.resolve("missing.txt"), Representation.ADJACENCY_LIST));
        Path path = file("0 0\n");
        assertThrows(NullPointerException.class, () -> GraphIo.read(path, null));
        assertThrows(NullPointerException.class,
                () -> GraphIo.read(path, Representation.ADJACENCY_LIST, null));
        assertThrows(NullPointerException.class,
                () -> GraphIo.read(null, Representation.ADJACENCY_LIST));
    }
}
