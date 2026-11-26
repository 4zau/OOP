package ru.nsu.esapronov.task141;

import java.util.Objects;

/**
 * Пункт учебного плана.
 *
 * @param id уникальный идентификатор контроля
 * @param subject название дисциплины или практики
 * @param semester номер семестра начиная с 1
 * @param type вид контроля
 */
public record Assessment(String id, String subject, int semester, ControlType type) {
    /** Проверяет обязательные поля пункта плана. */
    public Assessment {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(type, "type");
        if (id.isBlank() || subject.isBlank() || semester < 1) {
            throw new IllegalArgumentException("Нужны id, дисциплина и положительный семестр");
        }
    }
}
