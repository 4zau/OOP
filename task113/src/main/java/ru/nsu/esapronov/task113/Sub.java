package ru.nsu.esapronov.task113;

import java.util.Map;

/** Разность двух выражений. */
final class Sub extends BinaryExpression {
    /**
     * Создаёт разность.
     *
     * @param left уменьшаемое
     * @param right вычитаемое
     */
    public Sub(Expression left, Expression right) {
        super(left, right);
    }

    @Override protected char operator() {
        return '-';
    }

    @Override public Expression derivative(String variable) {
        return new Sub(left.derivative(variable), right.derivative(variable));
    }

    @Override protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) - right.eval(variables);
    }
}
