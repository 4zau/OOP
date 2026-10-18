package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Тесты класса Sub. */
class SubTest {
    @Test
    void subtractionWorks() {
        Expression expression = new Sub(new Number(3), new Number(7));
        assertEquals(-4, expression.eval(""));
        assertEquals("(3-7)", expression.toString());
    }

    @Test
    void differenceDerivativeIsEvaluated() {
        Expression derivative = Expression.parse("((3*x)-x)").derivative("x");

        assertEquals(2, derivative.eval("x=5"));
    }
}
