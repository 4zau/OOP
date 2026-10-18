package ru.nsu.esapronov.task121;

import java.util.Objects;

/** Направленное ребро. */
public record Edge<V>(V from, V to) {
    public Edge {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
    }
}
