package ru.nsu.esapronov.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class TableIteratorTest {
    @Test
    void emptyIteratorHasNoElements() {
        Iterator<Entry<Integer, String>> iterator = new HashTable<Integer, String>().iterator();
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void visitsCollisionChainsAndSkipsEmptyBuckets() {
        HashTable<Integer, String> table = new HashTable<>();
        Map<Integer, String> expected = Map.of(0, "a", 16, "b", 32, "c", 15, "d");
        expected.forEach(table::put);
        Map<Integer, String> actual = new HashMap<>();
        Iterator<Entry<Integer, String>> iterator = table.iterator();
        while (iterator.hasNext()) {
            assertTrue(iterator.hasNext());
            Entry<Integer, String> entry = iterator.next();
            assertFalse(actual.containsKey(entry.getKey()));
            actual.put(entry.getKey(), entry.getValue());
        }
        assertEquals(expected, actual);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void nextDoesNotRequireHasNextAndIteratorsAreIndependent() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        Iterator<Entry<String, Integer>> first = table.iterator();
        Iterator<Entry<String, Integer>> second = table.iterator();
        assertEquals(new Entry<>("key", 1), first.next());
        assertFalse(first.hasNext());
        assertTrue(second.hasNext());
        assertEquals(new Entry<>("key", 1), second.next());
    }

    @Test
    void insertionAndRemovalInvalidateIterators() {
        HashTable<Integer, String> table = new HashTable<>();
        Iterator<Entry<Integer, String>> empty = table.iterator();
        table.put(1, "one");
        assertThrows(ConcurrentModificationException.class, empty::hasNext);
        assertThrows(ConcurrentModificationException.class, empty::next);
        Iterator<Entry<Integer, String>> beforeRemove = table.iterator();
        table.remove(1);
        assertThrows(ConcurrentModificationException.class, beforeRemove::hasNext);
        assertThrows(ConcurrentModificationException.class, beforeRemove::next);
    }

    @Test
    void replacingValuesDoesNotInvalidateIterator() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(0, "old");
        table.put(16, "old");
        Iterator<Entry<Integer, String>> iterator = table.iterator();
        table.put(0, "put");
        table.update(16, "update");
        Map<Integer, String> actual = new HashMap<>();
        while (iterator.hasNext()) {
            Entry<Integer, String> entry = iterator.next();
            actual.put(entry.getKey(), entry.getValue());
        }
        assertEquals(Map.of(0, "put", 16, "update"), actual);
    }

    @Test
    void failedRemovalsDoNotInvalidateIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        final Iterator<Entry<String, Integer>> iterator = table.iterator();
        assertFalse(table.remove("missing"));
        assertFalse(table.remove("key", 2));
        assertThrows(NoSuchElementException.class, () -> table.update("missing", 1));
        assertEquals(new Entry<>("key", 1), iterator.next());
    }

    @Test
    void returnedEntryKeepsItsValueAfterUpdate() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        Entry<String, Integer> entry = table.iterator().next();
        table.update("key", 2);
        assertEquals(1, entry.getValue());
        assertEquals(2, table.get("key"));
    }

    @Test
    void iteratorRemoveIsUnsupported() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        Iterator<Entry<String, Integer>> iterator = table.iterator();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, iterator::remove);
        assertEquals(1, table.size());
    }
}
