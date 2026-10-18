package ru.nsu.esapronov.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Тесты класса Expression. */
class ExpressionTest {
    @Test
    void parseBuildsAnExpression() {
        assertEquals(5, Expression.parse("(2+3)").eval(""));
    }

    @Test
    void assignmentsAllowSpacesAndSeveralVariables() {
        Expression expression = Expression.parse("(x+(y*z))");

        assertEquals(-7, expression.eval(" x = 5 ; y = -4 ; z = 3 "));
    }

    @Test
    void invalidAssignmentsAreRejected() {
        Expression expression = new Variable("x");
        String[] assignments = {"x", "x=", "x=abc", "1x=2", "x=1=2",
            "x=2147483648", "x=1;", "x=1;;y=2", "x=1;x=2"};
        for (String assignment : assignments) {
            assertThrows(IllegalArgumentException.class,
                    () -> expression.eval(assignment), assignment);
        }
    }
}
