package ru.nsu.esapronov.task141;

/** Виды контроля из учебного плана. */
public enum ControlType {
    /** Задание. */
    ASSIGNMENT,
    /** Контрольная работа. */
    TEST,
    /** Коллоквиум. */
    COLLOQUIUM,
    /** Экзамен. */
    EXAM,
    /** Дифференцированный зачет. */
    DIFFERENTIATED_CREDIT,
    /** Обычный зачет. */
    CREDIT,
    /** Защита отчета по практике. */
    PRACTICE_REPORT,
    /** Защита работы. */
    THESIS;

    /**
     * Учитывается ли контроль при выборе последней оценки по дисциплине.
     *
     * @return является ли контроль итоговым числовым контролем дисциплины
     */
    public boolean isDiplomaControl() {
        return this == EXAM || this == DIFFERENTIATED_CREDIT || this == PRACTICE_REPORT;
    }
}
