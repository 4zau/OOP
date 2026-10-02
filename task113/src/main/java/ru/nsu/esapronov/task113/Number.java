package ru.nsu.esapronov.task113;

import java.util.Map;

/** Целочисленная константа. */
final class Number extends Expression {
    private final int value;

    /**
     * Создаёт константу.
     *
     * @param value значение константы
     */
    public Number(int value) {
        this.value = value;
    }

    @Override
    public Expression derivative(String variable) {
        return new Number(0);
    }

    @Override
    protected int eval(Map<String, Integer> variables) {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}