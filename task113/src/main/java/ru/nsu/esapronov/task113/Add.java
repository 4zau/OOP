package ru.nsu.esapronov.task113;

import java.util.Map;

/** Сумма двух выражений. */
final class Add extends BinaryExpression {
    /**
     * Создаёт сумму.
     *
     * @param left первое слагаемое
     * @param right второе слагаемое
     */
    public Add(Expression left, Expression right) {
        super(left, right);
    }

    @Override protected char operator() {
        return '+';
    }

    @Override public Expression derivative(String variable) {
        return new Add(left.derivative(variable), right.derivative(variable));
    }

    @Override protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) + right.eval(variables);
    }
}
