package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AbstractGraphTest {
    private Graph<String> sample(Representation representation, boolean reverse) {
        Graph<String> graph = representation.create();
        for (String vertex : reverse ? List.of("C", "B", "A") : List.of("A", "B", "C")) {
            graph.addVertex(vertex);
        }
        graph.addEdge("A", "B");
        return graph;
    }

    @Test
    void equalityAndHashCodeIgnoreRepresentationAndInsertionOrder() {
        for (Representation first : Representation.values()) {
            for (Representation second : Representation.values()) {
                Graph<String> a = sample(first, false);
                Graph<String> b = sample(second, true);
                Graph<String> c = sample(Representation.INCIDENCE_MATRIX, false);
                assertEquals(a, a);
                assertEquals(a, b);
                assertEquals(b, a);
                assertEquals(b, c);
                assertEquals(a, c);
                assertEquals(a.hashCode(), b.hashCode());
                Set<Graph<String>> set = new HashSet<>();
                set.add(a);
                assertTrue(set.contains(b));
            }
        }
    }

    @Test
    void equalityIncludesIsolatedVerticesAndEdgeDirection() {
        Graph<String> a = sample(Representation.ADJACENCY_LIST, false);
        Graph<String> b = sample(Representation.ADJACENCY_MATRIX, false);
        assertNotEquals(null, a);
        assertFalse(a.equals(null));
        assertFalse(a.equals("not a graph"));
        b.removeVertex("C");
        assertNotEquals(a, b);
        b.addVertex("C");
        b.removeEdge("A", "B");
        b.addEdge("B", "A");
        assertNotEquals(a, b);
        b.removeEdge("B", "A");
        assertNotEquals(a, b);
    }

    @Test
    void toStringShowsVerticesAndEdges() {
        Graph<String> graph = sample(Representation.ADJACENCY_LIST, false);
        String text = graph.toString();
        assertTrue(text.startsWith("Graph{vertices="));
        assertTrue(text.contains("A"));
        assertTrue(text.contains("B"));
        assertTrue(text.contains("C"));
        assertTrue(text.contains("from=A"));
        assertTrue(text.contains("to=B"));
    }
}
