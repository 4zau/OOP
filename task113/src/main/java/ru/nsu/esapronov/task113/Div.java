package ru.nsu.esapronov.task113;

import java.util.Map;

/** Частное двух выражений с целочисленным делением при вычислении. */
final class Div extends BinaryExpression {
    /**
     * Создаёт частное.
     *
     * @param left числитель
     * @param right знаменатель
     */
    public Div(Expression left, Expression right) {
        super(left, right);
    }

    @Override protected char operator() {
        return '/';
    }

    /** Строит производную по правилу частного: (u' * v - u * v') / (v * v). */
    @Override public Expression derivative(String variable) {
        return new Div(
                new Sub(new Mul(left.derivative(variable), right),
                        new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }

    @Override protected int eval(Map<String, Integer> variables) {
        return left.eval(variables) / right.eval(variables);
    }
}