package ru.nsu.esapronov.task141;

/** Результаты контроля. */
public enum Grade {
    /** Неудовлетворительно. */
    FAIL(2),
    /** Удовлетворительно. */
    SATISFACTORY(3),
    /** Хорошо. */
    GOOD(4),
    /** Отлично. */
    EXCELLENT(5),
    /** Зачтено. */
    PASS(0),
    /** Не зачтено. */
    NOT_PASSED(0);

    private final int points;

    Grade(int points) {
        this.points = points;
    }

    /**
     * Числовое значение результата.
     *
     * @return оценка 2–5 или 0 для обычного зачета
     */
    public int points() {
        return points;
    }

    /**
     * Проверяет наличие числовой оценки.
     *
     * @return есть ли у результата оценка 2–5
     */
    public boolean isNumeric() {
        return points != 0;
    }
}
