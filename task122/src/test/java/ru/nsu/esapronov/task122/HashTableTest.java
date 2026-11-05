package ru.nsu.esapronov.task122;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.jupiter.api.Test;

class HashTableTest {
    private record Key(int id) {
        @Override
        public int hashCode() {
            return 1;
        }
    }

    @Test
    void newTableIsEmpty() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
        assertNull(table.get("missing"));
        assertFalse(table.containsKey("missing"));
        assertFalse(table.remove("missing"));
        assertFalse(table.remove("missing", null));
        assertEquals("{}", table.toString());
    }

    @Test
    void putAddsAndReplacesUsingKeyEquality() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(new String("key"), 1);
        table.put(new String("key"), 2);
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
        assertTrue(table.containsKey("key"));
        assertEquals(2, table.get("key"));
    }

    @Test
    void updateRequiresExistingKey() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.update("missing", 1));
        assertTrue(table.isEmpty());
        table.put("key", 1);
        table.update("key", 2);
        assertEquals(2, table.get("key"));
        assertEquals(1, table.size());
    }

    @Test
    void supportsNullKeysAndValues() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put(null, null);
        table.put("key", null);
        assertEquals(2, table.size());
        assertTrue(table.containsKey(null));
        assertTrue(table.containsKey("key"));
        assertNull(table.get(null));
        assertNull(table.get("key"));
        table.update(null, 7);
        assertEquals(7, table.get(null));
        assertTrue(table.remove(null, 7));
        assertTrue(table.remove("key", null));
        assertTrue(table.isEmpty());
    }

    @Test
    void removesHeadMiddleAndTailOfCollisionChain() {
        HashTable<Key, Integer> table = new HashTable<>();
        for (int i = 0; i < 5; i++) {
            table.put(new Key(i), i);
        }
        assertNull(table.get(new Key(99)));
        assertFalse(table.remove(new Key(99)));
        assertTrue(table.remove(new Key(2)));
        assertTrue(table.remove(new Key(0)));
        assertTrue(table.remove(new Key(4)));
        assertEquals(2, table.size());
        assertEquals(1, table.get(new Key(1)));
        assertEquals(3, table.get(new Key(3)));
        assertFalse(table.containsKey(new Key(2)));
        assertFalse(table.remove(new Key(2)));
    }

    @Test
    void conditionalRemoveRequiresMatchingValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        assertFalse(table.remove("key", 2));
        assertFalse(table.remove("missing", 1));
        assertEquals(1, table.size());
        assertEquals(1, table.get("key"));
        assertTrue(table.remove("key", 1));
        assertTrue(table.isEmpty());
    }

    @Test
    void preservesEntriesWhileGrowingAndShrinking() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 300; i++) {
            table.put(i, i * 2);
        }
        assertEquals(300, table.size());
        for (int i = 0; i < 300; i++) {
            assertEquals(i * 2, table.get(i));
        }
        for (int i = 0; i < 295; i++) {
            assertTrue(table.remove(i));
        }
        assertEquals(5, table.size());
        for (int i = 295; i < 300; i++) {
            assertEquals(i * 2, table.get(i));
        }
        for (int i = 0; i < 100; i++) {
            table.put(i, i);
        }
        assertEquals(105, table.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, table.get(i));
        }
        for (int i = 295; i < 300; i++) {
            assertEquals(i * 2, table.get(i));
        }
    }

    @Test
    void supportsNegativeHashes() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(Integer.MIN_VALUE, "min");
        table.put(-1, "negative");
        assertEquals("min", table.get(Integer.MIN_VALUE));
        assertEquals("negative", table.get(-1));
    }

    @Test
    void equalityAndHashCodeDependOnEntries() {
        HashTable<Key, Integer> first = new HashTable<>();
        HashTable<Key, Integer> second = new HashTable<>();
        assertEquals(first, second);
        first.put(new Key(1), null);
        first.put(new Key(2), 20);
        second.put(new Key(2), 20);
        second.put(new Key(1), null);
        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first.hashCode(), second.hashCode());
        assertNotEquals(null, first);
        assertNotEquals("table", first);
        second.update(new Key(1), 10);
        assertNotEquals(first, second);
        second.remove(new Key(1));
        second.put(new Key(3), null);
        assertNotEquals(first, second);
        second.remove(new Key(3));
        assertNotEquals(first, second);
    }

    @Test
    void formatsEntriesAsText() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("key", 1);
        assertEquals("{key=1}", table.toString());
        table.put("other", 2);
        String text = table.toString();
        assertTrue(text.equals("{key=1, other=2}") || text.equals("{other=2, key=1}"));
    }

    @Test
    void mixedOperationsAgreeWithHashMap() {
        HashTable<Key, Integer> table = new HashTable<>();
        Map<Key, Integer> expected = new HashMap<>();
        Random random = new Random(122);
        for (int i = 0; i < 2000; i++) {
            Key key = random.nextInt(10) == 0 ? null : new Key(random.nextInt(100));
            Integer value = random.nextBoolean() ? null : random.nextInt();
            switch (random.nextInt(4)) {
                case 0 -> {
                    table.put(key, value);
                    expected.put(key, value);
                }
                case 1 -> {
                    boolean present = expected.containsKey(key);
                    expected.remove(key);
                    assertEquals(present, table.remove(key));
                }
                case 2 -> assertEquals(expected.remove(key, value), table.remove(key, value));
                default -> {
                    if (expected.containsKey(key)) {
                        expected.put(key, value);
                        table.update(key, value);
                    } else {
                        assertThrows(NoSuchElementException.class, () -> table.update(key, value));
                    }
                }
            }
            assertEquals(expected.size(), table.size());
            assertEquals(expected.isEmpty(), table.isEmpty());
            assertEquals(expected.containsKey(key), table.containsKey(key));
            assertEquals(expected.get(key), table.get(key));
            Map<Key, Integer> actual = new HashMap<>();
            for (Entry<Key, Integer> entry : table) {
                assertFalse(actual.containsKey(entry.getKey()));
                actual.put(entry.getKey(), entry.getValue());
            }
            assertEquals(expected, actual);
            assertEquals(expected.hashCode(), table.hashCode());
        }
    }
}
