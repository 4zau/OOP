package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

abstract class GraphTest {
    protected abstract Graph<String> createGraph();

    @Test
    void addsAndRemovesVertices() {
        Graph<String> graph = createGraph();
        assertTrue(graph.vertices().isEmpty());
        assertTrue(graph.edges().isEmpty());
        assertFalse(graph.removeVertex("missing"));
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex(new String("A")));
        assertTrue(graph.containsVertex("A"));
        assertFalse(graph.containsVertex("B"));
        assertEquals(List.of("A"), graph.vertices());
        assertTrue(graph.neighbors("A").isEmpty());
        assertTrue(graph.removeVertex("A"));
        assertFalse(graph.removeVertex("A"));
        assertTrue(graph.vertices().isEmpty());
        assertTrue(graph.addVertex("A"));
    }

    @Test
    void edgesAreDirectedAndUnique() {
        Graph<String> graph = createGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        assertTrue(graph.addEdge("A", "B"));
        assertFalse(graph.addEdge("A", "B"));
        assertTrue(graph.containsEdge("A", "B"));
        assertFalse(graph.containsEdge("B", "A"));
        assertEquals(List.of("B"), graph.neighbors("A"));
        assertTrue(graph.neighbors("B").isEmpty());
        assertTrue(graph.addEdge("B", "A"));
        assertFalse(graph.removeEdge("A", "C"));
        assertTrue(graph.removeEdge("A", "B"));
        assertFalse(graph.removeEdge("A", "B"));
        assertTrue(graph.containsEdge("B", "A"));
        assertEquals(Set.of(new Edge<>("B", "A")), graph.edges());
    }

    @Test
    void selfLoopsCanBeAddedAndRemoved() {
        Graph<String> graph = createGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("B", "A");
        assertTrue(graph.addEdge("A", "A"));
        assertFalse(graph.addEdge("A", "A"));
        assertTrue(graph.containsEdge("A", "A"));
        assertEquals(List.of("A"), graph.neighbors("A"));
        assertFalse(graph.containsEdge("B", "B"));
        assertTrue(graph.removeEdge("A", "A"));
        assertFalse(graph.removeEdge("A", "A"));
        assertTrue(graph.neighbors("A").isEmpty());
        assertTrue(graph.containsEdge("B", "A"));
    }

    @Test
    void removingMiddleVertexDeletesOnlyIncidentEdges() {
        Graph<String> graph = createGraph();
        for (String vertex : List.of("A", "B", "C", "D")) {
            graph.addVertex(vertex);
        }
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("B", "B");
        graph.addEdge("D", "B");
        graph.addEdge("A", "D");
        graph.addEdge("C", "D");
        assertTrue(graph.removeVertex("B"));
        assertEquals(List.of("A", "C", "D"), graph.vertices());
        assertEquals(Set.of(new Edge<>("A", "D"), new Edge<>("C", "D")),
                graph.edges());
        assertTrue(graph.addVertex("B"));
        assertTrue(graph.neighbors("B").isEmpty());
        assertFalse(graph.containsEdge("A", "B"));
        assertTrue(graph.addEdge("B", "C"));
        assertTrue(graph.containsEdge("B", "C"));
        graph.removeVertex("A");
        graph.removeVertex("B");
        graph.removeVertex("D");
        assertEquals(List.of("C"), graph.vertices());
        assertTrue(graph.edges().isEmpty());
        graph.removeVertex("C");
        assertTrue(graph.vertices().isEmpty());
    }

    @Test
    void removesFirstMiddleAndLastEdgeWithoutLosingOtherEdges() {
        Graph<String> graph = createGraph();
        for (String vertex : List.of("A", "B", "C")) {
            graph.addVertex(vertex);
        }
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "A");
        graph.addEdge("A", "A");
        graph.removeEdge("B", "C");
        graph.removeEdge("A", "B");
        graph.removeEdge("A", "A");
        assertEquals(Set.of(new Edge<>("C", "A")), graph.edges());
        graph.removeEdge("C", "A");
        assertTrue(graph.edges().isEmpty());
        assertTrue(graph.addEdge("B", "C"));
    }

    @Test
    void rejectsUnknownVerticesAndNullArguments() {
        Graph<String> graph = createGraph();
        graph.addVertex("A");
        assertThrows(IllegalArgumentException.class, () -> graph.neighbors("X"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("X", "A"));
        assertThrows(IllegalArgumentException.class, () -> graph.addEdge("A", "X"));
        assertThrows(IllegalArgumentException.class, () -> graph.removeEdge("X", "A"));
        assertThrows(IllegalArgumentException.class, () -> graph.containsEdge("A", "X"));
        assertThrows(NullPointerException.class, () -> graph.addVertex(null));
        assertThrows(NullPointerException.class, () -> graph.removeVertex(null));
        assertThrows(NullPointerException.class, () -> graph.containsVertex(null));
        assertThrows(NullPointerException.class, () -> graph.neighbors(null));
        assertThrows(NullPointerException.class, () -> graph.addEdge(null, "A"));
        assertThrows(NullPointerException.class, () -> graph.addEdge("A", null));
        assertEquals(List.of("A"), graph.vertices());
        assertTrue(graph.edges().isEmpty());
    }

    @Test
    void returnedCollectionsDoNotExposeInternalStorage() {
        Graph<String> graph = createGraph();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        List<String> vertices = graph.vertices();
        List<String> neighbors = graph.neighbors("A");
        Set<Edge<String>> edges = graph.edges();
        assertThrows(UnsupportedOperationException.class, () -> vertices.add("C"));
        assertThrows(UnsupportedOperationException.class, neighbors::clear);
        edges.clear();
        assertTrue(graph.containsEdge("A", "B"));
        graph.removeVertex("B");
        assertEquals(List.of("A", "B"), vertices);
        assertEquals(List.of("B"), neighbors);
    }

    @Test
    void randomOperationsMatchIndependentSets() {
        Graph<String> graph = createGraph();
        Set<String> vertices = new HashSet<>();
        Set<Edge<String>> edges = new HashSet<>();
        Random random = new Random(121);
        for (int step = 0; step < 500; step++) {
            String from = "v" + random.nextInt(8);
            String to = "v" + random.nextInt(8);
            switch (random.nextInt(4)) {
                case 0 -> assertEquals(vertices.add(from), graph.addVertex(from));
                case 1 -> {
                    assertEquals(vertices.remove(from), graph.removeVertex(from));
                    edges.removeIf(e -> e.from().equals(from) || e.to().equals(from));
                }
                case 2 -> {
                    if (vertices.contains(from) && vertices.contains(to)) {
                        assertEquals(edges.add(new Edge<>(from, to)), graph.addEdge(from, to));
                    }
                }
                case 3 -> {
                    if (vertices.contains(from) && vertices.contains(to)) {
                        assertEquals(edges.remove(new Edge<>(from, to)),
                                graph.removeEdge(from, to));
                    }
                }
                default -> throw new AssertionError();
            }
            assertEquals(vertices, new HashSet<>(graph.vertices()), "step " + step);
            assertEquals(edges, graph.edges(), "step " + step);
            for (String vertex : vertices) {
                Set<String> expected = new HashSet<>();
                for (Edge<String> edge : edges) {
                    if (edge.from().equals(vertex)) {
                        expected.add(edge.to());
                    }
                }
                assertEquals(expected, new HashSet<>(graph.neighbors(vertex)));
                assertEquals(expected.size(), graph.neighbors(vertex).size());
            }
        }
    }
}
