package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Тесты класса Add. */
class AddTest {
    @Test
    void additionEvaluatesAndPrintsOperands() {
        Expression expression = new Add(new Number(7), new Number(3));
        assertEquals(10, expression.eval(""));
        assertEquals("(7+3)", expression.toString());
    }

    @Test
    void sumDerivativeIsEvaluated() {
        Expression derivative = Expression.parse("(x+(2*x))").derivative("x");

        assertEquals(3, derivative.eval("x=5"));
    }
}
