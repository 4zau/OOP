package ru.nsu.esapronov.task122;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntSupplier;

/**
 * Итератор по парам хеш-таблицы с проверкой структурных изменений.
 *
 * @param <K> тип ключа
 * @param <V> тип значения
 */
public class TableIterator<K, V> implements Iterator<Entry<K, V>> {
    private final Node<K, V>[] buckets;
    private final int expectedModCount;
    private final IntSupplier currentModCount;

    private int bucketIndex;
    private Node<K, V> nextNode;

    /**
     * Создаёт итератор по текущим корзинам таблицы.
     *
     * @param buckets корзины таблицы
     * @param expectedModCount число изменений при создании итератора
     * @param currentModCount источник текущего числа изменений
     */
    public TableIterator(Node<K, V>[] buckets,
                         int expectedModCount,
                         IntSupplier currentModCount) {
        this.buckets = buckets;
        this.expectedModCount = expectedModCount;
        this.currentModCount = currentModCount;
        advanceToNextBucket();
    }

    private void checkModification() {
        if (expectedModCount != currentModCount.getAsInt()) {
            throw new ConcurrentModificationException();
        }
    }

    private void advanceToNextBucket() {
        while (nextNode == null && bucketIndex < buckets.length) {
            nextNode = buckets[bucketIndex++];
        }
    }

    @Override
    public boolean hasNext() {
        checkModification();
        return nextNode != null;
    }

    @Override
    public Entry<K, V> next() {
        checkModification();

        if (nextNode == null) {
            throw new NoSuchElementException();
        }

        Node<K, V> current = nextNode;
        nextNode = current.next;
        advanceToNextBucket();

        return new Entry<>(current.key, current.value);
    }
}
