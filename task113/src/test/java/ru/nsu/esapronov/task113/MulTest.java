package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Тесты класса Mul. */
class MulTest {
    @Test
    void multiplicationEvaluatesAndPrintsOperands() {
        Expression expression = new Mul(new Number(-7), new Number(3));
        assertEquals(-21, expression.eval(""));
        assertEquals("(-7*3)", expression.toString());
    }

    @Test
    void productDerivativeUsesBothOperands() {
        Expression expression = Expression.parse("(x*x)");
        Expression derivative = expression.derivative("x");

        assertEquals("((1*x)+(x*1))", derivative.toString());
        assertEquals(10, derivative.eval("x=5"));
        assertEquals(-6, derivative.eval("x=-3"));
        assertEquals("(x*x)", expression.toString());
    }

    @Test
    void derivativeDistinguishesVariables() {
        Expression expression = Expression.parse("(x*y)");

        assertEquals(7, expression.derivative("x").eval("x=3;y=7"));
        assertEquals(3, expression.derivative("y").eval("x=3;y=7"));
        assertEquals(0, expression.derivative("z").eval("x=3;y=7"));
    }

    @Test
    void secondDerivativeCanBeEvaluated() {
        Expression expression = Expression.parse("(x*x)");

        assertEquals(2, expression.derivative("x").derivative("x").eval("x=9"));
    }
}
