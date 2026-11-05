package ru.nsu.esapronov.task122;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Хеш-таблица с цепочками для разрешения коллизий.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class HashTable<K, V> implements Iterable<Entry<K, V>> {
    private static final int MIN_CAPACITY = 16;
    private static final int MAX_CAPACITY = 1 << 30;
    private Node<K, V>[] buckets;
    private int size;
    private int modCount;

    public HashTable() {
        buckets = createArray(MIN_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] createArray(int capacity) {
        return (Node<K, V>[]) new Node<?, ?>[capacity];
    }

    private static int index(Object key, int capacity) {
        int hash = Objects.hashCode(key);
        hash ^= hash >>> 16;
        return hash & (capacity - 1);
    }

    private Node<K, V> findNode(Object key) {
        Node<K, V> node = buckets[index(key, buckets.length)];
        while (node != null) {
            if (Objects.equals(node.key, key)) {
                return node;
            }
            node = node.next;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Добавляет пару или заменяет значение существующего ключа. */
    public void put(K key, V value) {
        Node<K, V> existing = findNode(key);
        if (existing != null) {
            existing.value = value;
            return;
        }
        int threshold = buckets.length - buckets.length / 4;
        if (size >= threshold && buckets.length < MAX_CAPACITY) {
            resize(buckets.length * 2);
        }
        int bucket = index(key, buckets.length);
        buckets[bucket] = new Node<>(key, value, buckets[bucket]);
        size++;
        modCount++;
    }

    /** Возвращает null при отсутствии ключа. */
    public V get(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    /** Обновляет существующий ключ. */
    public void update(K key, V value) {
        Node<K, V> node = findNode(key);
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        node.value = value;
    }

    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    /** Удаляет пару по ключу; сообщает, была ли она найдена. */
    public boolean remove(K key) {
        int bucket = index(key, buckets.length);
        Node<K, V> previous = null;
        Node<K, V> current = buckets[bucket];
        while (current != null) {
            if (Objects.equals(current.key, key)) {
                if (previous == null) {
                    buckets[bucket] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                modCount++;
                if (buckets.length > MIN_CAPACITY && size < buckets.length / 4) {
                    resize(buckets.length / 2);
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    /** Удаляет пару, только если совпадают и ключ, и значение. */
    public boolean remove(K key, V value) {
        Node<K, V> node = findNode(key);
        return node != null && Objects.equals(node.value, value) && remove(key);
    }

    private void resize(int capacity) {
        Node<K, V>[] newBuckets = createArray(capacity);
        for (Node<K, V> head : buckets) {
            Node<K, V> current = head;
            while (current != null) {
                Node<K, V> next = current.next;
                int bucket = index(current.key, capacity);
                current.next = newBuckets[bucket];
                newBuckets[bucket] = current;
                current = next;
            }
        }
        buckets = newBuckets;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new TableIterator<>(buckets, modCount, () -> modCount);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof HashTable<?, ?>)) {
            return false;
        }
        HashTable<?, ?> other = (HashTable<?, ?>) object;
        if (size != other.size) {
            return false;
        }
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                Node<?, ?> otherNode = other.findNode(node.key);
                if (otherNode == null || !Objects.equals(node.value, otherNode.value)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Node<K, V> head : buckets) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                result += Objects.hashCode(node.key) ^ Objects.hashCode(node.value);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> entry : this) {
            if (!first) {
                result.append(", ");
            }
            result.append(entry);
            first = false;
        }
        return result.append('}').toString();
    }
}
