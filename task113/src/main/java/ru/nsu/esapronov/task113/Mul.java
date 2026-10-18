package ru.nsu.esapronov.task113;

import java.util.Map;

/** Произведение двух выражений. */
final class Mul extends BinaryExpression {
    /**
     * Создаёт произведение.
     *
     * @param left первый множитель
     * @param right второй множитель
     */
    public Mul(Expression left, Expression right) {
        super(left, right);
    }

    @Override protected char operator() {
        return '*';
    }

    /** Строит производную по правилу произведения: u' * v + u * v'. */
    @Override public Expression derivative(String variable) {
        return new Add(new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
    }

    @Override protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) * right.eval(variables);
    }
}
