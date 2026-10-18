package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Тесты класса Parser. */
class ParserTest {
    @Test
    void parserRejectsNullInput() {
        assertThrows(NullPointerException.class, () -> new Parser(null));
    }

    @Test
    void parserReadsAllOperations() {
        assertEquals(10, Expression.parse("(7+3)").eval(""));
        assertEquals(4, Expression.parse("(7-3)").eval(""));
        assertEquals(21, Expression.parse("(7*3)").eval(""));
        assertEquals(2, Expression.parse("(7/3)").eval(""));
    }

    @Test
    void parserReadsSpacesAndNestedExpressions() {
        Expression expression = Expression.parse("  ( 3 + ( 2 * x ) )  ");

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x=10"));
    }

    @Test
    void parserReadsNegativeNumbersAndVariableNames() {
        Expression expression = Expression.parse("(-12+_value2)");

        assertEquals("(-12+_value2)", expression.toString());
        assertEquals(-7, expression.eval("_value2=5"));
    }

    @Test
    void parserRejectsInvalidExpressions() {
        String[] texts = {"", " ", "(1+)", "(1+2", "1+2", "(1%2)",
            "(1 2)", "(1+2)extra", "-x", "-", "(1", "@", "(1)", "((1+2)+3))"};
        for (String text : texts) {
            assertThrows(IllegalArgumentException.class, () -> Expression.parse(text), text);
        }
    }
}
