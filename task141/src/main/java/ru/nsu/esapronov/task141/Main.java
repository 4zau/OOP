package ru.nsu.esapronov.task141;

import java.util.List;

/** Пример. */
public final class Main {
    private Main() {
    }

    /**
     * Запускает пример с небольшим демонстрационным учебным планом.
     *
     * @param args аргументы командной строки, не используются
     */
    public static void main(String[] args) {
        RecordBook book = new RecordBook("Михаил Иванов", EducationForm.PAID, 2, List.of(
                new Assessment("math1", "Математика", 1, ControlType.EXAM),
                new Assessment("oop", "ООП", 2, ControlType.DIFFERENTIATED_CREDIT),
                new Assessment("pe", "Физкультура", 2, ControlType.CREDIT),
                new Assessment("math2", "Математика", 3, ControlType.EXAM),
                new Assessment("thesis", "ВКР", 8, ControlType.THESIS)));
        book.recordGrade("math1", Grade.GOOD);
        book.recordGrade("oop", Grade.EXCELLENT);
        book.recordGrade("pe", Grade.PASS);
        book.setCurrentSemester(3);

        System.out.println("Студент: " + book.getStudentName());
        System.out.println("Средний балл: " + book.averageGrade().orElseThrow());
        System.out.println("Возможен перевод на бюджет: " + book.canTransferToBudget());
        System.out.println("Возможен красный диплом: " + book.canGetHonorsDiploma());
        book.setEducationForm(EducationForm.BUDGET);
        System.out.println("Повышенная стипендия: " + book.canGetIncreasedScholarship());
    }
}
