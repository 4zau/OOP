package ru.nsu.esapronov.task113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Запускает работу с выражением, прочитанным с консоли или из файла.
 * Первая строка содержит выражение, вторая — значения переменных,
 * третья — имя переменной для дифференцирования.
 */
public class Main {
    /**
     * Печатает выражение, его значение и производную, если переданы нужные строки.
     *
     * @param args необязательное имя файла с входными данными
     * @throws IOException если не удалось прочитать входные данные
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("too much arguments");
            return;
        }

        Reader source = args.length == 1
                ? Files.newBufferedReader(Path.of(args[0]))
                : new java.io.InputStreamReader(System.in);

        try (BufferedReader input = new BufferedReader(source)) {
            String line = input.readLine();
            if (line == null) {
                throw new IllegalArgumentException("expected an expression");
            }
            Expression expression = Expression.parse(line);
            expression.print();

            String assignments = input.readLine();
            if (assignments != null && !assignments.isBlank()) {
                System.out.println(expression.eval(assignments));
            }

            String variable = input.readLine();
            if (variable != null && !variable.isBlank()) {
                expression.derivative(variable.strip()).print();
            }
        }
    }
}