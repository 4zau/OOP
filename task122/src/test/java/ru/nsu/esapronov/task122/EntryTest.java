package ru.nsu.esapronov.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class EntryTest {
    @Test
    void exposesKeyAndValue() {
        Entry<String, Integer> entry = new Entry<>("key", 42);
        assertEquals("key", entry.getKey());
        assertEquals(42, entry.getValue());
        assertEquals("key=42", entry.toString());
    }

    @Test
    void equalityAndHashCodeUseBothFields() {
        Entry<String, Integer> entry = new Entry<>("key", 42);
        Entry<String, Integer> equal = new Entry<>(new String("key"), 42);
        assertEquals(entry, entry);
        assertEquals(entry, equal);
        assertEquals(equal, entry);
        assertEquals(entry.hashCode(), equal.hashCode());
        assertNotEquals(new Entry<>("other", 42), entry);
        assertNotEquals(new Entry<>("key", 43), entry);
        assertNotEquals(null, entry);
        assertNotEquals("key=42", entry);
    }

    @Test
    void handlesNullFields() {
        Entry<String, Integer> entry = new Entry<>(null, null);
        assertNull(entry.getKey());
        assertNull(entry.getValue());
        assertEquals(new Entry<>(null, null), entry);
        assertEquals(0, entry.hashCode());
        assertEquals("null=null", entry.toString());
        assertNotEquals(new Entry<>("key", null), entry);
        assertNotEquals(new Entry<>(null, 1), entry);
    }
}
