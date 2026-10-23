package ru.nsu.esapronov.task121;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class EdgeTest {
    @Test
    void storesOrderedEndpointsAndComparesByValue() {
        Edge<String> edge = new Edge<>("A", "B");
        Edge<String> equal = new Edge<>(new String("A"), new String("B"));
        assertEquals("A", edge.from());
        assertEquals("B", edge.to());
        assertEquals(edge, equal);
        assertEquals(edge.hashCode(), equal.hashCode());
        assertNotEquals(edge, new Edge<>("B", "A"));
        assertEquals(new Edge<>("A", "A"), new Edge<>("A", "A"));
        assertTrue(edge.toString().contains("from=A"));
    }

    @Test
    void rejectsNullEndpoints() {
        assertThrows(NullPointerException.class, () -> new Edge<>(null, "B"));
        assertThrows(NullPointerException.class, () -> new Edge<>("A", null));
    }
}
