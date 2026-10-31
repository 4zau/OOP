package ru.nsu.esapronov.task122;

import java.util.Objects;

final class Entry<K, V> {
    final K key;
    final V value;

    Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }

    @Override
    public boolean equals(Object object) {
        if (this == object) { return true; }
        if (!(object instanceof Entry<?, ?>)) { return false; }
        Entry<?, ?> other = (Entry<?, ?>) object;
        return Objects.equals(key, other.key)
                && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    @Override
    public String toString() { return key + "=" + value; }
}